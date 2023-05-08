package simple.swagger.schema.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmployeeRequest {
    private Long id;
    @Schema(maxLength = 20)
    private String name;
    @Schema(maxLength = 100)
    private String lastName;
}
