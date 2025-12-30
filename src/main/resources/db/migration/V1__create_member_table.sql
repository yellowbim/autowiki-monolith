CREATE TABLE `member` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT 'PK',
    `user_id` VARCHAR(50) NOT NULL COMMENT '아이디',
    `password` VARCHAR(255) NOT NULL COMMENT '비밀번호',
    `user_name` VARCHAR(50) NOT NULL COMMENT '이름',
    `phone_num` VARCHAR(11) NULL COMMENT '연락처',
    `email` VARCHAR(50) NOT NULL COMMENT '이메일',
    `role` VARCHAR(50) NOT NULL COMMENT '권한',
    `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
    `updated_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
        ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',

    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_user_user_id` (`user_id`)
) COMMENT='회원 테이블';