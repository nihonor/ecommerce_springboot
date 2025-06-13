package com.project.ecommerce.controllers;


import com.project.ecommerce.config.JwtConfig;
import com.project.ecommerce.dtos.JwtResponse;
import com.project.ecommerce.dtos.LoginRequest;
import com.project.ecommerce.dtos.UserDto;
import com.project.ecommerce.mapper.UserMapper;
import com.project.ecommerce.repositories.UserRepository;
import com.project.ecommerce.services.JwtService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserMapper userMapper;
    private final JwtConfig jwtConfig;

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletResponse response
    ) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginRequest.getEmail(), loginRequest.getPassword()
        ));
        var user=userRepository.findByEmail(loginRequest.getEmail()).orElseThrow();
        var accessToken=jwtService.generateAccessToken(user);
        var refreshToken=jwtService.generateRefreshToken(user);
        var cookie=new Cookie("refreshToken",refreshToken);
        cookie.setHttpOnly(true);
        cookie.setPath("/auth");
        cookie.setMaxAge(jwtConfig.getRefreshTokenExpiration());
        cookie.setSecure(true);
        response.addCookie(cookie);
        return ResponseEntity.ok(new JwtResponse(accessToken));

    }
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<Void> handleBadCredentialsException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    @PostMapping("/validate")
    public boolean validateToken(@RequestHeader("Authorization") String authHeader) {
        System.out.println("validate called");
        var token=authHeader.replace("Bearer ", "");
        return jwtService.validateToken(token);

    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        var authentication=SecurityContextHolder.getContext().getAuthentication();
        var userId=(Long)authentication.getPrincipal();
        var user=userRepository.findById(userId).orElse(null);
        if(user==null){
            return ResponseEntity.notFound().build();
        }
        var userDto=userMapper.toDto(user);
        return ResponseEntity.ok(userDto);
    }

}
