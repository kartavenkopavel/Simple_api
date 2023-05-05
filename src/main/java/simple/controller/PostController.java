package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.dto.PostDto;
import simple.entity.Post;
import simple.service.PostService;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostService postService;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        return postService.createPost(post);
    }

    @GetMapping("/user/{id}/list")
    public List<PostDto> getUserPosts(@PathVariable Long id) {
        return postService.getUserPosts(id);
    }

    @GetMapping("/search/{query}")
    public List<Post> search(@PathVariable String query) {
        return postService.search(query);
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postService.getPostById(id);
    }

    @GetMapping("/list")
    public List<PostDto> getList() {
        return postService.getPostList();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return postService.remove(id);
    }
}
