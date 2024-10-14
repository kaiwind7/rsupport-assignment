package com.rsupport.api.domain.notice.dto;

import com.rsupport.api.domain.notice.entity.Notice;
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
@Schema(description = "공지사항")
public class NoticeDTO {
    @Schema(description = "공지사항 제목")
    private String title;
    @Schema(description = "공지사항내용")
    private String content;
    @Schema(description = "작성자")
    private String author;
    @Schema(description = "조회수")
    private int views;
    @Schema(description = "공지 시작시간")
    private LocalDateTime startDatetime;
    @Schema(description = "공지 종료시간")
    private LocalDateTime endDatetime;
    @Schema(description = "공지사항 생성시간")
    private LocalDateTime createdAt;
    @Schema(description = "첨부파일")
    private List<NoticeAttachmentDTO> attachments;

    public static NoticeDTO fromEntity(Notice notice) {
        List<NoticeAttachmentDTO> noticeAttachmentDTOs = null;
        if (!notice.getAttachments().isEmpty() && notice.getAttachments() != null) {
            noticeAttachmentDTOs = notice.getAttachments().stream()
                    .map(NoticeAttachmentDTO::fromEntity)
                    .toList();
        }

        return NoticeDTO.builder()
                .title(notice.getTitle())
                .content(notice.getContent())
                .author(notice.getAuthor())
                .views(notice.getViews())
                .createdAt(notice.getCreatedAt())
                .startDatetime(notice.getStartDatetime())
                .endDatetime(notice.getEndDatetime())
                .attachments(noticeAttachmentDTOs).build();
    }
}
