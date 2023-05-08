package simple.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import simple.entity.Comment;
import simple.entity.Issue;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByIssue(Issue issue);
}
