package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.application.service.UserService;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.presentation.requestDto.SignInRequestDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserRoleDTO;
import com.logistic.client.user.presentation.requestDto.UserDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Tag(name ="User API", description = "유저 관련 API")
@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "유저 회원 가입 처리")
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDTO requestUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUp(requestUser));
    }

    @Operation(summary = "로그인", description = "유저 로그인 처리")
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@Valid @RequestBody SignInRequestDTO signInRequest, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }
        String token = userService.signIn(signInRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + token).build();
    }

    @Operation(summary = "유저 단일 조회", description = "파라미터로 받아온 유저 네임을 통해 유저 단일 조회")
    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username, HttpServletRequest request) {
        String userRole = request.getHeader("role");
        String signInUsername = request.getHeader("username");

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username, signInUsername, userRole));
    }

    @Operation(summary = "허브 담당자 조회", description = "허브 아이디를 통해 허브 담당자 조회")
    @GetMapping("/{hubId}")
    public ResponseEntity<?> getUserByHubId(@PathVariable UUID hubId) {
        return ResponseEntity.status(HttpStatus.OK).body(userService.getUserByHubId(hubId));
    }

    @Operation(summary = "유저 검색/리스트 조회", description = "검색 조건을 통해 유저 조회")
    @GetMapping("")
    public ResponseEntity<?> getUsers(@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
                                      @RequestParam(name = "size", required = false, defaultValue = "10") Integer  size,
                                      @RequestParam(name = "sortBy", required = false, defaultValue = "createdAt") String sortBy,
                                      @RequestParam(name = "orderBy", required = false, defaultValue = "asc") String orderBy,
                                      @RequestParam(name = "userRole", required = false) UserRole searchRole,
                                      HttpServletRequest request) {
        String userRole = request.getHeader("role");

        if (size != 10 && size != 30 && size != 50) {
            size = 10;
        }
        Sort.Direction direction = orderBy.equalsIgnoreCase("desc") ? Sort.Direction.DESC : Sort.Direction.ASC;
        PageRequest pageable = PageRequest.of(page, size, Sort.by(direction, orderBy));

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(userRole, pageable, searchRole));
    }

    @Operation(summary = "유저 정보 수정", description = "파라미터를 통해 들어온 유저의 정보를 수정 처리")
    @PatchMapping("/update/{username}")
    public ResponseEntity<?> updateUser(@PathVariable String username, @Valid @RequestBody UpdateUserDTO requestDto,
                                            BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }

        String signInUsername = request.getHeader("username");
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUser(username, requestDto, signInUsername));
    }

    @Operation(summary = "유저 권한 수정", description = "파라미터를 통해 들어온 유저의 권한을 수정 처리")
    @PatchMapping("/updateRole/{username}")
    public ResponseEntity<?> updateUserRole(@PathVariable String username, @Valid @RequestBody UpdateUserRoleDTO requestDto,
                                        BindingResult bindingResult, HttpServletRequest request) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }

        String userRole = request.getHeader("role");
        String signInUsername = request.getHeader("username");
        return ResponseEntity.status(HttpStatus.OK).body(userService.updateUserRole(username, requestDto, userRole, signInUsername));
    }

    @Operation(summary = "유저 삭제", description = "파라미터를 통해 들어온 유저를 논리적으로 삭제 처리")
    @PatchMapping("/delete/{username}")
    public ResponseEntity<?> deleteUser(@PathVariable String username, HttpServletRequest request) {
        String userRole = request.getHeader("role");
        String signInUsername = request.getHeader("username");

        userService.deleteUser(username, userRole, signInUsername);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    private Map<String, Object> ValidationErrorResponse(BindingResult bindingResult) {
        List<Map<String, String>> errors = bindingResult.getFieldErrors().stream()
                .map(fieldError -> Map.of(
                        "field", fieldError.getField(),
                        "message", fieldError.getDefaultMessage(),
                        "rejectedValue", String.valueOf(fieldError.getRejectedValue()) // 입력된 값도 포함
                ))
                .toList();

        return Map.of(
                "status", 400,
                "error", "Validation Field",
                "message", errors
        );
    }
}

