package sfera.tsm.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TaskDto {
    @Schema(hidden = true)
    private Long id;
    @NotBlank
    private String title;
    private String description;
    @Schema(hidden = true)
    private String priority;
    @Schema(hidden = true)
    private String status;
    @Schema(hidden = true)
    private List<CommentDto> comments;
}
