package jjuni.domain.auth.dto;


import jjuni.domain.auth.enums.RoleType;
import jjuni.domain.member.entity.Member;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record SignUpRequest(

        @Size(min = 2, max = 50)
        @Schema(description = "사용자ID", example = "leejj9999")
        @NotBlank(message = "아이디를 입력해주세요")
        String userId,

        @Size(min = 9, max = 30)
        @Schema(description = "비밀번호", example = "test1234!@#$")
        @NotBlank(message = "비밀번호를 입력해주세요")
        String password,

        @Size(min = 2, max = 30)
        @Schema(description = "이름", example = "테스트")
        @NotBlank(message = "이름을 입력해주세요")
        String name,

        @Size(min = 13, max = 14)
        @Schema(description = "연락처", example = "010-1234-1234")
        @NotBlank(message = "연락처를 입력해주세요")
        String phoneNum,

        @Size(min = 11, max = 20)
        @Schema(description = "이메일", example = "test9999@naver.com")
        @NotBlank(message = "이메일을 입력해주세요")
        String email
) {
    public Member toEntity() {
        return Member.builder()
                .userId(userId)
                .password(password)
                .userName(name)
                .phoneNum(phoneNum)
                .email(email)
                .role(RoleType.USER)
                .build();
    }
}
