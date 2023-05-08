package simple.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Employee;
import simple.service.EmployeeService;
import simple.swagger.schema.request.EmployeeRequest;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/employee")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    @Operation(
            tags = "Employee",
            summary = "Getting employee list",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            )
    )
    public List<Employee> getList() {
        return employeeService.getEmployeeList();
    }

    @GetMapping("/{id}")
    @Operation(
            tags = "Employee",
            operationId = "id",
            summary = "Getting employee by id",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            )
    )
    public Employee getById(@PathVariable Long id) {
        return employeeService.getEmployeeById(id);
    }

    @PostMapping("/create")
    @Operation(
            tags = "Employee",
            operationId = "employee",
            summary = "Create new employee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = EmployeeRequest.class))
            ),
            responses = @ApiResponse(
                    responseCode = "201",
                    description = "Created",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            )
    )
    public ResponseEntity<Object> create(@RequestBody Employee employee) {
        return employeeService.createEmployee(employee);
    }

    @PutMapping("/update")
    @Operation(
            tags = "Employee",
            operationId = "employee",
            summary = "Update employee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = EmployeeRequest.class))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            )
    )
    public ResponseEntity<Object> update(@RequestBody Employee employee) {
        return employeeService.updateEmployee(employee);
    }

    @PatchMapping("/edit/{id}")
    @Operation(
            tags = "Employee",
            operationId = "employee",
            summary = "Edit employee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = EmployeeRequest.class))
            ),
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = Employee.class))
            )
    )
    public ResponseEntity<Object> edit(@RequestBody Map<String, Object> employeeMap, @PathVariable Long id) {
        return employeeService.editEmployee(employeeMap, id);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(
            tags = "Employee",
            operationId = "id",
            summary = "Delete employee",
            responses = @ApiResponse(
                    responseCode = "200",
                    description = "OK"
            )
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return employeeService.remove(id);
    }
}
