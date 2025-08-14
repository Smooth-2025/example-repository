package com.smooth.usage_example.common_example.user.service;

import com.example.test80.global.exception.BusinessException;
import com.example.test80.user.dto.CreateUserRequestDto;
import com.example.test80.user.dto.UserResponseDto;
import com.example.test80.user.exception.UserErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@Service
public class UserService {

    // 간단한 인메모리 저장소 (실제 프로젝트에서는 DB 사용)
    private final Map<Long, UserResponseDto> userStorage = new HashMap<>();
    private final Map<String, UserResponseDto> emailIndex = new HashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public UserResponseDto createUser(CreateUserRequestDto request) {
        log.info("Creating user with email: {}", request.getEmail());

        // 이메일 중복 체크
        if (emailIndex.containsKey(request.getEmail())) {
            throw new BusinessException(UserErrorCode.DUPLICATE_EMAIL,
                    "이미 가입된 이메일입니다: " + request.getEmail());
        }

        // 비밀번호 검증 (간단한 예시)
        if ("password123".equals(request.getPassword())) {
            throw new BusinessException(UserErrorCode.INVALID_PASSWORD,
                    "너무 간단한 비밀번호입니다. 더 복잡한 비밀번호를 사용해주세요.");
        }

        // 사용자 생성
        Long userId = idGenerator.getAndIncrement();
        UserResponseDto user = UserResponseDto.builder()
                .id(userId)
                .name(request.getName())
                .email(request.getEmail())
                .emailVerified(false)
                .build();

        userStorage.put(userId, user);
        emailIndex.put(request.getEmail(), user);

        log.info("User created successfully with ID: {}", userId);
        return user;
    }

    public UserResponseDto getUserById(Long userId) {
        log.info("Finding user by ID: {}", userId);

        UserResponseDto user = userStorage.get(userId);
        if (user == null) {
            //throw new BusinessException(UserErrorCode.USER_NOT_FOUND);
            throw new BusinessException(UserErrorCode.USER_CANT_FOUND);
        }

        return user;
    }

    public UserResponseDto getUserByEmail(String email) {
        log.info("Finding user by email: {}", email);

        UserResponseDto user = emailIndex.get(email);
        if (user == null) {
            throw new BusinessException(UserErrorCode.USER_NOT_FOUND,
                    "이메일 " + email + "로 등록된 사용자를 찾을 수 없습니다.");
        }

        return user;
    }

    public UserResponseDto verifyEmail(Long userId) {
        log.info("Verifying email for user ID: {}", userId);

        UserResponseDto user = getUserById(userId); // 여기서 USER_NOT_FOUND 예외 발생 가능

        if (user.isEmailVerified()) {
            throw new BusinessException(UserErrorCode.EMAIL_NOT_VERIFIED,
                    "이미 인증된 이메일입니다.");
        }

        // 이메일 인증 처리
        UserResponseDto verifiedUser = UserResponseDto.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .emailVerified(true)
                .build();

        userStorage.put(userId, verifiedUser);
        emailIndex.put(user.getEmail(), verifiedUser);

        log.info("Email verified successfully for user ID: {}", userId);
        return verifiedUser;
    }

    public void deleteUser(Long userId) {
        log.info("Deleting user with ID: {}", userId);

        UserResponseDto user = getUserById(userId);

        userStorage.remove(userId);
        emailIndex.remove(user.getEmail());

        log.info("User deleted successfully with ID: {}", userId);
    }
}