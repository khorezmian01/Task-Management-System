package sfera.tsm.dto.req;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReqExecutors {
    @NotBlank(message = "поле не должно быть пустым")
    @Positive(message = "userId должен быть больше 0")
    private Long userId;
}
