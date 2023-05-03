package simple.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.Entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
}
