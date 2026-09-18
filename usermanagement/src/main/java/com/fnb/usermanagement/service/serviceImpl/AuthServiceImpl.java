package com.fnb.usermanagement.service.serviceImpl;

import com.fnb.usermanagement.dto.LoginRequest;
import com.fnb.usermanagement.dto.LoginResponse;
import com.fnb.usermanagement.dto.RegisterRequest;
import com.fnb.usermanagement.dto.RegisterResponse;
import com.fnb.usermanagement.entity.Role;
import com.fnb.usermanagement.entity.User;
import com.fnb.usermanagement.entity.UserCredential;
import com.fnb.usermanagement.repository.UserCredentialsRepository;
import com.fnb.usermanagement.repository.UserRepository;
import com.fnb.usermanagement.security.JwtService;
import com.fnb.usermanagement.service.AuthService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;

    private final UserCredentialsRepository userCredentialsRepository;

    private final PasswordEncoder passwordEncoder;

    private final JwtService jwtService;

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
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .build();
        userCredentialsRepository.save(userCredential);

        return toUserResponse(user);

    }

    @Override
    public LoginResponse login(LoginRequest loginRequest) {

        User user =  userRepository.findByEmail(loginRequest.getEmail());

        String token =  jwtService.generateToken(user);

        return LoginResponse.builder()
                .token(token)
                .customerId(user.getCustomerId())
                .email(user.getEmail())
                .role(user.getRole().name())
                .build();
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
