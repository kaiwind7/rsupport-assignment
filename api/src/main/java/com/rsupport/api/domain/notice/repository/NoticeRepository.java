package com.rsupport.api.domain.notice.repository;

import com.rsupport.api.domain.notice.entity.Notice;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface NoticeRepository extends JpaRepository<Notice, Long> {
    @Query("SELECT n " +
            "FROM Notice n " +
            "LEFT JOIN FETCH n.attachments " +
            "WHERE n.startDatetime <= :now " +
            "AND n.endDatetime >= :now ")
    Page<Notice> findActiveNotices(@Param("now") LocalDateTime now, Pageable pageable);

    @Modifying
    @Query("UPDATE Notice n SET n.views = n.views + 1 WHERE n.id = :noticeId")
    void incrementViews(@Param("noticeId") Long noticeId);
}
