package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class CommentRequest {
    @Schema(example = "Some text", maxLength = 400)
    private String text;
    private int likes;
    private IssueRequest issue;
}
