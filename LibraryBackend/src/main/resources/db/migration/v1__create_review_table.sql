CREATE TABLE review
(
    id         BIGINT AUTO_INCREMENT PRIMARY KEY,
    user_id    BIGINT   NOT NULL,
    book_id    BIGINT   NOT NULL,
    rating     INT      NOT NULL,
    comment    TEXT,
    created_at DATETIME NOT NULL,
    CONSTRAINT fk_user FOREIGN KEY (user_id) REFERENCES user (id),
    CONSTRAINT fk_book FOREIGN KEY (book_id) REFERENCES Books (id)
);
