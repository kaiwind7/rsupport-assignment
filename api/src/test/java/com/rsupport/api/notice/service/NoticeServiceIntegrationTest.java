package com.rsupport.api.notice.service;

import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import com.rsupport.api.domain.notice.repository.NoticeAttachmentRepository;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import com.rsupport.api.domain.notice.request.NoticeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class NoticeServiceIntegrationTest {
    @Autowired
    private NoticeService noticeService;

    @Autowired
    private NoticeRepository noticeRepository;

    @Autowired
    private NoticeAttachmentRepository noticeAttachmentRepository;

    @Test
    void testSaveNoticeIntegration() {
        NoticeRequest request = NoticeRequest.builder()
                .title("통합 테스트 공지 타이틀")
                .content("통합 테스트 공지 내용")
                .author("jk")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now().plusDays(1)).build();

        NoticeDTO savedNotice = noticeService.saveNotice(request, new ArrayList<>());

        assertNotNull(savedNotice);
        assertEquals("통합 테스트 공지 타이틀", savedNotice.getTitle());
    }

    @Test
    void testFindNoticeIntegration() {
        Notice notice = Notice.builder()
                .title("Find Notice Test")
                .content("Find Notice Content")
                .author("Author")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now().plusDays(1))
                .build();
        noticeRepository.save(notice);

        NoticeDTO foundNotice = noticeService.findNotice(notice.getId());

        assertNotNull(foundNotice);
        assertEquals("Find Notice Test", foundNotice.getTitle());
    }

    @Test
    void testDeleteNoticeIntegration() throws IOException {
        NoticeAttachment attachment = NoticeAttachment.builder()
                .fileName("testFile.txt")
                .filePath(Files.createTempFile(UUID.randomUUID().toString(), "testFile.txt").toString())
                .build();
        Notice notice = Notice.builder()
                .title("Test Notice for Deletion")
                .content("Test Notice for Deletion")
                .author("jk")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now())
                .attachments(new ArrayList<>(List.of(attachment))).build();

        attachment.assignNotice(notice);

        noticeRepository.save(notice);

        noticeService.deleteNotice(notice.getId());

        Optional<Notice> deletedNotice = noticeRepository.findById(notice.getId());
        assertFalse(deletedNotice.isPresent());
    }

    @Test
    void testUpdateNoticeIntegration() {
        Notice notice = Notice.builder()
                .title("Old Title")
                .content("Old Content")
                .author("jk")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now()).build();
        noticeRepository.save(notice);

        NoticeRequest request = NoticeRequest.builder()
                .title("Updated Title")
                .content("Updated Content").build();

        NoticeDTO updatedNotice = noticeService.updateNotice(notice.getId(), request, new ArrayList<>());

        assertEquals("Updated Title", updatedNotice.getTitle());
        assertEquals("Updated Content", updatedNotice.getContent());
    }

    @Test
    void testIncrementViewsIntegration() throws InterruptedException {
        Notice notice = Notice.builder().title("조회수 증가 테스트")
                .content("조회수 증가 테스트")
                .author("jk")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now())
                .views(0).build();
        noticeRepository.save(notice);

        // CountDownLatch를 사용하여 비동기 작업 완료 대기
        CountDownLatch latch = new CountDownLatch(1);

        // incrementViews 호출 후 작업 완료 기다림
        noticeService.incrementViews(notice.getId());

        // latch를 대기하기 전에 약간의 시간을 둠
        latch.await(3, TimeUnit.SECONDS);

        Notice updatedNotice = noticeRepository.findById(notice.getId()).orElseThrow();
        assertEquals(1, updatedNotice.getViews());
    }
}
