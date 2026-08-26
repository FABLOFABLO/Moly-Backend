package moly.backend.domain.expedition.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
@Table(
        name = "tbl_user_expedition",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_user_expedition_user_expedition",
                columnNames = {"user_id", "expedition_id"}
        )
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserExpedition {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_expedition_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expedition_id", nullable = false)
    private Expedition expedition;

    @Enumerated(EnumType.STRING)
    @Column(name = "expedition_role", nullable = false)
    private ExpeditionRole expeditionRole;

    @Builder
    private UserExpedition(User user, Expedition expedition, ExpeditionRole expeditionRole) {
        this.user = user;
        this.expedition = expedition;
        this.expeditionRole = expeditionRole;
    }
}
