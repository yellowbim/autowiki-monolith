package jjuni.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

/**
 * 로그인 요청 Dto
 */
public record SignInRequest(
        @Schema(description = "사용자ID", example = "leedh")
        @NotBlank(message = "아이디를 입력해주세요")
        String userId,

        @Schema(description = "비밀번호", example = "leedh1234")
        @NotBlank(message = "비밀번호를 입력해주세요") String password
) {}
