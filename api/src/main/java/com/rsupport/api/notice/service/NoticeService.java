package com.rsupport.api.notice.service;

import com.rsupport.api.common.enums.ErrorCode;
import com.rsupport.api.common.exception.ServiceException;
import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import com.rsupport.api.domain.notice.repository.NoticeAttachmentRepository;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import com.rsupport.api.domain.notice.request.NoticeRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;
    private final NoticeAttachmentRepository noticeAttachmentRepository;

    @Value("${notice.file.path}")
    private String NOTICE_FILE_PATH;

    /**
     * 공지기간 내 공지사항 목록 조회
     *
     * @param pageable
     * @return
     */
    @Cacheable(value = "notices", key = "#pageable.pageNumber + '-' + #pageable.pageSize")
    public Page<NoticeDTO> findNoticeAll(Pageable pageable) {
        return noticeRepository.findActiveNotices(LocalDateTime.now(), pageable)
                .map(NoticeDTO::fromEntity);
    }

    /**
     * 공지사항 조회 DTO 변환
     *
     * @param noticeId 공지사항ID
     * @return NoticeDTO 공지사항DTO
     */
    @Transactional(readOnly = true)
    public NoticeDTO findNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        incrementViews(noticeId);
        return NoticeDTO.fromEntity(notice);
    }


    /**
     * 공지사항 조회수 증가
     *
     * @param noticeId 공지사항ID
     */
    @Async
    @Transactional
    public void incrementViews(Long noticeId) {
        noticeRepository.incrementViews(noticeId);
    }

    /**
     * 공지사항 조회
     *
     * @param noticeId 공지사항ID
     * @return Notice 공지사항 Entity
     */
    private Notice findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId)
                .orElseThrow(() -> new ServiceException("해당 공지가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));
    }

    /**
     * 공지사항 저장
     *
     * @param request 공지사항 저장 정보
     * @return 공지사항 저장정보
     */
    @Transactional
    @CacheEvict(value = "notices", allEntries = true)
    public NoticeDTO saveNotice(NoticeRequest request, List<MultipartFile> files) {
        Notice notice = Notice.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .author(request.getAuthor())
                .startDatetime(request.getStartDatetime())
                .endDatetime(request.getEndDatetime())
                .attachments(new ArrayList<>()).build();

        List<NoticeAttachment> noticeAttachments = saveNoticeAttachmentFile(files);
        noticeAttachments.forEach(notice::addAttachment);

        Notice resultNotice = noticeRepository.save(notice);
        return NoticeDTO.fromEntity(resultNotice);
    }


    /**
     * 첨부파일 저장
     *
     * @param files 파일목록
     * @return List<NoticeAttachment>
     */
    public List<NoticeAttachment> saveNoticeAttachmentFile(List<MultipartFile> files) {
        Path relativePath = createAttachmentDirectory();
        return files.stream()
                .map(file -> saveSingleFile(file, relativePath))
                .toList();

    }

    /**
     * 첨부파일 경로 생성
     *
     * @return Path
     */
    private Path createAttachmentDirectory() {
        Path currentDirectory = Paths.get("").toAbsolutePath();
        Path relativePath = currentDirectory.resolve(NOTICE_FILE_PATH + UUID.randomUUID());

        if (!Files.exists(relativePath)) {
            try {
                Files.createDirectories(relativePath);
            } catch (IOException e) {
                throw new ServiceException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }
        return relativePath;
    }

    /**
     * 파일 저장
     *
     * @param file         첨부파일
     * @param relativePath 첨부파일 경로
     * @return NoticeAttachment
     */
    private NoticeAttachment saveSingleFile(MultipartFile file, Path relativePath) {
        Path filePath = relativePath.resolve(file.getOriginalFilename());
        try {
            Files.write(filePath, file.getBytes());
        } catch (IOException e) {
            throw new ServiceException(ErrorCode.FILE_UPLOAD_FAILED);
        }
        log.info("File saves to: {}", filePath);
        return NoticeAttachment.builder()
                .fileName(file.getOriginalFilename())
                .filePath(filePath.toString())
                .build();
    }

    /**
     * 공지사항 변경
     *
     * @param noticeId 공지사항ID
     * @param request  공지사항 변경 정보
     * @param files    첨부파일 추가
     * @return
     */
    @Transactional
    public NoticeDTO updateNotice(Long noticeId, NoticeRequest request, List<MultipartFile> files) {
        Notice notice = findNoticeById(noticeId);

        notice.modifyNotice(request);

        // 첨부파일 삭제
        if (request.getDeleteFiles() != null && !request.getDeleteFiles().isEmpty()) {
            deleteAttachmentFile(notice, request.getDeleteFiles());
        }

        // 첨부파일 추가
        List<NoticeAttachment> noticeAttachment = saveNoticeAttachmentFile(files);
        noticeAttachment.stream().forEach(notice::addAttachment);

        return NoticeDTO.fromEntity(notice);
    }

    /**
     * 첨부파일 삭제 (데이터)
     *
     * @param notice  공지사항 정보
     * @param fileIds 삭제 첨부파일 ID
     */
    private void deleteAttachmentFile(Notice notice, List<Long> fileIds) {
        List<NoticeAttachment> attachmentsToRemove = notice.getAttachments().stream()
                .filter(attachment -> fileIds.contains(attachment.getId()))
                .toList();

        if (attachmentsToRemove.isEmpty()) {
            throw new ServiceException("삭제할 첨부파일이 없습니다.", ErrorCode.NOT_FOUND_ENTITY);
        }

        attachmentsToRemove.forEach(attachment -> {
            notice.getAttachments().remove(attachment);
            noticeAttachmentRepository.delete(attachment);
            deletePhysicalFile(attachment.getFilePath());
        });
    }

    /**
     * 첨부파일 삭제 (실제 파일)
     *
     * @param filePath 파일경로
     */
    private void deletePhysicalFile(String filePath) {
        try {
            Path path = Paths.get(filePath);
            if (Files.exists(path)) {
                Files.delete(path);
                log.info("File deleted: {}", filePath);
            } else {
                log.info("File not found: {}", filePath);
            }
        } catch (IOException e) {
            log.error("파일 삭제 에러 : {}", filePath, e);
            throw new ServiceException("파일 삭제 중 오류가 발생했습니다: " + filePath, ErrorCode.FILE_DELETE_ERROR);
        }
    }


    /**
     * 공지사항 삭제
     *
     * @param noticeId 공지사항ID
     */
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);

        notice.getAttachments().stream().forEach(noticeAttachment -> {
            deletePhysicalFile(noticeAttachment.getFilePath());
        });

        noticeRepository.delete(notice);
    }

}
