package clabi.poc.domain.auth.dto;

import clabi.poc.domain.auth.enums.RoleType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

/**
 * 로그인 요청 Dto
 */
@Builder
public record RefreshTokenResponse(
        @Schema(description = "access token", example = "yJhbGciOiJIUzI1NiJ9.eyJpZCI6MSwidXNlcklkIjoiYWlfdXNlcl8xIiwicm9sZSI6IlVTRVIiLCJpYXQiOjE3NDc4MTM4MTcsImV4cCI6MTc0NzgxNzQxN30.4_dxMQorFKtXkpghqH-z2-9WeNRRZC3LxFw0CXnDjEI")
        String accessToken
) {}
