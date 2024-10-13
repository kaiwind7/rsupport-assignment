package com.rsupport.api.notice.controller;

import com.rsupport.api.common.response.ApiResponse;
import com.rsupport.api.domain.notice.dto.NoticeDTO;
import com.rsupport.api.domain.notice.request.NoticeRequest;
import com.rsupport.api.notice.service.NoticeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notices")
@Slf4j
public class NoticeController {
    private final NoticeService noticeService;

    @GetMapping
    @Operation(summary = "공지사항 목록 조회", description = "공지사항 목록을 페이징해서 조회한다.")
    public ApiResponse<Page<NoticeDTO>> getNoticeList(@Parameter(description = "페이지", example = "0") @RequestParam(defaultValue = "0") int page,
                                                      @Parameter(description = "사이즈", example = "30") @RequestParam(defaultValue = "30") int size) {
        return ApiResponse.success(noticeService.findNoticeAll(PageRequest.of(page, size)));
    }

    @GetMapping("/{noticeId}")
    @Operation(summary = "공지사항 조회", description = "해당 ID 공지사항을 조회한다.")
    public ApiResponse<NoticeDTO> getNotice(@PathVariable Long noticeId) {
        return ApiResponse.success(noticeService.findNotice(noticeId));
    }

    @PostMapping(value = "", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "공지사항 정보 저장", description = "공지사항 정보를 저장한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "저장 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "파일 업로드를 실패했습니다"),
    })
    public ApiResponse<NoticeDTO> createNotice(@RequestPart(value = "request") NoticeRequest request,
                                               @RequestPart(value = "files") List<MultipartFile> files) {
        NoticeDTO noticeDTO = noticeService.saveNotice(request, files);
        return ApiResponse.success(noticeDTO);
    }

    @PutMapping(value = "/{noticeId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(summary = "공지사항 정보 수정", description = "공지사항 정보를 수정한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "수정 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 공지가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "삭제할 첨부파일이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "파일 삭제 중 오류가 발생했습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "파일 업로드를 실패했습니다."),
    })
    public ApiResponse<NoticeDTO> updateNotice(@PathVariable Long noticeId,
                                               @RequestPart(value = "request") NoticeRequest request,
                                               @RequestPart(value = "files") List<MultipartFile> files) {
        return ApiResponse.success(noticeService.updateNotice(noticeId, request, files));
    }

    @DeleteMapping("/{noticeId}")
    @Operation(summary = "공지사항 정보 삭제", description = "공지사항 정보를 삭제한다.", responses = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "삭제 성공"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "해당 공지가 존재하지 않습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "삭제할 첨부파일이 없습니다."),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "500", description = "파일 삭제 중 오류가 발생했습니다."),
    })
    public ApiResponse<NoticeDTO> deleteNotice(@PathVariable Long noticeId) {
        noticeService.deleteNotice(noticeId);
        return ApiResponse.success();
    }

}
