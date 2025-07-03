# My First REST API - Backend

이 프로젝트는 Spring Boot를 사용하여 REST API 백엔드를 개발하는 학습용 프로젝트입니다.
특히, **테스트 주도 개발(TDD)** 방식을 적용하여 견고하고 유지보수하기 쉬운 코드를 작성하는 데 중점을 두고 있습니다.

## 🚀 개발 방식

이 프로젝트는 **Gemini CLI**의 도움을 받아 진행되고 있습니다. Gemini CLI는 TDD 학습 멘토 역할을 수행하며, 다음 단계를 안내하고 코드 품질에 대한 피드백을 제공합니다:

1.  **기능 논의**: 다음 구현할 기능을 정의합니다.
2.  **실패 테스트 제안**: 기능을 검증할 실패하는 테스트 케이스를 제안합니다.
3.  **테스트 작성**: 제안을 바탕으로 테스트 코드를 작성합니다.
4.  **실패 확인**: 테스트가 의도대로 실패하는지 확인합니다.
5.  **구현 힌트 제공**: 테스트를 통과시킬 구현 코드에 대한 힌트와 방향성을 제공합니다.
6.  **코드 구현**: 힌트를 바탕으로 스스로 기능 코드를 작성합니다.
7.  **성공 확인**: 테스트가 성공하는지 확인합니다.
8.  **리팩토링 제안**: 코드 개선점을 논의하고 적용합니다.

## 🛠️ 기술 스택

*   **언어**: Java
*   **프레임워크**: Spring Boot
*   **빌드 도구**: Gradle
*   **데이터베이스**: H2 (인메모리 데이터베이스)
*   **ORM**: Spring Data JPA
*   **테스트**: JUnit 5, MockMvc
*   **기타**: Lombok

## 📦 주요 기능 (예정)

*   게시글(Post) CRUD
*   댓글(Comment) CRUD
*   사용자 인증/인가 (향후 추가 예정)

## ⚙️ 설치 및 실행

1.  **프로젝트 클론**:
    ```bash
    git clone https://github.com/Mrbaeksang/my-first-rest-api-backend.git
    cd my-first-rest-api-backend
    ```
2.  **의존성 설치**:
    Gradle이 자동으로 의존성을 다운로드합니다.
3.  **애플리케이션 실행**:
    ```bash
    ./gradlew bootRun
    ```
    (Windows의 경우 `gradlew.bat bootRun`)

## 🧪 테스트 실행

```bash
./gradlew test
```
(Windows의 경우 `gradlew.bat test`)

---

**Made with ❤️ and Gemini CLI**
