package sfera.tsm.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginDto(@Email String email, @NotBlank @Size(min = 3, max=8) String password) {
}
