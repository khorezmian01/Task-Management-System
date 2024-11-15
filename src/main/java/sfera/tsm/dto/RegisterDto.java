package sfera.tsm.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterDto {
    @Email(message = "email should be valid")
    private String email;
    @NotBlank
//    @Pattern(regexp = "")
    @Size(min = 3, max = 8, message = "email should be between 3 and 8 characters")
    private String password;
}
