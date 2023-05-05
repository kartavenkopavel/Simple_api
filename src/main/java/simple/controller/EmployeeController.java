package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Employee;
import simple.service.EmployeeService;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("api/user")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @GetMapping("/list")
    public List<Employee> getList() {
        return employeeService.getUserList();
    }

    @GetMapping("/{id}")
    public Employee getById(@PathVariable Long id) {
        return employeeService.getUserById(id);
    }

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Employee employee) {
        return employeeService.createUser(employee);
    }

    @PutMapping("/update")
    public Employee update(@RequestBody Employee employee) {
        return employeeService.updateUser(employee);
    }

    @PatchMapping("/edit/{id}")
    public Employee edit(@RequestBody Map<String, Object> employeeMap, @PathVariable Long id) {
        return employeeService.editUser(employeeMap, id);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return employeeService.remove(id);
    }
}
