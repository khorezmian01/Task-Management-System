package sfera.tsm.dto.res;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ResPageable {
    @Schema(hidden = true)
    private int page;
    private int size;
    private int totalPage;
    private Long totalElements;
    private Object data;
}
