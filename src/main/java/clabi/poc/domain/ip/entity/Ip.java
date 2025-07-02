package clabi.poc.domain.ip.entity;

import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Entity
@Table(name = "ips")
@Getter
@EntityListeners(AuditingEntityListener.class)
public class Ip {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "ip_address", length = 45, nullable = false)
    private String ipAddress;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDate createdAt;

    protected Ip() {}

    public Ip(String ipAddress, LocalDate createdAt) {
        this.ipAddress = ipAddress;
        this.createdAt = createdAt;
    }
}
