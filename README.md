# 1️⃣ common_example
## 📄 파일 구조 형태 - 우선 글로벌은 다 복붙하세요! user에는 사용 예시가 있습니다!
```java
common_example
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

## 💡 Service 에러코드 시작 예시
- **1xxx**: user-service
- **2xxx**: drivecast-service
- **3xxx**: driving-analysis-service
- **4xxx**: api-gateway
- **5xxx**: pothole-analysis-service
- **6xxx**: accident-analysis-service

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
# 2️⃣ bedrock_example
## 📄 파일 구조 형태
```java
bedrock_example
├── BedrockController.java
└── BedrockService.java
```
## 🔹 설명
> `BedrockController`에서 `/api/bedrock/chat` 엔드포인트를 제공합니다.  
> 클라이언트가 메시지를 보내면 BedrockService를 통해 모델 응답을 받아 반환합니다.

## 📌 BedrockService 주요 기능
### BedrockRuntimeClient 생성

```java
BedrockRuntimeClient.builder()
    .region(Region.AP_NORTHEAST_2)
    .credentialsProvider(ProfileCredentialsProvider.create("smooth"))
    .build();
```

### 모델 호출 구조

```java
Map<String, Object> requestBody = Map.of(
    "anthropic_version", "bedrock-2023-05-31",
    "messages", List.of(Map.of(
        "role", "user",
        "content", List.of(Map.of(
            "type", "text",
            "text", message
        ))
    )),
    "max_tokens", 100,
    "temperature", 0.7
);
```
### InvokeModelRequest 생성 후 모델 호출

```java
InvokeModelRequest request = InvokeModelRequest.builder()
    .modelId("anthropic.claude-3-haiku-20240307-v1:0")
    .body(SdkBytes.fromUtf8String(jsonBody))
    .build();

InvokeModelResponse response = bedrockClient.invokeModel(request);
```
## 📌 BedrockController 사용 예시
### 요청
```http request
POST /api/bedrock/chat
Content-Type: application/json

{
  "message": "Hello, Bedrock!"
}
```
- 요청 바디에는 message 키만 필요합니다.

## 💡 주의 사항
### AWS CLI 프로파일(smooth)설정
- 자신의 AWS CLI 프로파일이 있다면 그것으로 바꾸시면 됩니다!
### 모델 ID와 Region
- 모델 ID(anthropic.claude-3-haiku-20240307-v1:0)와 Region을 맞게 설정하셔야 합니다!
- Region별로 제공하는 모델이 다르니 확인이 필요합니다!
- max_tokens, temperature 등 파라미터는 필요에 따라 조정해주세요!
