package com.rsupport.api.domain.notice.entity;

import com.rsupport.api.domain.notice.dto.NoticeDTO;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Entity
@Table(name = "notice")
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Lob
    @Column(nullable = false)
    private String content;

    @Column(name = "start_datetime", nullable = false)
    private LocalDateTime startDatetime;

    @Column(name = "end_datetime", nullable = false)
    private LocalDateTime endDatetime;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "announcement", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeAttachment> attachments;

    public void modifyNotice(NoticeDTO noticeDTO) {
        this.title = noticeDTO.getTitle();
        this.content = noticeDTO.getContent();
        this.startDatetime = noticeDTO.getStartDatetime();
        this.endDatetime = noticeDTO.getEndDatetime();
        this.attachments = noticeDTO.getAttachments().stream()
                .map(attachments -> NoticeAttachment.builder()
                        .fileName(attachments.getFileName())
                        .filePath(attachments.getFilePath()).build())
                .toList();
    }
}
