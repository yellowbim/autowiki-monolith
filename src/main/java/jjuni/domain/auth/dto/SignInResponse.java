package jjuni.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jjuni.domain.auth.enums.RoleType;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 로그인 응답 Dto
 */
@Data
@AllArgsConstructor
public class SignInResponse {

    @Schema(description = "회원 유형", example = "USER")
    private RoleType RoleType;

    private String accessToken;

    private String refreshToken;
}
