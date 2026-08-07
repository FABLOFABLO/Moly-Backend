# git-convention.md

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
