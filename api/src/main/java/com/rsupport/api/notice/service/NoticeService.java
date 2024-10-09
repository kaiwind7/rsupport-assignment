package com.rsupport.api.notice.service;

import com.rsupport.api.common.enums.ErrorCode;
import com.rsupport.api.common.exception.ServiceException;
import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NoticeService {
    private final NoticeRepository noticeRepository;

    /**
     * 공지사항 조회
     *
     * @param noticeId 공지사항ID
     * @return NoticeDTO 공지사항DTO
     */
    @Transactional(readOnly = true)
    public NoticeDTO findNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        return NoticeDTO.fromEntity(notice);
    }

    /**
     * 공지사항 조회
     *
     * @param noticeId 공지사항ID
     * @return Notice 공지사항 Entity
     */
    private Notice findNoticeById(Long noticeId) {
        return noticeRepository.findById(noticeId).orElseThrow(() -> new ServiceException("해당 공지가 존재하지 않습니다.", ErrorCode.NOT_FOUND_ENTITY));
    }

    /**
     * 공지사항 저장
     *
     * @param noticeDTO 공지사항 저장 정보
     * @return 공지사항 저장정보
     */
    @Transactional
    public NoticeDTO saveNotice(NoticeDTO noticeDTO) {
        List<NoticeAttachment> noticeAttachment = noticeDTO.getAttachments().stream()
                .map(attachment -> NoticeAttachment.builder()
                        .fileName(attachment.getFileName())
                        .filePath(attachment.getFilePath()).build())
                .toList();
        Notice notice = noticeRepository.save(Notice.builder()
                .title(noticeDTO.getTitle())
                .content(noticeDTO.getContent())
                .startDatetime(noticeDTO.getStartDatetime())
                .endDatetime(noticeDTO.getEndDatetime())
                .attachments(noticeAttachment).build());

        return NoticeDTO.fromEntity(notice);
    }


    /**
     * 공지사항 변경
     *
     * @param noticeId  공지사항ID
     * @param noticeDTO 공지사항 변경 정보
     * @return 변경된 공지사항DTO
     */
    @Transactional
    public NoticeDTO updateNotice(Long noticeId, NoticeDTO noticeDTO) {
        Notice notice = findNoticeById(noticeId);
        notice.modifyNotice(noticeDTO);
        return NoticeDTO.fromEntity(notice);
        //파일 수정 시 전체 데이터 삭제 후 새로 업데이트? 아니면 데이터 추가? 파일만 삭제되는 케이스도 있을거 같은데 ...어떤 방법이 좋을까..
    }

    /**
     * 공지사항 삭제
     *
     * @param noticeId 공지사항ID
     */
    @Transactional
    public void deleteNotice(Long noticeId) {
        Notice notice = findNoticeById(noticeId);
        noticeRepository.delete(notice);
    }

}
