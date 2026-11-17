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

@Entity
@Table(name = "tbl_capsule_image")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CapsuleImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "capsule_id", nullable = false)
    private Capsule capsule;

    @Column(name = "image_key", nullable = false, length = 512)
    private String imageKey;

    @Builder
    private CapsuleImage(Capsule capsule, String imageKey) {
        this.capsule = capsule;
        this.imageKey = imageKey;
    }
}
