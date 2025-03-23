package com.logistic.client.user.presentation.controller;

import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.presentation.requestDto.SignInRequestDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserRoleDTO;
import com.logistic.client.user.presentation.requestDto.UserDTO;
import com.logistic.client.user.application.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody UserDTO requestUser, BindingResult bindingResult) {
        if (bindingResult.hasErrors()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(ValidationErrorResponse(bindingResult));
        }
        return ResponseEntity.status(HttpStatus.OK).body(userService.signUp(requestUser));
    }

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

    @GetMapping("/{username}")
    public ResponseEntity<?> getUser(@PathVariable String username, HttpServletRequest request) {
        String userRole = request.getHeader("role");
        String signInUsername = request.getHeader("username");

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUser(username, signInUsername, userRole));
    }

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
        Pageable pageable = PageRequest.of(page, size, Sort.by(direction, sortBy));

        return ResponseEntity.status(HttpStatus.OK).body(userService.getUsers(userRole, pageable, searchRole));
    }

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

