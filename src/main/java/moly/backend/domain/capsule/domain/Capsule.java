package moly.backend.domain.capsule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moly.backend.domain.capsule.exception.CapsuleAlreadyOpenedException;
import moly.backend.domain.expedition.domain.Expedition;
import moly.backend.domain.user.domain.User;
import org.hibernate.annotations.CreationTimestamp;
import org.locationtech.jts.geom.Point;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_capsule")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Capsule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id")
    private Expedition expedition;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 1000)
    private String content;

    @Column(nullable = false, columnDefinition = "geometry(Point,4326)")
    private Point location;

    @Column(name = "required_count", nullable = false)
    private int requiredCount;

    @Column(name = "required_time", nullable = false)
    private LocalDateTime requiredTime;

    @Column(name = "opened_time")
    private LocalDateTime openedTime;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 255)
    private String address;

    @Column(name = "road_address", length = 255)
    private String roadAddress;

    @Builder
    private Capsule(User user, Expedition expedition, String title, String content, Point location,
                    int requiredCount, LocalDateTime requiredTime, String address, String roadAddress) {
        this.user = user;
        this.expedition = expedition;
        this.title = title;
        this.content = content;
        this.location = location;
        this.requiredCount = requiredCount;
        this.requiredTime = requiredTime;
        this.address = address;
        this.roadAddress = roadAddress;
    }

    public void open(LocalDateTime openedTime) {
        if (this.openedTime != null) {
            throw new CapsuleAlreadyOpenedException();
        }
        this.openedTime = openedTime;
    }
}
