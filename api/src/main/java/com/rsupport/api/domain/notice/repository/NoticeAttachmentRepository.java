package com.rsupport.api.domain.notice.repository;

import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeAttachmentRepository extends JpaRepository<NoticeAttachment, Long> {
}
