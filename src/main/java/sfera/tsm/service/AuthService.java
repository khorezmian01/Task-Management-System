package sfera.tsm.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sfera.tsm.dto.CreateUserDto;
import sfera.tsm.dto.LoginDto;
import sfera.tsm.entity.User;
import sfera.tsm.exception.AlreadyExistException;
import sfera.tsm.repository.UserRepository;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    private Long register(CreateUserDto createUserDto){
        boolean b = userRepository.existsByEmail(createUserDto.getEmail());
        if(b)
            throw new AlreadyExistException("User with this email: " + createUserDto.getEmail() + " already exist");
        User user = User.builder()
                .email(createUserDto.getEmail())
                .password(passwordEncoder.encode(createUserDto.getPassword()))
                .build();
        User save = userRepository.save(user);
        return save.getId();
    }

    private String login(LoginDto loginDto){

    }

}
