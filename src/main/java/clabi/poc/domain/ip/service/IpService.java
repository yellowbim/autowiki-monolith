package clabi.poc.domain.ip.service;

import clabi.poc.domain.ip.entity.Ip;
import clabi.poc.domain.ip.repository.IpRepository;
import clabi.poc.domain.stats.entity.StatsDaily;
import clabi.poc.domain.stats.repository.StatsDailyRepository;
import clabi.poc.global.config.Xff;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDate;

@Service
@Transactional(readOnly = true)
public class IpService {
    private final IpRepository ipRepository;
    private final StatsDailyRepository statsDailyRepository;
    private final Xff xff;

    public IpService(IpRepository ipRepository, StatsDailyRepository statsDailyRepository, Xff xff) {
        this.ipRepository = ipRepository;
        this.statsDailyRepository = statsDailyRepository;
        this.xff = xff;
    }

    @Transactional
    public boolean saveIpIfNotExists(HttpServletRequest request) {
        LocalDate today = LocalDate.now();
        String clientIp = xff.getClientIp(request);

        if ("0:0:0:0:0:0:0:1".equals(clientIp) || "::1".equals(clientIp)) {
            clientIp = "127.0.0.1";
        }

        if (clientIp == null || ipRepository.existsByIpAddressAndCreatedAt(clientIp, today)) {
            return false;
        }

        if (ipRepository.existsByIpAddressAndCreatedAt(clientIp, today)) {
            return false;
        }

        Ip ip = new Ip(clientIp, today);
        ipRepository.save(ip);

        StatsDaily statsDaily = statsDailyRepository.findByCreatedAt(today)
                .orElseGet(() -> new StatsDaily(0, 0, 0, today));

        statsDaily.incrementVisitCount();
        statsDailyRepository.save(statsDaily);

        return true;
    }
}
