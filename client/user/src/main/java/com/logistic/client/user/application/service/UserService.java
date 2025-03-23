package com.logistic.client.user.application.service;

import com.logistic.client.user.application.dto.responseDto.CompanyResDTO;
import com.logistic.client.user.application.dto.responseDto.HubResDTO;
import com.logistic.client.user.infrastructure.client.CompanyClient;
import com.logistic.client.user.infrastructure.client.HubClient;
import com.logistic.client.user.presentation.requestDto.SignInRequestDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserDTO;
import com.logistic.client.user.presentation.requestDto.UpdateUserRoleDTO;
import com.logistic.client.user.presentation.requestDto.UserDTO;
import com.logistic.client.user.application.dto.responseDto.UserResDTO;
import com.logistic.client.user.domain.model.User;
import com.logistic.client.user.domain.model.UserRole;
import com.logistic.client.user.infrastructure.Security.AuthService;
import com.logistic.client.user.infrastructure.Security.dto.CreateTokenDTO;
import com.logistic.client.user.infrastructure.configuration.customException.AccessDeniedException;
import com.logistic.client.user.infrastructure.configuration.customException.SamePasswordException;
import com.logistic.client.user.infrastructure.configuration.customException.UserAlreadyExistException;
import com.logistic.client.user.infrastructure.repository.UserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepositoryImpl userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;

    private final HubClient hubClient;
    private final CompanyClient companyClient;

    public UserResDTO signUp(UserDTO requestUser) {
        try {
            if(userRepository.existsByUsernameAndIsDeletedFalse(requestUser.getUsername())) {
                throw new UserAlreadyExistException("이미 존재하는 유저 이름입니다.");
            }

            if(requestUser.getRole().equals(UserRole.HUB_MANAGER))  {
                HubResDTO hub = hubClient.getHubById(requestUser.getHubId());
            }

            if(requestUser.getRole().equals(UserRole.COMPANY_MANAGER))  {
                CompanyResDTO company = companyClient.getCompany(requestUser.getCompanyId());
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

    //검색 및 페이징 추가 필요
    public List<UserResDTO> getUsers(String userRole) {
        try {
            UserRole role = UserRole.valueOf(userRole);

            if(!role.equals(UserRole.MASTER)) {
                throw new AccessDeniedException("유저 정보에 대해 접근 권한이 존재하지 않습니다.");
            }

            List<User> users = userRepository.findAllByIsDeletedFalse();
            return users.stream().map(UserResDTO::from).toList();
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

            user.setRole(requestDto.getUserRole());
            user.setUpdatedAt(LocalDateTime.now());
            user.setUpdatedBy(signInUsername);

            if(requestDto.getUserRole().equals(UserRole.HUB_MANAGER))  {
                HubResDTO hub = hubClient.getHubById(requestDto.getHubId());
            }

            if(requestDto.getUserRole().equals(UserRole.COMPANY_MANAGER))  {
                CompanyResDTO company = companyClient.getCompany(requestDto.getCompanyId());
            }

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
