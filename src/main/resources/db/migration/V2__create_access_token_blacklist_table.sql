CREATE TABLE `access_token_blacklist` (
      `access_token` VARCHAR(512) NOT NULL COMMENT 'JWT Access Token (PK)',
      `expired_at` DATETIME NOT NULL COMMENT '토큰 만료 시각',
      `created_at` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '등록 시각',

      PRIMARY KEY (`access_token`)
) COMMENT='Access Token 블랙리스트';
