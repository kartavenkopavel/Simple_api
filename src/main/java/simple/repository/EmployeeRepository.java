package simple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
