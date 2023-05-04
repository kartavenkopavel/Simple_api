package simple.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simple.dto.PostDto;
import simple.entity.Employee;
import simple.entity.Post;
import simple.repository.PostRepository;
import simple.repository.EmployeeRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/create")
    public ResponseEntity<?> createPost(@RequestBody Post post) {
        if (post.getTitle() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Title field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (post.getEmployee() == null || post.getEmployee().getId() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "User field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Employee employee = employeeRepository.findById(post.getEmployee().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        post.setEmployee(employee);

        Post savedPost = postRepository.save(post);

        PostDto postDto = PostDto.builder()
                .id(savedPost.getId())
                .title(savedPost.getTitle())
                .description(savedPost.getDescription())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(postDto);
    }

    @GetMapping("/user/{id}/list")
    public List<PostDto> getPostsByEmployeeId(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        List<Post> listPosts = postRepository.findByEmployee(employee);

        return listPosts.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/search/{query}")
    public List<Post> search(@PathVariable String query) {
        List<Post> posts = postRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();

        posts = posts.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                        post.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                        post.getEmployee().getName().toLowerCase().contains(lowerCaseQuery) ||
                        post.getEmployee().getLastName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        return posts;
    }

    @GetMapping("/{id}")
    public Post getById(@PathVariable Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/list")
    public List<PostDto> getList() {
        List<Post> listPosts = postRepository.findAll();

        return listPosts.stream()
                .map(PostDto::new)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
