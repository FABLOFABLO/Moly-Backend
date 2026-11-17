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
import jakarta.persistence.UniqueConstraint;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import moly.backend.domain.user.domain.User;

@Entity
@Table(name = "tbl_capsule_member", uniqueConstraints = {
        @UniqueConstraint(name = "uk_capsule_member_user_capsule", columnNames = {"user_id", "capsule_id"})
})
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CapsuleMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "capsule_member_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false)
    private Capsule capsule;

    @Builder
    private CapsuleMember(User user, Capsule capsule) {
        this.user = user;
        this.capsule = capsule;
    }
}
