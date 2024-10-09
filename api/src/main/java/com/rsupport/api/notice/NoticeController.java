package com.rsupport.api.notice;

import com.rsupport.api.common.response.ApiResponse;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class NoticeController {
    /**
     * ● 공지사항 등록, 수정, 삭제, 조회 API를 구현한다.
     *
     * ● 공지사항 등록시 입력 항목은 다음과 같다.
     *
     * \- 제목, 내용, 공지 시작일시, 공지 종료일시, 첨부파일 (여러개)
     *
     * ● 공지사항 조회시 응답은 다음과 같다.
     *
     * \- 제목, 내용, 등록일시, 조회수, 작성자
     */

    /**
     * 공지사항 등록, 수저으 삭제, 조회
     */

    @GetMapping("/{id}")
    public ApiResponse getNotice(@PathVariable Long id) {
        return ApiResponse.success();
    }
}
