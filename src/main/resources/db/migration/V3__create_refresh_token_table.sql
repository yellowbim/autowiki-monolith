CREATE TABLE refresh_token (
   id BIGINT NOT NULL AUTO_INCREMENT COMMENT 'Refresh token seq',
   member_id BIGINT NOT NULL COMMENT '회원 ID',
   refresh_token TEXT NOT NULL COMMENT 'Refresh Token 값',
   created_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '생성일',
   updated_at DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
       ON UPDATE CURRENT_TIMESTAMP COMMENT '수정일',

   PRIMARY KEY (id),
   UNIQUE KEY uk_refresh_token_member (member_id),

   CONSTRAINT fk_refresh_token_member
       FOREIGN KEY (member_id)
           REFERENCES member (id)
           ON DELETE CASCADE
) COMMENT='사용자별 Refresh Token 테이블';
