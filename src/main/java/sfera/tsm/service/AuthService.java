package sfera.tsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.tsm.dto.ProfileDto;
import sfera.tsm.dto.RegisterDto;
import sfera.tsm.dto.LoginDto;
import sfera.tsm.entity.User;
import sfera.tsm.entity.enums.ERole;
import sfera.tsm.exception.AlreadyExistException;
import sfera.tsm.exception.NotFoundException;
import sfera.tsm.exception.PasswordDontMatchException;
import sfera.tsm.repository.UserRepository;
import sfera.tsm.security.JwtProvider;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;

    public Long register(RegisterDto registerDto){
        boolean b = userRepository.existsByEmail(registerDto.getEmail());
        if(b)
            throw new AlreadyExistException("User with this email: " + registerDto.getEmail() + " already exist");
        User user = User.builder()
                .email(registerDto.getEmail())
                .password(passwordEncoder.encode(registerDto.getPassword()))
                .role(ERole.ROLE_USER)
                .build();
        User save = userRepository.save(user);
        return save.getId();
    }

    public ProfileDto login(LoginDto loginDto){
        User user = userRepository.findByEmail(loginDto.email())
                .orElseThrow(() -> new NotFoundException("User with this email: " + loginDto.email()+"not found"));
        if(!passwordEncoder.matches(loginDto.password(), user.getPassword())){
            throw new PasswordDontMatchException();
        }
        return ProfileDto.builder()
                .email(user.getEmail())
                .jwt(jwtProvider.generateToken(user.getEmail()))
                .role(user.getRole().name())
                .build();
    }

}
