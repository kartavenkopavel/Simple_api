package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class PostRequest {
    @Schema(required = true, example = "1")
    private Long id;
    @Schema(maxLength = 100)
    private String title;
    @Schema(maxLength = 1000)
    private String description;
}
