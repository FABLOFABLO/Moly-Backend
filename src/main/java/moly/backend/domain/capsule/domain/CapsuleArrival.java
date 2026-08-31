package moly.backend.domain.capsule.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moly.backend.domain.capsule.exception.CapsuleInvalidArrivalStatusException;
import java.time.LocalDateTime;

@Entity
@Table(name = "tbl_capsule_arrival", uniqueConstraints = {
        @UniqueConstraint(name = "uk_capsule_arrival_member", columnNames = "capsule_member_id")
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CapsuleArrival {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_arrival_id", nullable = false)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_member_id", nullable = false)
    private CapsuleMember capsuleMember;

    @Enumerated(EnumType.STRING)
    @Column(name = "arrival_status", nullable = false)
    private ArrivalStatus arrivalStatus;

    @Column(name = "arrived_at")
    private LocalDateTime arrivedAt;

    @Column(name = "departed_at")
    private LocalDateTime departedAt;

    @Builder
    private CapsuleArrival(CapsuleMember capsuleMember) {
        this.capsuleMember = capsuleMember;
        this.arrivalStatus = ArrivalStatus.NOT_ARRIVED;
    }

    public void arrive(LocalDateTime arrivedAt) {
        if (arrivalStatus == ArrivalStatus.ARRIVED) {
            throw new CapsuleInvalidArrivalStatusException();
        }
        this.arrivalStatus = ArrivalStatus.ARRIVED;
        this.arrivedAt = arrivedAt;
        this.departedAt = null;
    }

    public void depart(LocalDateTime departedAt) {
        if (arrivalStatus != ArrivalStatus.ARRIVED) {
            throw new CapsuleInvalidArrivalStatusException();
        }
        this.arrivalStatus = ArrivalStatus.DEPARTED;
        this.departedAt = departedAt;
    }
}
