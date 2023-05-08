package simple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.entity.Employee;
import simple.entity.Issue;

import java.util.List;


public interface IssueRepository extends JpaRepository<Issue, Long> {
    List<Issue> findByEmployee(Employee employee);
}
