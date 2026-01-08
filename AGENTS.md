# AGENTS.MD

## 프로젝트 목적
- 이 프로젝트는 Java 기반 Spring Boot 애플리케이션입니다.
- 목표는 기존 코드를 Kotlin으로 점진적으로 변환하는 것입니다.
- Kotlin 전환이 완료된 이후에만 신규 기능을 추가합니다.
- 빅뱅 방식으로 Kotlin 변환은 지양합니다. 작은 단위로 변환 해주세요.

## 기술 스택
- Java 21 (기존)
- Kotlin (전환 대상)
- Spring Boot
- JPA (Hibernate)
- Redis (Docker, local)
- MySQL (Docker, local)

## 코딩 규칙
- Lombok은 사용하지 않습니다.
- val을 우선 사용합니다.
- 패키지 구조는 변경하지 않습니다.
- Kotlin에서는 data class, nullable type 등 idiomatic Kotlin을 우선합니다.

## 전환 규칙 (Java → Kotlin)
- 클래스 단위로 전환합니다.
- 클래스 변환 순서는 의존성이 낮은 것부터 진행합니다. 예시) DTO,Enum -> Entity -> Repository -> Service -> Controller 입니다. 이 기준은 CODEX의 생각에 따라 변경할 수 있습니다.
- 비즈니스 로직은 절대 변경하지 않습니다.
- public API 시그니처는 유지합니다.
- Java Optional은 nullable type으로 변환합니다.
- 생성자는 constructor injection을 유지합니다.
- 변환된 클래스는 동일한 이름으로 동일 패키지에 생성됩니다.

## 금지 사항
- 불필요한 추상화 추가 금지
- Coroutine 도입 금지
- 성능 최적화 금지
- 테스트 코드 신규 추가 금지 (요청 시 제외)

## 커뮤니케이션
- 변경 사항을 코드 전에 간단히 설명합니다.
- 변경으로 인한 동작 차이가 있으면 반드시 명시합니다.
- 저는 Kotlin 초보이므로, 사용한 문법에 대해 짧은 설명을 함께 제공합니다.