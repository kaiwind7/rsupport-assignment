package com.rsupport.api.domain.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "공지사항요청")
public class NoticeRequest {
    @Schema(description = "공지사항ID")
    private Long id;
    @Schema(description = "공지사항제목")
    private String title;
    @Schema(description = "공지사항내용")
    private String content;
    @Schema(description = "공지 시작일자")
    private LocalDateTime startDatetime;
    @Schema(description = "공지 종료일자")
    private LocalDateTime endDatetime;
    @Schema(description = "작성자")
    private String author;
    @Schema(description = "삭제파일목록")
    private List<Long> deleteFiles;
}
