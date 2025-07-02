package clabi.poc.domain.ip.controller;

import clabi.poc.domain.ip.service.IpService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "ip", description = "ip 저장")
@RestController
@RequestMapping("/api/v1/ip")
public class IpController {
    private final IpService ipService;

    public IpController(IpService ipService) {
        this.ipService = ipService;
    }

    @PostMapping
    @Operation(summary = "Create IP", description = "Client 의 요청 IP 정보를 저장하는 API 입니다.<br/>IP를 생성하거나 이미 존재하는 경우 아무 작업도 수행하지 않습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "IP 생성 성공"),
            @ApiResponse(responseCode = "200", description = "IP가 이미 존재하여 저장이 필요하지 않음"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청, IP 주소 형식 또는 요청 데이터가 유효하지 않음")
    })
    public ResponseEntity<Void> createIp(HttpServletRequest request) {
        boolean isNewIpCreated = ipService.saveIpIfNotExists(request);

        if (isNewIpCreated) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } else {
            return ResponseEntity.ok().build();
        }
    }
}
