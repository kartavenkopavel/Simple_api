package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import simple.dto.CommentDto;
import simple.entity.Comment;
import simple.entity.Post;
import simple.repository.CommentRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentService {

    @Autowired
    private PostService postService;

    @Autowired
    private CommentRepository commentRepository;

    public ResponseEntity<?> createComment(Comment comment) {
        if (comment.getText() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Text field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Post post = postService.getPostById(comment.getPost().getId());
        comment.setPost(post);
        Comment savedComment = commentRepository.save(comment);

        CommentDto commentDto = CommentDto.builder()
                .id(savedComment.getId())
                .text(savedComment.getText())
                .likes(savedComment.getLikes())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    public List<CommentDto> getPostComments(Long id) {
        Post post = postService.getPostById(id);
        List<Comment> commentList = commentRepository.findByPost(post);

        return commentList.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
