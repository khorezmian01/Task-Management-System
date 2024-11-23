package sfera.tsm.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.io.Serializable;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskDto implements Serializable {

    @Schema(hidden = true)
    private Long id;
    @NotBlank(message = "поле не должно пустым")
    private String title;
    private String description;
    private String priority;
    @Schema(hidden = true)
    private String status;
    @Schema(hidden = true)
    private List<CommentDto> comments;

}
