package com.rsupport.api.domain.notice.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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

    @NotBlank(message = "공지사항 제목은 필수 항목입니다.")
    @Schema(description = "공지사항제목")
    private String title;

    @NotBlank(message = "공지사항 내용은 필수 항목입니다.")
    @Schema(description = "공지사항내용")
    private String content;

    @Schema(description = "공지 시작일자")
    private LocalDateTime startDatetime;

    @Schema(description = "공지 종료일자")
    private LocalDateTime endDatetime;

    @NotBlank(message = "작성자는 필수 항목입니다.")
    @Schema(description = "작성자")
    private String author;

    @Schema(description = "삭제파일목록")
    private List<Long> deleteFiles;
}
