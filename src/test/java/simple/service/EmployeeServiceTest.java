package simple.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.entity.Issue;
import simple.repository.EmployeeRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeServiceTest {

    @Mock
    EmployeeRepository employeeRepository;

    @InjectMocks
    EmployeeService service;

    @Test
    void getEmployeeList_ReturnsValidResponseEntity() {
        var employees = List.of(
                Employee.builder()
                        .id(1L)
                        .name("Ivan")
                        .lastName("Ivanov")
                        .build(),
                Employee.builder()
                        .id(2L)
                        .name("Petr")
                        .lastName("Petrov")
                        .build()
        );
        doReturn(employees).when(employeeRepository).findAll();

        var responseEntity = service.getEmployeeList();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(employees, responseEntity.getBody());
    }

    @Test
    void getEmployeeList_ReturnsEmptyResponseEntityBody() {
        var employees = List.of();
        doReturn(employees).when(employeeRepository).findAll();

        var responseEntity = service.getEmployeeList();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void createEmployee_WithValidEmployee_ReturnsCreatedResponseEntity() {
        var employee = Employee.builder()
                .name("Ivan")
                .lastName("Ivanov")
                .build();

        var savedEmployee = Employee.builder()
                .id(1L)
                .name("Ivan")
                .lastName("Ivanov")
                .build();

        doReturn(savedEmployee).when(employeeRepository).save(employee);

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedEmployee, responseEntity.getBody());
    }

    @Test
    void createEmployee_WithMissingNameAndLastName_ReturnsBadRequestResponseEntity() {
        var employee = Employee.builder().build();

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'name' and 'lastName' fields is required", errorResponse.get("error"));
    }

    @Test
    void createEmployee_WithEmptyName_ReturnsBadRequestResponseEntity() {
        var employee = Employee.builder()
                .name("")
                .lastName("Ivanov")
                .build();

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'name' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void createEmployee_WithEmptyLastName_ReturnsBadRequestResponseEntity() {
        var employee = Employee.builder()
                .name("Ivan")
                .lastName("")
                .build();

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'lastName' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void createEmployee_WithInvalidNameLength_ReturnsBadRequestResponseEntity() {
        var employee = Employee.builder()
                .name("This is a very long name that exceeds the maximum length")
                .lastName("Ivanov")
                .build();

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'name' field length should be less then 20 characters", errorResponse.get("error"));
    }

    @Test
    void createEmployee_WithInvalidLastNameLength_ReturnsBadRequestResponseEntity() {
        var employee = Employee.builder()
                .name("Ivan")
                .lastName("This last name is too long to fit within the maximum allowed length of last names and checks the operation of the method")
                .build();

        var responseEntity = service.createEmployee(employee);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'lastName' field length should be less then 100 characters", errorResponse.get("error"));
    }

    @Test
    void getEmployeeById_ExistingEmployee_ReturnsEmployee() {
        var id = 1L;
        var employee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();

        doReturn(Optional.of(employee)).when(employeeRepository).findById(id);

        var result = service.getEmployeeById(id);

        assertNotNull(result);
        assertEquals(employee, result);
    }

    @Test
    void getEmployeeById_NotExistingEmployee_ThrowsResponseStatusExceptionWithNotFoundStatus() {
        var id = 1L;

        doReturn(Optional.empty()).when(employeeRepository).findById(id);

        assertThrows(ResponseStatusException.class, () -> service.getEmployeeById(id), "Not Found");
        try {
            service.getEmployeeById(id);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void editEmployee_WithValidName_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put(Employee.NAME_FIELD, "Petr");
        employeeMap.put(Employee.LAST_NAME_FIELD, "Petrov");

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);
        doReturn(existingEmployee).when(employeeRepository).save(existingEmployee);

        var responseEntity = service.editEmployee(employeeMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingEmployee, responseEntity.getBody());
        assertEquals("Petr", existingEmployee.getName());
        assertEquals("Petrov", existingEmployee.getLastName());
    }

    @Test
    void editEmployee_WithEmptyName_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put(Employee.NAME_FIELD, "");
        employeeMap.put(Employee.LAST_NAME_FIELD, "Petrov");

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.editEmployee(employeeMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'name' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void editEmployee_WithInvalidName_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put(Employee.NAME_FIELD, "This is a very long name that exceeds the maximum length");
        employeeMap.put(Employee.LAST_NAME_FIELD, "Petrov");

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.editEmployee(employeeMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'name' field length should be less then 20 characters", errorResponse.get("error"));
    }

    @Test
    void editEmployee_WithEmptyLastName_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put(Employee.NAME_FIELD, "Petr");
        employeeMap.put(Employee.LAST_NAME_FIELD, "");

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.editEmployee(employeeMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'lastName' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void editEmployee_WithInvalidLastName_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
        Map<String, Object> employeeMap = new HashMap<>();
        employeeMap.put(Employee.NAME_FIELD, "Petr");
        employeeMap.put(Employee.LAST_NAME_FIELD, "This last name is too long to fit within the maximum allowed length of last names and checks the operation of the method");

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.editEmployee(employeeMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'lastName' field length should be less then 100 characters", errorResponse.get("error"));
    }

    @Test
    void removeEmployee_WithExistingEmployee_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .build();

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.remove(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, times(1)).deleteById(id);
    }

    @Test
    void removeEmployee_WithExistingIssueInEmployee_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingEmployee = Employee.builder()
                .id(id)
                .name("Ivan")
                .lastName("Ivanov")
                .issues(List.of(Issue.builder()
                        .title("title")
                        .description("description")
                        .build()
                        )      )
                .build();

        doReturn(Optional.of(existingEmployee)).when(employeeRepository).findById(id);

        var responseEntity = service.remove(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());
        assertNotNull(responseEntity.getBody());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The employee has active issues", errorResponse.get("error"));

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, never()).deleteById(id);
    }

    @Test
    void removeEmployee_WithNotExistingEmployee_ReturnsNotFoundResponseEntity() {
        var id = 1L;

        doReturn(Optional.empty()).when(employeeRepository).findById(id);

        var exception = assertThrows(ResponseStatusException.class, () -> service.remove(id));

        assertNotNull(exception);
        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());

        verify(employeeRepository, times(1)).findById(id);
        verify(employeeRepository, never()).deleteById(id);
    }
}