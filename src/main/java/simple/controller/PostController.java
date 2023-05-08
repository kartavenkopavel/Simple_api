package simple.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Post;
import simple.service.PostService;
import simple.swagger.schema.request.PostRequest;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    @Operation(
            tags = {"Post"},
            operationId = "post",
            summary = "Create new post",
            description = "Before creating a post, you need to create a employee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = PostRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    )
            }
    )
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/user/{id}/list")
    @Operation(
            tags = {"Post"},
            operationId = "id",
            summary = "Getting post list by employee id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    )
            }
    )
    public List<Post> getUserPosts(@PathVariable Long id) {
        return postService.getUserPosts(id);
    }

    @GetMapping("/search/{query}")
    @Operation(
            tags = {"Post"},
            operationId = "query",
            summary = "Getting some post or employee by query",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    )
            }
    )
    public List<Post> search(@PathVariable String query) {
        return postService.search(query);
    }

    @GetMapping("/{id}")
    @Operation(
            tags = {"Post"},
            operationId = "id",
            summary = "Getting post by id",
            parameters = {@Parameter(name = "id", example = "1")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    )
            }
    )
    public Post getById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/list")
    @Operation(
            tags = {"Post"},
            summary = "Getting posts list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Post.class))
                    )
            }
    )
    public List<Post> getList() {
        return postService.getPostList();
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"Post"},
            summary = "Delete post",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return postService.remove(id);
    }
}
