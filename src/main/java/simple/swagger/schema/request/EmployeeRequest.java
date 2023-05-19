package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeRequest {
    @Schema(minLength = 1, maxLength = 20, required = true)
    private String name;
    @Schema(minLength = 1, maxLength = 100, required = true)
    private String lastName;
}
