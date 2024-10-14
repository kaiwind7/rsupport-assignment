package com.rsupport.api.domain.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항처리 큐 리퀘스트")
public class NoticeProcessRequest {
    @Schema(description = "공지사항 저장 리퀘스트")
    private NoticeRequest noticeRequest;
    @Schema(description = "공지사항 첨부파일 저장")
    private List<MultipartFile> files;
}
