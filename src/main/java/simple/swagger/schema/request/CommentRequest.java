package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequest {
    @Schema(minLength = 1, maxLength = 400, required = true)
    private String text;
    private int likes;
}
