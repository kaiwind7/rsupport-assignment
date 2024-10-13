package com.rsupport.api.domain.notice.dto;

import com.rsupport.api.domain.notice.entity.Notice;
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
public class NoticeDTO {
    private String title;
    private String content;
    private String author;
    private int views;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private LocalDateTime createdAt;
    private List<NoticeAttachmentDTO> attachments;

    public static NoticeDTO fromEntity(Notice notice) {
        List<NoticeAttachmentDTO> noticeAttachmentDTOs = notice.getAttachments().stream()
                .map(NoticeAttachmentDTO::fromEntity)
                .toList();

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
