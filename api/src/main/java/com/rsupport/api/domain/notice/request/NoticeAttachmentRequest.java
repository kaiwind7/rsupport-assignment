package com.rsupport.api.domain.notice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeAttachmentRequest {
    private String fileName;
    private String filePath;
    private MultipartFile file;
}
