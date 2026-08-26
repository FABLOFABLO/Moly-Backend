package moly.backend.domain.expedition.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_expedition")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Expedition {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "expedition_id", nullable = false)
    private Long id;

    @Column(nullable = false, length = 30)
    private String name;

    @Column(name = "member_count", nullable = false)
    private int memberCount;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Builder
    private Expedition(String name) {
        this.name = name;
        this.memberCount = 1;
    }
}
