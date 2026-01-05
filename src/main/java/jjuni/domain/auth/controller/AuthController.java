package jjuni.domain.auth.controller;

import jjuni.domain.auth.dto.*;
import jjuni.domain.auth.service.AuthService;
import jjuni.domain.common.dto.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@Tag(name = "Auth", description = "로그인, Access token재발급 등 인증처리 API 입니다.")
@RestController
@RequestMapping("/api/v1/auth")
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;


    // 사용자 회원가입
    @Operation(summary = "회원가입", description = "신규 사용자 회원가입")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Create Success")
    })
    @ResponseBody
    @PostMapping("sign-up")
    public ApiResponse<SignUpResponse> signUp(@Validated @RequestBody SignUpRequest request) throws Exception {
        SignUpResponse signUpResponse = authService.signUp(request);
        return ApiResponse.success(signUpResponse);
    }

    // 사용자 로그인
    @Operation(summary = "로그인", description = "사용자 로그인 (jwt 토큰 발급)")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Login Success")
    })
    @ResponseBody
    @PostMapping("sign-in")
    public ApiResponse<SignInResponse> signIn(@Validated @RequestBody SignInRequest request) {
        SignInResponse signInResponse = authService.signIn(request);
        return ApiResponse.success(signInResponse);
    }

    // 사용자 로그아웃
    @Operation(summary = "로그아웃", description = "사용자 로그아웃 (refresh 토큰 정보 삭제)<br/>블랙리스트 기능은 추가하지 않았기 때문에 Access Token이 만료되기전까지는 사용 가능합니다.")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "User Login Success")
    })
    @PostMapping("sign-out")
    public ResponseEntity signOut(HttpServletRequest request) {
        authService.signOut(request);
        return ResponseEntity.noContent().build();
    }

    /**
     * Access Token 재발급 요청
     * - Header 에 있는 Refresh Token 으로 Access Token 재발급
     */
    @Operation(summary = "Access Token 재발급", description = "Access Token 만료로 인한 재발급")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Access Token")
    })
    @ResponseBody
    @PostMapping("reissue-access-token")
    public ApiResponse<RefreshTokenResponse> reIssueAccessToken(HttpServletRequest request, @RequestHeader("Refresh-Token") String refreshToken) throws Exception {
        RefreshTokenResponse newAccesToken = authService.reIssueAccessToken(request, refreshToken);
        return ApiResponse.success(newAccesToken);
    }
}
