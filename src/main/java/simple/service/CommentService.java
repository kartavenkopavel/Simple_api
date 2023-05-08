package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import simple.entity.Comment;
import simple.entity.Issue;
import simple.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CommentService {

    @Autowired
    private IssueService issueService;

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<?> createComment(Comment comment) {
        Map<String, String> errorResponse = new HashMap<>();
        if (comment.getText() == null) {
            errorResponse.put("error", "The 'text' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (comment.getText().length() > 400) {
            errorResponse.put("error", "The 'text' field length should be less than 400 characters");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (comment.getIssue() == null) {
            errorResponse.put("error", "The 'issue' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Issue issue = issueService.getIssueById(comment.getIssue().getId());
        comment.setIssue(issue);
        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    public List<Comment> getIssueComments(Long id) {
        Issue issue = issueService.getIssueById(id);
        return commentRepository.findByIssue(issue);
    }

    public ResponseEntity<Void> deleteComment(Long id) {
        Optional<Comment> optionalComment = commentRepository.findById(id);
        if (optionalComment.isPresent()) {
            commentRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
