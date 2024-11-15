package sfera.tsm.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import sfera.tsm.dto.LoginDto;
import sfera.tsm.dto.ProfileDto;
import sfera.tsm.dto.RegisterDto;
import sfera.tsm.service.AuthService;

@RestController
@RequestMapping
@RequiredArgsConstructor
@CrossOrigin(maxAge = 3600)
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<Long> register(@RequestBody RegisterDto registerDto){
        Long register = authService.register(registerDto);
        return ResponseEntity.ok(register);
    }

    @PostMapping("/login")
    public ResponseEntity<ProfileDto> login(@RequestBody LoginDto loginDto){
        ProfileDto login = authService.login(loginDto);
        return ResponseEntity.ok(login);
    }

}
