package com.rsupport.api.common.utils;

import com.rsupport.api.common.enums.ErrorCode;
import com.rsupport.api.common.exception.ServiceException;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class AttachmentFileUtils {
    private static String NOTICE_FILE_PATH;

    @Value("${notice.file.path}")
    public void setNoticeFilePath(String filePath) {
        AttachmentFileUtils.NOTICE_FILE_PATH = filePath;
    }

    /**
     * 첨부파일 저장
     *
     * @param files 파일목록
     * @return List<NoticeAttachment>
     */
    public static List<NoticeAttachment> saveNoticeAttachmentFile(List<MultipartFile> files) {
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
    private static Path createAttachmentDirectory() {
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
     * @param file         파일
     * @param relativePath 상대경로
     * @return NoticeAttachment
     */
    private static NoticeAttachment saveSingleFile(MultipartFile file, Path relativePath) {
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
}
