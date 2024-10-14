package com.rsupport.api.notice.service;

import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.entity.Notice;
import com.rsupport.api.domain.notice.repository.NoticeRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class NoticeQueueService {
    private LinkedBlockingQueue<Notice> queue = new LinkedBlockingQueue<>();
    private final Map<String, Notice> requestTrackingMap = new ConcurrentHashMap<>(); // 중복 체크용 Map

    private ExecutorService executorService;

    private final NoticeRepository noticeRepository;
    private boolean keepProcessing = true;

    @PostConstruct
    private void init() {
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(this::processQueue);
    }

    /**
     * 등록된 큐 진행
     */
    private void processQueue() {
        while (keepProcessing && !Thread.currentThread().isInterrupted()) {
            Notice notice = null;

            try {
                notice = queue.take();
                saveNotice(notice);
                removeFromTrackingMap(notice);
            } catch (InterruptedException e) {
                log.warn("공지사항 등록 스레드가 인터럽트되었습니다.", e);
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                log.error("공지사항 등록 중 오류가 발생했습니다.", e);
            }
        }
    }

    public String addNoticeRequestToQueue(Notice notice) {
        String uniqueKey = generateUniqueKey(notice);
        try {
            if (requestTrackingMap.putIfAbsent(uniqueKey, notice) == null) {
                if (!queue.offer(notice, 2, TimeUnit.SECONDS)) {
                    String message = "공지사항 등록 요청 대기열이 가득 찼습니다. 나중에 다시 시도하세요.";
                    log.warn(message);
                    return message;
                }
            } else {
                log.info("중복된 요청입니다.: {}", notice);
                return "중복된 요청입니다.";
            }

            log.info("공지사항 등록 요청이 큐에 추가되었습니다: {}", notice);
            return "공지사항 등록 요청이 정상적으로 처리되었습니다.";
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            String message = "큐에 추가하는 동안 인터럽트가 발생했습니다.";
            log.error(message, e);
            return message;
        }
    }

    private String generateUniqueKey(Notice notice) {
        return notice.getTitle() + "-" + notice.getStartDatetime() + "-" + notice.getEndDatetime() + "-" + notice.getAuthor();
    }

    private void removeFromTrackingMap(Notice notice) {
        String uniqueKey = generateUniqueKey(notice);
        requestTrackingMap.remove(uniqueKey);
    }

    /**
     * 공지사항 저장
     *
     * @param notice 공지사항 저장 정보
     * @return 공지사항 저장정보
     */
    @Async("taskExecutor")
    @Transactional
    @CacheEvict(value = "notices", allEntries = true)
    public NoticeDTO saveNotice(Notice notice) {
        Notice resultNotice = noticeRepository.save(notice);
        return NoticeDTO.fromEntity(resultNotice);
    }

    /**
     * 루프 종료 제어 메서드
     */
    public void stopProcessing() {
        this.keepProcessing = false;
    }

    /**
     * 테스트를 위한 메서드
     *
     * @param notice
     * @return
     */
    public String getUniqueKeyForTest(Notice notice) {
        return generateUniqueKey(notice);
    }

    /**
     * 테스트를 위한 메서드
     *
     * @return
     */
    public Map<String, Notice> getRequestTrackingMapForTest() {
        return requestTrackingMap;
    }

    public void processQueueForTest() {
        processQueue();
    }

}
