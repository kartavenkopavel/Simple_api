package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Comment;
import simple.dto.CommentDto;
import simple.entity.Post;
import simple.repository.CommentRepository;
import simple.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    PostRepository postRepository;

    @Autowired
    CommentRepository commentRepository;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Comment comment) {
        if (comment.getText() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Text field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Post post = postRepository.findById(comment.getPost().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        comment.setPost(post);

        Comment savedComment = commentRepository.save(comment);

        CommentDto commentDto = CommentDto.builder()
                .id(savedComment.getId())
                .text(savedComment.getText())
                .likes(savedComment.getLikes())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(commentDto);
    }

    @GetMapping("/post/{id}/list")
    public List<CommentDto> getPostsByEmployeeId(@PathVariable Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Comment> comments = commentRepository.findByPost(post);

        return comments.stream()
                .map(CommentDto::new)
                .collect(Collectors.toList());
    }
}
