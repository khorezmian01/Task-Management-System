package sfera.tsm.controller;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.tsm.dto.LoginDto;
import sfera.tsm.dto.ProfileDto;
import sfera.tsm.dto.RegisterDto;
import sfera.tsm.service.AuthService;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @Operation(summary = "регистрация пользователя", description = "После успешной регистрации возвращает ID пользователя")
    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody @Valid RegisterDto registerDto){
        Long register = authService.register(registerDto);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDto> login(@RequestBody @Valid LoginDto loginDto){
        ProfileDto login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }

}
