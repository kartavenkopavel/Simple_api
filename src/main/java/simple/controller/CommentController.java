package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Comment;
import simple.dto.CommentDto;
import simple.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    public ResponseEntity<?> create(@RequestBody Comment comment) {
       return commentService.createComment(comment);
    }

    @GetMapping("/post/{id}/list")
    public List<CommentDto> getPostComments(@PathVariable Long id) {
        return commentService.getPostComments(id);
    }
}
