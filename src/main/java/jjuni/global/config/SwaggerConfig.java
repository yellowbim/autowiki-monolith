package jjuni.global.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class SwaggerConfig {

    @Value("${swagger.server-url}")
    private String serverUrl;

    private static final String SECURITY_SCHEME_NAME = "authorization";

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(
                        new Components()
                                // JWT 사용s
                                .addSecuritySchemes(SECURITY_SCHEME_NAME, new SecurityScheme()
                                        .name(SECURITY_SCHEME_NAME)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .addSecurityItem(new SecurityRequirement().addList(SECURITY_SCHEME_NAME))
                .addServersItem(new Server().url(serverUrl))
                .info(new Info()
                        .title("AUTO WIKI API")
                        .version("0.0.1")
                        .description(
                                "<b>■ 인증/인가 프로세스</b><br/>" +
                                        "1. 로그인 성공 시 Access Token(1시간), Refresh Token(1일) 발급<br/>" +
                                        "2. Access Token이 만료되면 Refresh Token으로 재발급 요청 가능<br/>" +
                                        "3. Refresh Token도 만료된 경우, 재로그인 필요<br/>" +
                                        "4. 로그아웃 시 Refresh Token은 즉시 삭제<br/><br/>" +
                                        "ps) 다중 로그인 시 access token은 만료시간만 체크하므로 1시간동안은 사용 가능. <br/>그러나 refresh 토큰이 일치하지 않아서 재로그인 필요<br/><br/>" +

                                        "<b>■ Access Token 유효시간</b>: 1시간<br/>" +
                                        "<b>■ Refresh Token 유효시간</b>: 1일<br/><br/>" +

                                        "<b>■ 공통 에러 코드</b><br/><br/>" +

                                        "<b>[1000번대] 일반 오류</b><br/>" +
                                        "- 1000 : UNAUTHORIZED - <b>401 Unauthorized</b> - 인증에 실패하였습니다.<br/>" +
                                        "- 1001 : INVALID_ARGUMENT - <b>400 Bad Request</b> - 잘못된 파라미터입니다.<br/>" +
                                        "- 1002 : ENTITY_NOT_FOUND - <b>400 Bad Request</b> - 존재하지 않는 테이블입니다.<br/>" +
                                        "- 1003 : INTERNAL_ERROR - <b>500 Internal Server Error</b> - 시스템 오류가 발생하였습니다. 다시 시도해주세요.<br/><br/>" +

                                        "<b>[2000번대] 인증 관련</b><br/>" +
                                        "- 2000 : NOT_VALID_MEMBER_INFO - <b>400 Bad Request</b> - 잘못된 아이디, 비밀번호입니다.<br/>" +
                                        "- 2001 : MEMBER_NOT_EXIST - <b>404 Not Found</b> - 존재하지 않는 사용자입니다.<br/><br/>" +

                                        "<b>[3000번대] JWT 오류</b><br/>" +
                                        "- 3000 : JWT_NOT_FIND_TOKEN - <b>401 Unauthorized</b> - 토큰 정보가 누락되어있습니다.<br/>" +
                                        "- 3001 : JWT_TOKEN_PARSING - <b>400 Bad Request</b> - 토큰 파싱 에러가 발생하였습니다.<br/>" +
                                        "- 3002 : JWT_ACCESS_TOKEN_EXPIRED - <b>401 Unauthorized</b> - Access Token이 만료되었습니다.<br/>" +
                                        "- 3003 : JWT_REFRESH_TOKEN_EXPIRED - <b>401 Unauthorized</b> - Refresh Token이 만료되었습니다. 다시 로그인해주세요.<br/>" +
                                        "- 3004 : JWT_UNKNOWN_ERROR - <b>500 Internal Server Error</b> - JWT 에러가 발생하였습니다.<br/>" +
                                        "- 3005 : JWT_INVALID_ACCESS_TOKEN - <b>400 Bad Request</b> - 유효하지 않은 Access Token입니다.<br/>" +
                                        "- 3006 : JWT_INVALID_REFRESH_TOKEN - <b>400 Bad Request</b> - 유효하지 않은 Refresh Token입니다.<br/>" +
                                        "- 3007 : EXIST_BLACK_LIST - <b>401 Unauthorized</b> - 블랙리스트에 등록된 토큰입니다.<br/><br/>" +

                                        "<b>[4000번대] 사용자 오류</b><br/>" +
                                        "- 4000 : NOT_FOUND_USER - <b>404 Not Found</b> - 사용자 정보가 존재하지 않습니다.<br/><br/>" +

                                        "<b>[5000번대] 파라미터 오류</b><br/>" +
                                        "- 5000 : PARAM_NOT_VALID - <b>400 Bad Request</b> - 파라미터 오류입니다.<br/><br/>" +

                                        "<b>[6000번대] 통계 관련</b><br/>" +
                                        "- 6000 : STATS_NOT_FOUND - <b>404 Not Found</b> - 통계 정보가 존재하지 않습니다.<br/>" +
                                        "- 6001 : STATS_ALREADY_EXIST - <b>409 Conflict</b> - 이미 등록된 통계 정보입니다.<br/><br/>" +

                                        "<b>[7000번대] 채팅 메모</b><br/>" +
                                        "- 7000 : CHAT_MEMO_ALREADY_EXIST - <b>409 Conflict</b> - 채팅 메모가 이미 존재합니다.<br/>"
                        )
                );
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("public")
                .pathsToMatch("/**")
                .build();
    }
}
