package clabi.poc.domain.chat.dto.chat.res;

import clabi.poc.domain.chat.dto.chat.res.mapDto.ChatMemoMapDto;
import clabi.poc.domain.chat.dto.chat.res.mapDto.ChatSatisfactionMapDto;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Schema(description = "채팅 + 만족도 응답 DTO")
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record ChatWithSatisfactionDto(
        @NotNull
        @Schema(description = "채팅 고유 ID", example = "1")
        int chatId,

        @Schema(description = "사용자의 IP 주소", example = "192.168.1.1")
        String ipAddress,

        @NotBlank
        @NotNull
        @Schema(description = "채팅에서 질문하는 내용", example = "클라비는 어디에 위치하고 있나요?")
        String chatQuestion,

        @Schema(description = "채팅 질문에 대한 답변", example = "클라비는 서울시 송파구 법원로에 위치하고 있습니다.")
        String chatAnswer,

        @Schema(description = "채팅 시 사용되는 토큰의 수", example = "10")
        Integer useTokenCount,

        @Schema(description = "채팅의 지연 시간 (초 단위)", example = "0.25")
        Float latency,

        @Schema(description = "시나리오 분기 조건", example = "action")
        String action,

        @Schema(description = "시나리오 분기 서브 조건", example = "subAction")
        String subAction,

        @Schema(description = "추천 질문 목록", example = "[\"추천질문1\",\"추천질문2\",\"추천질문3\"]")
        List<String> recommendedQuestions,

        @Schema(description = "참조 정보", example = "[{\"referenceType\":\"type\",\"referenceContent\":\"content\"}]")
        List<Map<String, Object>> references,

        @Schema(description = "형식 정보", example = "[{\"category\": [{\"name\": \"일반행정\", \"values\": 176}, {\"name\": \"기반조성\", \"values\": 105}, {\"name\": \"인력양성\", \"values\": 31}], \"type\": \"check\"}]")
        Map<String, Object> form,

        @Schema(description = "채팅 타입", example = "사업 구분")
        String chatType,

        @Schema(description = "선택 후 응답", example = "클라비는 서울시 송파구 법원로에 위치하고 있습니다.")
        String selectAnswer,

        @Schema(description = "테이블 정보", example = "[{\"imageUrl\":\"http://localhost:8080/image/1\",\"imageType\":\"type\"}]")
        List<Map<String, Object>> table,

        @Schema(description = "그래프 정보", example = "[{\"imageUrl\":\"http://localhost:8080/image/1\",\"imageType\":\"type\"}]")
        List<Map<String, Object>> graph,

        @Schema(description = "이미지 정보", example = "[{\"imageUrl\":\"http://localhost:8080/image/1\",\"imageType\":\"type\"}]")
        List<Map<String, Object>> images,

        @Schema(description = "채팅 히스토리 정보", example = "[{\"key1\":\"value1\",\"key2\":\"value2\",\"key3\":\"value3\"}]")
        List<Map<String, Object>> chatHistoryList,

        @Schema(description = "사용자 선택 정보", example = "[{\"name\": \"기반조성\", \"values\": 78}, {\"name\": \"연구개발\", \"values\": 184}]")
        List<Map<String, Object>> selectValue,

        @Schema(description = "사업 카테고리, 예시질문 정보", example = "{\"category_code\": \"LocalizationSelectAction\", \"category_id\": 3, \"category_name\": \"지역별 사업 예산 현황\", \"example\": \"부산지역 ICT 사업현황을 알려줘\"}")
        Map<String, Object> exampleQuestionInfo,

        @NotNull
        @Schema(description = "연관된 채팅 그룹의 ID", example = "1")
        int chatGroupId,

        @Schema(description = "채팅 생성 날짜 및 시간", example = "2024-10-24T10:15:30")
        LocalDateTime createdAt,

        @Schema(description = "채팅 마지막 업데이트 날짜 및 시간", example = "2024-10-25T11:20:40")
        LocalDateTime updatedAt,

        @Schema(description = "만족도 정보")
        ChatSatisfactionMapDto satisfaction, // 없을 수도 있으니 nullable 허용

        @Schema(description = "메모 정보")
        ChatMemoMapDto memo // 없을 수도 있으니 nullable 허용
) {}
