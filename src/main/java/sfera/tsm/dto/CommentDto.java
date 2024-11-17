package sfera.tsm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommentDto {

    @Schema(hidden = true)
    private Long id;
    @NotBlank(message = "поле не должно быть пустым")
    private String text;
    @NotBlank(message = "taskId должен быть не пустым")
    private Long taskId;
    @Schema(hidden = true)
    private String userEmail;
    @Schema(hidden = true)
    private LocalDateTime createdAt;
}
