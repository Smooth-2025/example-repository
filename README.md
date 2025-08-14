# 1️⃣ common_example
## 📄 파일 구조 형태
```java
com.example
├── global/
│   ├── common/
│   │   └── ApiResponse.java
│   └── exception/
│       ├── ErrorCode.java
│       ├── BusinessException.java
│       ├── CommonErrorCode.java
│       └── GlobalExceptionHandler.java
└── user/
    ├── controller/
    │   └── UserController.java
    ├── service/
    │   └── UserService.java
    ├── repository/
    │   └── UserRepository.java
    ├── entity/
    │   └── User.java
    ├── dto/
    │   ├── UserResponseDto.java
    │   ├── CreateUserRequestDto.java
    │   └── UpdateUserRequestDto.java
    └── exception/
        └── UserErrorCode.java
```
## 🙌 User Service 에러코드 (1xxx) 세부 분류 예시
- **10xx**: 기본 사용자 관리 관련
- **11xx**: 계정 상태 관련
- **12xx**: 계정 상태 관련

## 📌 새로운 예외를 만들고 싶다면..
1. `ErrorCode`를 implements한 `UserErrorCode`나 `AccidentErrorCode`를 만듦.
2. 그리고 각 에러에 대한 내용을 작성하고 `throw new BusinessException`을 작성함.
```java
@Getter
@AllArgsConstructor
public enum UserErrorCode implements ErrorCode {

    // 기본 사용자 관리
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, 1001, "사용자를 찾을 수 없습니다."),
    DUPLICATE_EMAIL(HttpStatus.CONFLICT, 1002, "이미 사용 중인 이메일입니다."),

    // 계정 상태
    ACCOUNT_LOCKED(HttpStatus.FORBIDDEN, 1011, "계정이 잠금 상태입니다."),
    ACCOUNT_DISABLED(HttpStatus.FORBIDDEN, 1012, "비활성화된 계정입니다."),
    ACCOUNT_EXPIRED(HttpStatus.FORBIDDEN, 1013, "만료된 계정입니다."),

    // 권한 관련
    INSUFFICIENT_PERMISSION(HttpStatus.FORBIDDEN, 1021, "권한이 부족합니다."),
    ADMIN_ONLY_ACCESS(HttpStatus.FORBIDDEN, 1022, "관리자만 접근 가능합니다.");


    private final HttpStatus httpStatus;
    private final Integer code;
    private final String message;
}
```
```java
throw new BusinessException(UserErrorCode.USER_NOT_FOUND,
                    "이메일 " + email + "로 등록된 사용자를 찾을 수 없습니다.");
throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
```
----