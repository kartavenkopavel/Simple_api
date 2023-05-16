package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.repository.EmployeeRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static simple.service.ServiceUtils.*;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity<Object> createEmployee(Employee employee) {
        if (employee.getName() == null || employee.getLastName() == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' and 'lastName' fields is required"));
        }
        if (employee.getName().length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field must have at least then 1 character"));
        }
        if (employee.getLastName().length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field must have at least then 1 character"));
        }
        if (employee.getName().length() > 20) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field length should be less then 20 characters"));
        }
        if (employee.getLastName().length() > 100) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field length should be less then 100 characters"));
        }

        employee.setId(null);
        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    public ResponseEntity<List<Employee>> getEmployeeList() {
        return ResponseEntity.ok().body(employeeRepository.findAll());
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Object> updateEmployee(Employee employee) {
        getEmployeeById(employee.getId());

        if (employee.getName() == null || employee.getLastName() == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' and 'lastName' fields is required"));
        }
        if (employee.getName().length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field must have at least then 1 character"));
        }
        if (employee.getLastName().length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field must have at least then 1 character"));
        }
        if (employee.getName().length() > 20) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field length should be less then 20 characters"));
        }
        if (employee.getLastName().length() > 100) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field length should be less then 100 characters"));
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.OK).body(savedEmployee);
    }

    public ResponseEntity<Object> editEmployee(Map<String, Object> employeeMap, Long id) {
        Employee employee = getEmployeeById(id);

        String name = (String) employeeMap.get(Employee.NAME_FIELD);
        if (name != null && name.length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field must have at least then 1 character"));
        }
        if (name != null && name.length() > 20){
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'name' field length should be less then 20 characters"));
        }

        String lastName = (String) employeeMap.get(Employee.LAST_NAME_FIELD);
        if (name != null && lastName.length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field must have at least then 1 character"));
        }
        if (lastName != null && lastName.length() > 100) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'lastName' field length should be less then 100 characters"));
        }

        for (String key : employeeMap.keySet()) {
            switch (key) {
                case Employee.NAME_FIELD:
                    employee.setName((String) employeeMap.get(Employee.NAME_FIELD));
                    break;

                case Employee.LAST_NAME_FIELD:
                    employee.setLastName((String) employeeMap.get(Employee.LAST_NAME_FIELD));
                    break;
            }
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.OK).body(savedEmployee);
    }

    public ResponseEntity<Void> remove(Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
