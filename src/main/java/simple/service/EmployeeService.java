package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.repository.EmployeeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity<?> createEmployee(Employee employee) {
        Map<String, String> errorResponse = new HashMap<>();
        if (employee.getName() == null || employee.getLastName() == null) {
            errorResponse.put("error", "The 'name' and 'lastName' fields is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (employee.getName().length() > 20) {
            errorResponse.put("error", "The 'name' field length should be less then 20 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (employee.getName().length() > 100) {
            errorResponse.put("error", "The 'lastName' field length should be less then 100 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        employee.setId(null);
        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedEmployee);
    }

    public List<Employee> getEmployeeList() {
        return employeeRepository.findAll();
    }

    public Employee getEmployeeById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<?> updateEmployee(Employee employee) {
        getEmployeeById(employee.getId());

        Map<String, String> errorResponse = new HashMap<>();
        if (employee.getName().length() > 20) {
            errorResponse.put("error", "The 'name' field length should be less then 20 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (employee.getName().length() > 100) {
            errorResponse.put("error", "The 'lastName' field length should be less then 100 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Employee savedEmployee = employeeRepository.save(employee);

        return ResponseEntity.status(HttpStatus.OK).body(savedEmployee);
    }

    public ResponseEntity<?> editEmployee(Map<String, Object> employeeMap, Long id) {
        Employee employee = getEmployeeById(id);

        Map<String, String> errorResponse = new HashMap<>();
        if (!employeeMap.get(Employee.NAME_FIELD).equals(10)) {
            errorResponse.put("error", "The 'name' field length should be less then 20 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (!employeeMap.get(Employee.LAST_NAME_FIELD).equals(100)) {
            errorResponse.put("error", "The 'lastName' field length should be less then 100 characters");
            return ResponseEntity.badRequest().body(errorResponse);
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
