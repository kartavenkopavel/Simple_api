package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import simple.entity.Comment;
import simple.entity.Issue;
import simple.repository.CommentRepository;
import simple.repository.IssueRepository;

import java.util.List;
import java.util.Optional;

import static simple.service.ServiceUtils.createErrorResponse;

@Service
public class CommentService {

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<Object> createComment(Long issueId, Comment comment) {
        if (issueId == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'issueId' param is required"));
        }
        Issue issue = issueService.getIssueById(issueId);

        if (comment.getText() == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'text' field is required"));
        }
        if (comment.getText().length() > 400) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'text' field length should be less than 400 characters"));
        }

        comment.setIssue(issue);
        Comment savedComment = commentRepository.save(comment);
        issue.addComment(savedComment);
        issueRepository.save(issue);

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
