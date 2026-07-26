# AGENT.md

## Package Structure
- DDD-lite 구조
> 의미있는 단위의 패키지&클래스
- root
  - src
    - main
      - java
        - moly
          - backend
            - domain
              - feature
                - domain
                  - Entity(Class)
                  - repository
                    - Repository(interface)
                - presentation
                  - Controller(class)
                  - dto
                    - response
                      - FeatureResponse(class)
                    - request
                      - FeatureRequest(class)
                - exception
                  - NameException
                - service
                  - FeatureService
            - global

## Code Convention
### Entity
> 어노테이션 중 권장 어노테이션 이외에는 필요에 따라 구성
- 어노테이션 순서
`````
[클래스 범위]

1. JPA 핵심 선언
@Entity // 권장

2. DB 매핑 및 제약조건
@Table(name = "feature_tbl") // 권장
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
@RequestMapping("/url") // 권장

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
            @RequestBody @Valid FeatureRequest request
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
- Response
`````
public record FeatureResponse(...) {

    public static FeatureResponse from(Feature feature) {
        return new FeatureResponse(...);
    }
}
`````

# git

## Rules
- default branch는 develop이다.
- 기능 작업은 항상 최신 develop branch 기준으로 새 브랜치를 파생하여 작업한다.

### branch Naming
| Type       | Meaning                   | branch example             |
| ---------- |---------------------------| --------------------------------- |
| `feature`  | 새로운 API 또는 기능 추가          | `feature/item-submission-api`     |
| `fix`      | 버그 또는 오류 수정               | `fix/item-submission-status-error` |
| `refactor` | 기능 변화 없는 구조 개선            | `refactor/storage-service`        |
| `chore`    | 설정, 빌드, 의존성, 인프라등 개발환경 작업 | `chore/swagger-config`            |
| `docs`     | 문서 작성·수정              | `docs/update-agents-guide`        |
| `style`    | 코드 포맷팅 및 스타일 정리           | `style/format-feature-domain`     |

### commit
- 너무 작은 수정마다 커밋하지 않고, 의미 있는 작업 단위로 커밋한다.
- 기능 1차 구현, 버그 수정, 설정 변경, 문서 수정은 가능하면 분리한다.
- 여러 작업이 섞이면 커밋을 나누는 것을 권장한다.
- 빌드가 온전하지 않은 상태의 커밋은 지양한다.

### Commit Message Convention
기본 형식은 `type: short description`입니다.

```text
type: 변경 내용을 요약한 설명
```

- 설명은 한글 작성을 기본으로 합니다.
- 필요한 기술명, 클래스명, API명 등만 영어로 작성합니다.
- 제목만 보고 변경 내용을 파악할 수 있도록 작성합니다.
- 제목은 지나치게 길게 작성하지 않습니다.
- 제목 끝에는 마침표를 붙이지 않습니다.

| Type       | When To Use                | Example                          |
| ---------- | -------------------------- | -------------------------------- |
| `feat`     | 새로운 기능 또는 API 추가           | `feat: 아이템 제출 API 추가`            |
| `fix`      | 버그 또는 동작 오류 수정             | `fix: 아이템 제출 Swagger 인증 오류 수정`   |
| `refactor` | 기능 변화 없이 코드 구조 개선          | `refactor: 파일 저장 Service 분리`     |
| `chore`    | 빌드, 설정, 의존성, 인프라 등 개발환경 변경 | `chore: Swagger 설정 추가`           |
| `docs`     | 문서 작성 또는 수정                | `docs: Git 컨벤션 추가`               |
| `style`    | 포맷팅, 들여쓰기 등 로직 변화 없는 코드 수정 | `style: ItemController 코드 포맷 정리` |
| `rename`   | 파일, 클래스 또는 패키지 이름 변경       | `rename: ItemRequest DTO 이름 변경`  |
| `remove`   | 사용하지 않는 코드 또는 파일 삭제        | `remove: 사용하지 않는 Mock 데이터 삭제`    |


### push
- `push`는 기능단위로 할 수 있도록 권장한다.
  - 필요한 경우엔 어느정도 commit이 안정화 되어있다면 push한다.
- `develop` 병합 전에는 작업 브랜치를 원격에 push한다.
- 빌드 실패 상태를 원격에 올리는 것은 가능하면 지양한다.
