# code-convention.md

## Introduction
- 해당 md는 프로젝트의 전반적인 코드 베이스 컨벤션 입니다.
- 최대한 md의 스타일을 유지하는것을 지향합니다.
- feature, url 등은 예시입니다.
  - 실제 기능에 맞게 컬럼명, 속성등을 정의하세요.

### Entity
> 어노테이션 중 권장 어노테이션 이외에는 필요에 따라 구성
- 어노테이션 순서
`````
[클래스 범위]

1. JPA 핵심 선언
@Entity // 권장

2. DB 매핑 및 제약조건
@Table(name = "tbl_feature") // 권장
@Inheritance(...)
@DiscriminatorColumn(...)

3. Hibernate/JPA 동작 설정
@DynamicInsert
@DynamicUpdate

4. 직렬화 등 외부 프레임워크
@JsonIgnoreProperties(...)

5. Lombok
@Getter // 권장
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 권장
`````
`````
[컬럼]

1. id 컬럼
@Id
@GeneratedValue(strategy = GenerationType.IDENTITY)
@Column(name = "feature_id")
private Long id;

2. 일반 컬럼
@Column(name = "column", nullable = false, length = 100)
private type colum;

3. Enum
@Enumerated(EnumType.STRING)
@Column(name = "enum", nullable = false)
private Enum enum;

4. 연관관계
@ManyToOne(fetch = FetchType.LAZY)
@JoinColumn(name = "entity_id", nullable = false)
private Entity entity;

@OneToMany(mappedBy = "entity1", cascade = CascadeType.ALL, orphanRemoval = true)
private List<Entity2> entities2 = new ArrayList<>();
`````
- 생성자
`````
@Builder
private Entity (type column) {
    this.column = column;
}
`````

### Controller
- 어노테이션 순서
`````
[클래스 범위]
1. Spring MVC 컨트롤러 선언
@RestController // 권장

2. 공통 URL 매핑
@RequestMapping("/url-name") // 권장

3. API 문서화
@Tag(name = "Feature", description = "기능 API")

4. 보안
@PreAuthorize(...)

5. Lombok
@RequiredArgsConstructor // 권장
public class FeatureController { }
`````
`````
[메서드 범위]
@HttpMethod("feture-id") // 권장
    @ResponseStatus(HttpStatus.STATUS) // 권장
    @Operation(summary = "기능 생성")
    public void createFeature(
            @RequestBody @Valid FeatureRequest request // request DTO 바로 넘기는 패턴 통일
    ) { }
`````

### Service
- 어노테이션 구성
`````
@Service // 권장
@RequiredArgsConstructor // 권장
public class FeatureService {
    private final FeatureRepository featureRepository; // 권장

    @Transactional // 권장
    public type execute() { } // 메서드명 통일
}

// 1. Spring 서비스 선언
@Service

// 2. Lombok 생성자 주입
@RequiredArgsConstructor

// 3. 트랜잭션 기본 정책
@Transactional(readOnly = true) // 권장
public class FeatureService {
  private final FeatureRepository featureRepository; // 권장

    @Transactional // 권장(Dirty Checking)
    public type execute() { } // 메서드명 통일
}
`````

### DTO
- Request
`````
public record FeatureCreateRequest(...) {
}
`````
> Controller에 맞게 validation이 필요한 부분 최대한 적용
> - Not-Blank,Null,Empty
> - Size(지향),Min,Max(필요시)
>   - pattern, Email 등 필요시
- Response
`````
public record FeatureResponse(...) {

    public static FeatureResponse from(Feature feature) {
        return new FeatureResponse(...);
    }
}
`````