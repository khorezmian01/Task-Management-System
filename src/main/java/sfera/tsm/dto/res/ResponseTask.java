package sfera.tsm.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import sfera.tsm.dto.CommentDto;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResponseTask {
    @Schema(hidden = true)
    private Long id;
    private String title;
    private String description;
    private String priority;
    private String status;
    private List<CommentDto> comments;
}
