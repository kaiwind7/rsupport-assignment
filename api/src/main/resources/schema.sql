-- 공지사항 테이블
CREATE TABLE NOTICE (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,      -- 공지사항 ID (자동 증가)
    title VARCHAR(255) NOT NULL,               -- 제목
    content CLOB NOT NULL,                     -- 내용
    start_datetime TIMESTAMP NOT NULL,         -- 공지 시작일시
    end_datetime TIMESTAMP NOT NULL,           -- 공지 종료일시
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일시
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP -- 수정일시
);

-- 첨부파일 테이블
CREATE TABLE NOTICE_ATTACHMENT (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,      -- 첨부파일 ID (자동 증가)
    notice_id BIGINT NOT NULL,                 -- 공지사항 ID (외래키)
    file_name VARCHAR(255) NOT NULL,           -- 파일명
    file_path VARCHAR(512) NOT NULL,           -- 파일 경로
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, -- 생성일시
    CONSTRAINT fk_notice FOREIGN KEY (notice_id) REFERENCES NOTICE(id) ON DELETE CASCADE
);