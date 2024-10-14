package com.rsupport.api.notice.service;

import com.rsupport.api.common.enums.ErrorCode;
import com.rsupport.api.common.exception.ServiceException;
import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.entity.NoticeAttachment;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import com.rsupport.api.domain.notice.request.NoticeRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class NoticeServiceUnitTest {

    @InjectMocks
    private NoticeService noticeService;

    @Spy
    private NoticeRepository noticeRepository;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testFindNoticeAll() {
        Pageable pageable = PageRequest.of(0, 10);
        List<Notice> notices = new ArrayList<>();
        notices.add(Notice.builder()
                .title("Test Notice")
                .attachments(new ArrayList<>()).build());
        Page<Notice> noticePage = new PageImpl<>(notices);

        when(noticeRepository.findActiveNotices(any(LocalDateTime.class), eq(pageable))).thenReturn(noticePage);

        Page<NoticeDTO> result = noticeService.findNoticeAll(pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Test Notice", result.getContent().get(0).getTitle());
    }

    @Test
    void testFindNotice() {
        Notice notice = Notice.builder()
                .title("Test Notice")
                .content("Test Content")
                .author("Author")
                .attachments(new ArrayList<>()).build();
        when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(notice));

        NoticeDTO result = noticeService.findNotice(1L);

        assertEquals("Test Notice", result.getTitle());
        assertEquals("Test Content", result.getContent());
        assertEquals("Author", result.getAuthor());
    }

    @Test
    void testFindNotice_NotFound() {
        when(noticeRepository.findById(anyLong())).thenReturn(Optional.empty());

        ServiceException exception = assertThrows(ServiceException.class, () -> noticeService.findNotice(1L));
        assertEquals(ErrorCode.NOT_FOUND_ENTITY, exception.getErrorCode());
    }

    @Test
    void testDeleteNotice_FileDeletion() throws IOException {
        NoticeAttachment attachment = NoticeAttachment.builder()
                .fileName("testFile.txt")
                .filePath(Files.createTempFile(UUID.randomUUID().toString(), "testFile.txt").toString())
                .build();
        Notice notice = Notice.builder().title("Test Notice").attachments(new ArrayList<>(List.of(attachment))).build();

        when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(notice));

        noticeService.deleteNotice(1L);

        verify(noticeRepository, times(1)).delete(notice);
    }

    @Test
    void testUpdateNotice() {
        Notice notice = Notice.builder()
                .title("Old Title")
                .attachments(new ArrayList<>()).build();
        when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(notice));

        NoticeRequest request = NoticeRequest.builder()
                .title("Updated Title")
                .content("Updated Content").build();

        NoticeDTO result = noticeService.updateNotice(1L, request, new ArrayList<>());

        assertEquals(request.getTitle(), result.getTitle());
        assertEquals(request.getContent(), result.getContent());
    }

    @Test
    void testIncrementViews() {
        Notice notice = Notice.builder().title("Test Notice").views(0).build();
        when(noticeRepository.findById(anyLong())).thenReturn(Optional.of(notice));

        noticeService.incrementViews(1L);

        verify(noticeRepository, times(1)).incrementViews(1L);
    }

}
