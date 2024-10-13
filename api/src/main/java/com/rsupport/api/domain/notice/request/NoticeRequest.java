package com.rsupport.api.domain.notice.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class NoticeRequest {
    private Long id;
    private String title;
    private String content;
    private LocalDateTime startDatetime;
    private LocalDateTime endDatetime;
    private String author;
    private List<Long> deleteFiles;
}
