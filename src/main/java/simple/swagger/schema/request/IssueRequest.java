package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class IssueRequest {
    @Schema(minLength = 1, maxLength = 100, required = true)
    private String title;
    @Schema(minLength = 1, maxLength = 1000)
    private String description;
}
