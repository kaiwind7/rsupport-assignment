package com.rsupport.api.notice.controller;

import com.rsupport.api.common.response.ApiResponse;
import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.notice.service.NoticeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping("/{noticeId}")
    public ApiResponse<NoticeDTO> getNotice(@PathVariable Long noticeId) {
        return ApiResponse.success(noticeService.findNotice(noticeId));
    }

    @PostMapping("")
    public ApiResponse<NoticeDTO> createNotice(@RequestBody NoticeDTO noticeDTO) {
        return ApiResponse.success(noticeService.saveNotice(noticeDTO));
    }

    @PutMapping("/{noticeId}")
    public ApiResponse<NoticeDTO> updateNotice(@PathVariable Long noticeId, @RequestBody NoticeDTO noticeDTO) {
        return ApiResponse.success(noticeService.saveNotice(noticeDTO));
    }

    @DeleteMapping("/{noticeId}")
    public ApiResponse<NoticeDTO> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ApiResponse.success();
    }

}
