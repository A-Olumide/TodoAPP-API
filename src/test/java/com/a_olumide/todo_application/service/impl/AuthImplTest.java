package com.a_olumide.todo_application.service.impl;

import com.a_olumide.todo_application.dto.LoginDto;
import com.a_olumide.todo_application.dto.RegisterDto;
import com.a_olumide.todo_application.entity.Role;
import com.a_olumide.todo_application.entity.User;
import com.a_olumide.todo_application.exception.TodoAPIException;
import com.a_olumide.todo_application.repository.RoleRepository;
import com.a_olumide.todo_application.repository.UserRepository;
import com.a_olumide.todo_application.security.JwtTokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @InjectMocks
    private AuthImpl authService;
    private RegisterDto registerDto;
    private LoginDto loginDto;
    private User user;
    private Role role;

    @BeforeEach
    void setUp() {
        registerDto = new RegisterDto();
        registerDto.setName("Olumide");
        registerDto.setUsername("mide");
        registerDto.setEmail("olumideakindolie@yahoo.com");
        registerDto.setPassword("password");

        loginDto = new LoginDto();
        loginDto.setUsernameOrEmail("mide");
        loginDto.setPassword("password");

        user = new User();
        user.setUsername("mide");
        user.setEmail("olumideakindolie@yahoo.com");
        user.setPassword("encodedPassword");

        role = new Role();
        role.setName("ROLE_USER");


    }

    @Test
    void testRegister_Success() {

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(false);
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(passwordEncoder.encode(registerDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);
        String result = authService.register(registerDto);
        assertEquals("User Registered successfully", result);
        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void register_Fail_UsernameExists() {

        when(userRepository.existsByUsername(registerDto.getUsername())).thenReturn(true);
        TodoAPIException exception = assertThrows(TodoAPIException.class, () -> authService.register(registerDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Username already exists", exception.getMessage());
    }

    @Test
    void register_Fail_EmailExists() {

        when(userRepository.existsByEmail(registerDto.getEmail())).thenReturn(true);
        TodoAPIException exception = assertThrows(TodoAPIException.class, () -> authService.register(registerDto));
        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("This Email has been registered", exception.getMessage());
    }

    @Test
    void login() {

        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(jwtTokenProvider.generateToken(authentication)).thenReturn("testToken");
        String token = authService.login(loginDto);
        assertNotNull(token);
        assertEquals("testToken", token);
        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider, times(1)).generateToken(authentication);
    }

}