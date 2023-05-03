package simple.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simple.Repository.EmployeeRepository;
import simple.Entity.Employee;

import java.util.List;
import java.util.Map;

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
    public Employee getEmployeeById(@PathVariable Long id) {
        return employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @PostMapping("/create")
    public Employee createEmployee(@RequestBody Employee employee) {
        employee.setId(null);
        return employeeRepository.save(employee);
    }

    @PutMapping("/update")
    public Employee update(@RequestBody Employee employee) {
        getEmployeeById(employee.getId());
        return employeeRepository.save(employee);
    }

    @PatchMapping("/edit/{id}")
    public Employee edit(@RequestBody Map<String, Object> employeeMap, @PathVariable Long id) {
        Employee user = getEmployeeById(id);

        for (String key : employeeMap.keySet()) {
            switch (key) {
                case Employee.NAME_FIELD:
                    user.setName((String)employeeMap.get(Employee.NAME_FIELD));
                    break;

                case Employee.LAST_NAME_FIELD:
                    user.setName((String)employeeMap.get(Employee.LAST_NAME_FIELD));
                    break;
            }
        }

        return employeeRepository.save(user);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        employeeRepository.deleteById(id);
    }
}
