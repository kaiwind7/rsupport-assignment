package com.rsupport.api.domain.notice.entity;

import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.request.NoticeRequest;
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
import java.util.ArrayList;
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

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "views", nullable = false)
    private int views = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "notice", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<NoticeAttachment> attachments;

    public void modifyNotice(NoticeRequest noticeRequest) {
        this.title = noticeRequest.getTitle();
        this.content = noticeRequest.getContent();
        this.startDatetime = noticeRequest.getStartDatetime();
        this.endDatetime = noticeRequest.getEndDatetime();
    }

    // 연관 관계 편의 메서드
    public void addAttachment(NoticeAttachment attachment) {
        this.attachments.add(attachment);
        attachment.assignNotice(this); // Notice 설정
    }

    // 조회수 증가 메서드
    public void incrementViews() {
        this.views++;
    }
}
