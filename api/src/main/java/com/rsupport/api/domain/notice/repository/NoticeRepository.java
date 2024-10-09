package com.rsupport.api.domain.notice.repository;

import com.rsupport.api.domain.notice.entity.Notice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
}
