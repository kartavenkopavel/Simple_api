package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.entity.Post;
import simple.repository.PostRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmployeeService employeeService;

    public ResponseEntity<?> createPost(Post post) {
        if (post.getTitle() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'title' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (post.getEmployee() == null || post.getEmployee().getId() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'id' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Employee employee = employeeService.getUserById(post.getEmployee().getId());
        post.setEmployee(employee);

        Post savedPost = postRepository.save(post);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedPost);
    }

    public Post getPostById(Long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public List<Post> getUserPosts(Long id) {
        Employee employee = employeeService.getUserById(id);

        return postRepository.findByEmployee(employee);
    }

    public List<Post> search(String query) {
        List<Post> postList = postRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();

        postList = postList.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    post.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getName().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getLastName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        return postList;
    }

    public List<Post> getPostList() {
        return postRepository.findAll();
    }

    public ResponseEntity<Void> remove(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if (optionalPost.isPresent()) {
            postRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
