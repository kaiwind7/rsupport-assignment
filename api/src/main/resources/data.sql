-- 공지사항 데이터 삽입
INSERT INTO NOTICE (title, content, author, views, start_datetime, end_datetime, created_At)
VALUES ('공지사항 1', '첫 번째 공지사항입니다.', 'John Doe', 10, '2024-10-01 08:00:00', '2024-10-10 18:00:00', '2024-10-01 08:00:00'),
       ('공지사항 2', '두 번째 공지사항입니다.', 'Jane Doe', 5, '2024-10-05 09:00:00', '2024-10-20 18:00:00', '2024-10-01 08:00:00'),
       ('공지사항 3', '세 번째 공지사항입니다.', 'Alice Smith', 15, '2024-10-10 08:00:00', '2024-10-15 18:00:00',
        '2024-10-01 08:00:00'),
       ('공지사항 4', '네 번째 공지사항입니다.', 'Bob Johnson', 20, '2024-10-01 00:00:00', '2024-12-31 23:59:59',
        '2024-10-01 08:00:00'),
       ('공지사항 5', '다섯 번째 공지사항입니다.', 'Charlie Brown', 0, '2024-10-15 10:00:00', '2024-11-15 17:00:00',
        '2024-10-01 08:00:00');


-- 첨부파일 데이터 삽입
-- INSERT INTO NOTICE_ATTACHMENT (id, notice_id, file_name, file_path, created_at)
-- VALUES (1, 1, 'attachment1.pdf', '/files/attachment1.pdf', CURRENT_TIMESTAMP);
--
-- INSERT INTO NOTICE_ATTACHMENT (id, notice_id, file_name, file_path, created_at)
-- VALUES (2, 1, 'attachment2.jpg', '/files/attachment2.jpg', CURRENT_TIMESTAMP);
--
-- INSERT INTO NOTICE_ATTACHMENT (id, notice_id, file_name, file_path, created_at)
-- VALUES (3, 2, 'attachment3.docx', '/files/attachment3.docx', CURRENT_TIMESTAMP);
--
-- INSERT INTO NOTICE_ATTACHMENT (id, notice_id, file_name, file_path, created_at)
-- VALUES (4, 3, 'attachment4.png', '/files/attachment4.png', CURRENT_TIMESTAMP);
--
-- INSERT INTO NOTICE_ATTACHMENT (id, notice_id, file_name, file_path, created_at)
-- VALUES (5, 3, 'attachment5.zip', '/files/attachment5.zip', CURRENT_TIMESTAMP);