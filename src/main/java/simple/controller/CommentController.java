package simple.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Comment;
import simple.swagger.schema.request.CommentRequest;
import simple.service.CommentService;

import java.util.List;

@RestController
@RequestMapping("/api/comment")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @PostMapping("/create")
    @Operation(
            tags = {"Comment"},
            operationId = "comment",
            summary = "Create new comment",
            description = "Before creating a comment, you need to create a issue",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
            content = @Content(schema = @Schema(implementation = CommentRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
                    )
            }
    )
    public ResponseEntity<?> create(@RequestBody Comment comment) {
       return commentService.createComment(comment);
    }

    @GetMapping("/issue/{id}/list")
    @Operation(
            tags = {"Comment"},
            operationId = "id",
            summary = "Getting comments on a issue",
            parameters = {@Parameter(name = "id", example = "1")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Comment.class))
                    )
            }
    )
    public List<Comment> getIssueComments(@PathVariable Long id) {
        return commentService.getIssueComments(id);
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"Comment"},
            operationId = "id",
            summary = "Delete comment",
            parameters = {@Parameter(name = "id", example = "1")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    )
            }
    )
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        return commentService.deleteComment(id);
    }
}
