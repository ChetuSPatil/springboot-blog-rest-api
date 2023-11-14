package com.javateche.blog.service;

import com.javateche.blog.payload.LoginDTO;
import com.javateche.blog.payload.RegisterDTO;

public interface AuthService {
    String login(LoginDTO loginDTO);

    String register(RegisterDTO registerDTO);
}
