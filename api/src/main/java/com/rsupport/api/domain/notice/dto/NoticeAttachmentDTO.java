package com.rsupport.api.domain.notice.dto;

import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "첨부파일")
public class NoticeAttachmentDTO {
    @Schema(description = "파일명")
    private String fileName;
    @Schema(description = "파일경로")
    private String filePath;

    public static NoticeAttachmentDTO fromEntity(NoticeAttachment noticeAttachment) {
        return NoticeAttachmentDTO.builder()
                .fileName(noticeAttachment.getFileName())
                .filePath(noticeAttachment.getFilePath())
                .build();
    }
}
