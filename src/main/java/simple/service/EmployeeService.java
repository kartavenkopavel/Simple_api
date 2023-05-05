package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.repository.EmployeeRepository;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    public ResponseEntity<?> createUser(Employee employee) {
        if (employee.getName() == null || employee.getLastName() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'name' and 'lastName' fields is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        employee.setId(null);
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.created(URI.create("/create/" + savedEmployee.getId()))
                .body(savedEmployee);
    }

    public List<Employee> getUserList() {
        return employeeRepository.findAll();
    }

    public Employee getUserById(Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public Employee updateUser(Employee employee) {
        getUserById(employee.getId());
        return employeeRepository.save(employee);
    }

    public Employee editUser(Map<String, Object> userMap, Long id) {
        Employee employee = getUserById(id);

        for (String key : userMap.keySet()) {
            switch (key) {
                case Employee.NAME_FIELD:
                    employee.setName((String) userMap.get(Employee.NAME_FIELD));
                    break;

                case Employee.LAST_NAME_FIELD:
                    employee.setName((String) userMap.get(Employee.LAST_NAME_FIELD));
                    break;
            }
        }

        return employeeRepository.save(employee);
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
