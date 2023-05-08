package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import simple.entity.Comment;
import simple.entity.Post;
import simple.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CommentService {

    @Autowired
    private PostService postService;

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

        Post post = postService.getPostById(comment.getPost().getId());
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        return ResponseEntity.status(HttpStatus.CREATED).body(savedComment);
    }

    public List<Comment> getPostComments(Long id) {
        Post post = postService.getPostById(id);
        return commentRepository.findByPost(post);
    }
}
