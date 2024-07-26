package com.a_olumide.todo_application.service;

import com.a_olumide.todo_application.dto.LoginDto;
import com.a_olumide.todo_application.dto.RegisterDto;

public interface AuthService {
    String register(RegisterDto registerDto);

    String login(LoginDto login);
}
