package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simple.repository.EmployeeRepository;
import simple.entity.Employee;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("api/user")
public class EmployeeController {

    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/list")
    public List<Employee> getList() {
        return employeeRepository.findAll();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployee(@RequestBody Employee employee) {
        if (employee.getName() == null || employee.getLastName() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Name and Last name fields is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        employee.setId(null);
        Employee savedEmployee = employeeRepository.save(employee);
        return ResponseEntity.created(URI.create("/create/" + savedEmployee.getId()))
                .body(savedEmployee);
    }

    @PutMapping("/update")
    public Employee update(@RequestBody Employee employee) {
        getById(employee.getId());
        return employeeRepository.save(employee);
    }

    @PatchMapping("/edit/{id}")
    public Employee edit(@RequestBody Map<String, Object> employeeMap, @PathVariable Long id) {
        Employee user = getById(id);

        for (String key : employeeMap.keySet()) {
            switch (key) {
                case Employee.NAME_FIELD:
                    user.setName((String) employeeMap.get(Employee.NAME_FIELD));
                    break;

                case Employee.LAST_NAME_FIELD:
                    user.setName((String) employeeMap.get(Employee.LAST_NAME_FIELD));
                    break;
            }
        }

        return employeeRepository.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Employee> optionalEmployee = employeeRepository.findById(id);
        if (optionalEmployee.isPresent()){
            employeeRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
