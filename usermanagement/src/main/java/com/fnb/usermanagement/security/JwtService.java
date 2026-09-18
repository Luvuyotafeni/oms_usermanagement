package com.fnb.usermanagement.security;

import com.fnb.usermanagement.entity.User;

public interface JwtService {

    String generateToken(User user);

    boolean validateToken(String token);

    String extractEmailFromToken(String token);
}
