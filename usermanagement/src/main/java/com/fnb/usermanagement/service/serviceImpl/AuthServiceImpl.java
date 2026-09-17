package com.fnb.usermanagement.service.serviceImpl;

import com.fnb.usermanagement.dto.RegisterRequest;
import com.fnb.usermanagement.dto.RegisterResponse;
import com.fnb.usermanagement.entity.Role;
import com.fnb.usermanagement.entity.User;
import com.fnb.usermanagement.entity.UserCredential;
import com.fnb.usermanagement.repository.UserCredentialsRepository;
import com.fnb.usermanagement.repository.UserRepository;
import com.fnb.usermanagement.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    @Override
    @Transactional
    public RegisterResponse register(RegisterRequest registerRequest) {
        User user = User.builder()
                .firstName(registerRequest.getFirstName())
                .surname(registerRequest.getSurname())
                .email(registerRequest.getEmail())
                .role(Role.CUSTOMER)
                .build();
        user = userRepository.save(user);

        UserCredential userCredential = UserCredential.builder()
                .user(user)
                .password(registerRequest.getPassword())
                .build();
        userCredentialsRepository.save(userCredential);

        return toUserResponse(user);

    }


    private RegisterResponse toUserResponse(User user){
        return RegisterResponse.builder()
                .customerId(user.getCustomerId())
                .firstName(user.getFirstName())
                .surname(user.getSurname())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
    }
}
