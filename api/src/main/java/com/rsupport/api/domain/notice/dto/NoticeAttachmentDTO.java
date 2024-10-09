package com.rsupport.api.domain.notice.dto;

import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttachmentDTO {
    //private Notice notice;
    private String fileName;
    private String filePath;
    //private LocalDateTime createdAt;

    public static NoticeAttachmentDTO fromEntity(NoticeAttachment noticeAttachment){
        return NoticeAttachmentDTO.builder()
      //          .notice(noticeAttachment.getNotice())
                .fileName(noticeAttachment.getFileName())
                .filePath(noticeAttachment.getFilePath())
                .build();
    }
}
