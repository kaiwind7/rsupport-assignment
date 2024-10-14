package com.rsupport.api.notice.service;

import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentLinkedQueue;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class NoticeQueueServiceUnitTest {
    @InjectMocks
    private NoticeQueueService noticeQueueService;

    @Mock
    private NoticeRepository noticeRepository;

    @Spy
    private ConcurrentLinkedQueue<Notice> queue = new ConcurrentLinkedQueue<>(); // 실제 객체 생성

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testAddNoticeRequestToQueue_Success() throws InterruptedException {
        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .author("Author")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now().plusDays(1))
                .build();

        //when(queue.offer(notice, 2, TimeUnit.SECONDS)).thenReturn(true);
        doReturn(true).when(queue).offer(notice);

        String result = noticeQueueService.addNoticeRequestToQueue(notice);

        assertEquals("공지사항 등록 요청이 정상적으로 처리되었습니다.", result);
    }

    @Test
    void testAddNoticeRequestToQueue_QueueFull() throws InterruptedException {
        // Given
        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .author("Author")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now().plusDays(1))
                .build();

        // Mocking the behavior of queue offer to simulate a full queue
//        noticeQueueService = spy(new NoticeQueueService(noticeRepository));
        doReturn(false).when(queue).offer(notice);

        // When
        String result = noticeQueueService.addNoticeRequestToQueue(notice);

        // Then
        assertEquals("공지사항 등록 요청 대기열이 가득 찼습니다. 나중에 다시 시도하세요.", result);
    }


    @Test
    void testAddNoticeRequestToQueue_DuplicateRequest() {
        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .author("Author")
                .startDatetime(LocalDateTime.now())
                .endDatetime(LocalDateTime.now().plusDays(1))
                .build();

        String uniqueKey = noticeQueueService.getUniqueKeyForTest(notice);
        noticeQueueService.getRequestTrackingMapForTest().put(uniqueKey, notice);

        String result = noticeQueueService.addNoticeRequestToQueue(notice);

        assertEquals("중복된 요청입니다.", result);
    }
}
