package simple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.entity.Employee;
import simple.entity.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByEmployee(Employee employee);
}
