package clabi.poc.domain.ip.repository;

import clabi.poc.domain.ip.entity.Ip;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;

public interface IpRepository extends JpaRepository<Ip, Integer> {
    boolean existsByIpAddressAndCreatedAt(String ipAddress, LocalDate createdAt);
}
