package com.smooth.usage_example.common_example.user.controller;

import com.example.test80.global.common.ApiResponse;
import com.example.test80.user.dto.CreateUserRequestDto;
import com.example.test80.user.dto.TempResponseDto;
import com.example.test80.user.dto.UserResponseDto;
import com.example.test80.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 사용자 생성
     * POST /api/users
     */
    @PostMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> createUser(@Valid @RequestBody CreateUserRequestDto request) {
        log.info("POST /api/users - Creating user with email: {}", request.getEmail());

        UserResponseDto createdUser = userService.createUser(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(HttpStatus.CREATED, "사용자가 성공적으로 생성되었습니다.", createdUser));
    }

    /**
     * 사용자 조회 (ID로)
     * GET /api/users/{userId}
     */
    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserById(@PathVariable Long userId) {
        log.info("GET /api/users/{} - Finding user by ID", userId);

        UserResponseDto user = userService.getUserById(userId);

        return ResponseEntity.ok(
                ApiResponse.success("사용자 조회가 완료되었습니다.", user)
        );
    }

    /**
     * 사용자 조회 (이메일로)
     * GET /api/users?email=xxx@xxx.com
     */
    @GetMapping
    public ResponseEntity<ApiResponse<UserResponseDto>> getUserByEmail(@RequestParam String email) {
        log.info("GET /api/users?email={} - Finding user by email", email);

        UserResponseDto user = userService.getUserByEmail(email);

        return ResponseEntity.ok(
                ApiResponse.success("사용자 조회가 완료되었습니다.", user)
        );
    }

    /**
     * 이메일 인증
     * PUT /api/users/{userId}/verify-email
     */
    @PutMapping("/{userId}/verify-email")
    public ResponseEntity<ApiResponse<UserResponseDto>> verifyEmail(@PathVariable Long userId) {
        log.info("PUT /api/users/{}/verify-email - Verifying email", userId);

        UserResponseDto verifiedUser = userService.verifyEmail(userId);

        return ResponseEntity.ok(
                ApiResponse.success("이메일 인증이 완료되었습니다.", verifiedUser)
        );
    }

    /**
     * 사용자 삭제
     * DELETE /api/users/{userId}
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable Long userId) {
        log.info("DELETE /api/users/{} - Deleting user", userId);

        userService.deleteUser(userId);

        return ResponseEntity.ok(
                ApiResponse.success("사용자가 성공적으로 삭제되었습니다.")
        );
    }

    @GetMapping("/hi")
    public ResponseEntity<ApiResponse<Void>> getHello() {
        return ResponseEntity.ok(ApiResponse.success("hi"));
    }

    @GetMapping("/hi2")
    public ResponseEntity<ApiResponse<TempResponseDto>> getHello2() {
        return ResponseEntity.ok(ApiResponse.success("hi", new TempResponseDto("seohee")));
    }
}
