package com.logistic.client.user.application.service;

import com.logistic.client.user.application.dto.responseDto.CompanyResDTO;
import com.logistic.client.user.application.dto.responseDto.HubResDTO;
import com.logistic.client.user.application.dto.responseDto.UserResDTO;
import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.infrastructure.Security.AuthService;
import com.logistic.client.user.infrastructure.Security.dto.CreateTokenDTO;
import com.logistic.client.user.infrastructure.client.CompanyClient;
import com.logistic.client.user.infrastructure.client.HubClient;
import com.logistic.client.user.infrastructure.configuration.customException.AccessDeniedException;
import com.logistic.client.user.infrastructure.configuration.customException.SamePasswordException;
import com.logistic.client.user.infrastructure.configuration.customException.UserAlreadyExistException;
import com.logistic.client.user.infrastructure.repository.UserRepositoryImpl;
import com.logistic.client.user.presentation.requestDto.SignInRequestDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserRoleDTO;
import com.logistic.client.user.presentation.requestDto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryImpl userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    private final HubClient hubClient;
    private final CompanyClient companyClient;

    @Value("${service.admin.code}")
    private String adminCode;

    public UserResDTO signUp(UserDTO requestUser) {
        try {
            if(userRepository.existsByUsernameAndIsDeletedFalse(requestUser.getUsername())) {
                throw new UserAlreadyExistException("이미 존재하는 유저 이름입니다.");
            }

            if(requestUser.getRole().equals(UserRole.MASTER) && !requestUser.getAdminCode().equals(adminCode)) {
                throw new AccessDeniedException("관리자 코드가 일치하지 않으므로 마스터 권한으로 회원가입 하실 수 없습니다.");
            }

            User user = requestUser.toUser(passwordEncoder.encode(requestUser.getPassword()));
            user.setCreatedAt(LocalDateTime.now());
            user.setCreatedBy(requestUser.getUsername());
            userRepository.save(user);

            return UserResDTO.from(user);
        }
        catch (Exception e) {
            throw e;
        }
    }

    public String signIn(SignInRequestDTO signInRequest) {
        try {
            User user = userRepository.findByUsernameAndIsDeletedFalse(signInRequest.getUsername())
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            if (!passwordEncoder.matches(signInRequest.getPassword(), user.getPassword())){
                throw new IllegalArgumentException("비밀번호가 올바르지 않습니다.");
            }
            return authService.createAccessToken(new CreateTokenDTO(user.getUserId(), user.getUsername(), user.getRole(), user.getSlackId()));
        }
        catch (Exception e) {
            throw e;
        }
    }

    public UserResDTO getUser(String username, String signInUsername, String userRole) {
        try {
            UserRole role = UserRole.valueOf(userRole);

            if(!role.equals(UserRole.MASTER) && !username.equals(signInUsername)) {
                throw new AccessDeniedException("해당 유저 정보에 대해 접근 권한이 존재하지 않습니다.");
            }

            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            return UserResDTO.from(user);
        }
        catch(Exception e) {
            throw e;
        }
    }

    public UserResDTO getUserByHubId(UUID hubId) {
        try {
            User user = userRepository.findByHubIdAndIsDeletedFalse(hubId)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));
            return UserResDTO.from(user);
        }
        catch(Exception e) {
            throw e;
        }
    }

    public Page<UserResDTO> getUsers(String userRole, PageRequest pageable, UserRole searchRole) {
        try {
            UserRole role = UserRole.valueOf(userRole);

            if(!role.equals(UserRole.MASTER)) {
                throw new AccessDeniedException("유저 정보에 대해 접근 권한이 존재하지 않습니다.");
            }

            Page<User> userList = userRepository.findAllByRoleAndIsDeletedFalse(searchRole, pageable);

            if(userList.isEmpty()) {
                throw new IllegalArgumentException("조건에 맞는 유저가 존재하지 않습니다.");
            }

            return userList.map(user -> {
                return UserResDTO.from(user);
            });
        }
        catch(Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserResDTO updateUser(String username, UpdateUserDTO requestUser, String signInUsername) {
        try {
            //본인 아니면 유저 정보 수정 불가
            if(!username.equals(signInUsername)) {
                throw new AccessDeniedException("유저 정보에 대해 수정 권한이 존재하지 않습니다.");
            }
            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            if(requestUser.getPassword() != null) {
                if(passwordEncoder.matches(requestUser.getPassword(), user.getPassword())) {
                    throw new SamePasswordException("동일한 비밀번호로는 변경이 불가합니다.");
                }
                user.setPassword(passwordEncoder.encode(requestUser.getPassword()));
            }
            if(requestUser.getSlackId() != null) {
                user.setSlackId(requestUser.getSlackId());
            }
            user.setUpdatedAt(LocalDateTime.now());
            user.setUpdatedBy(signInUsername);

            return UserResDTO.from(user);
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public UserResDTO updateUserRole(String username, UpdateUserRoleDTO requestDto, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);

            if(!role.equals(UserRole.MASTER)) {
                throw new AccessDeniedException("유저 정보에 대해 수정 권한이 존재하지 않습니다.");
            }
            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            if(requestDto.getUserRole().equals(UserRole.HUB_MANAGER))  {
                HubResDTO hub = hubClient.getHubById(requestDto.getHubId());
                user.setHubId(hub.getHubId());
            }

            if(requestDto.getUserRole().equals(UserRole.COMPANY_MANAGER))  {
                CompanyResDTO company = companyClient.getCompany(requestDto.getCompanyId());
                user.setCompanyId(company.getCompanyId());
            }
            user.setRole(requestDto.getUserRole());
            user.setUpdatedAt(LocalDateTime.now());
            user.setUpdatedBy(signInUsername);

            return UserResDTO.from(user);
        }
        catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public void deleteUser(String username, String userRole, String signInUsername) {
        try {
            UserRole role = UserRole.valueOf(userRole);

            if(!role.equals(UserRole.MASTER)) {
                throw new AccessDeniedException("유저 정보에 대해 삭제 권한이 존재하지 않습니다.");
            }
            User user = userRepository.findByUsernameAndIsDeletedFalse(username)
                    .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 유저입니다."));

            user.setDeletedBy(signInUsername);
            user.setDeletedAt(LocalDateTime.now());
            user.setIsDeleted(true);
        }
        catch (Exception e) {
            throw e;
        }
    }
}
