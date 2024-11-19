package sfera.tsm;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import sfera.tsm.entity.User;
import sfera.tsm.entity.enums.ERole;
import sfera.tsm.repository.UserRepository;

@SpringBootApplication
@RequiredArgsConstructor
@EnableCaching
public class TsmApplication {

    private final PasswordEncoder passwordEncoder;
    @Value("${spring.jpa.hibernate.ddl-auto}")
    private String ddl;

    public static void main(String[] args) {
        SpringApplication.run(TsmApplication.class, args);
    }

    @Bean
    public CommandLineRunner runner(UserRepository userRepository){
        return args -> {
            if(ddl.equals("create") || ddl.equals("create-drop")) {
                User user = User.builder()
                        .email("admin@gmail.com")
                        .role(ERole.ROLE_ADMIN)
                        .password(passwordEncoder.encode("admin123"))
                        .build();
                userRepository.save(user);
            }
        };
    }
}
