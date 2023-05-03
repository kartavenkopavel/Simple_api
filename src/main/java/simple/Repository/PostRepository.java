package simple.Repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.Entity.Employee;
import simple.Entity.Post;

import java.util.List;


public interface PostRepository extends JpaRepository<Post, Long> {
    List<Post> findByEmployee(Employee employee);
}
