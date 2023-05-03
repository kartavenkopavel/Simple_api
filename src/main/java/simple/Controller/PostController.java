package simple.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import simple.Entity.Employee;
import simple.Entity.Post;
import simple.Repository.PostRepository;
import simple.Repository.EmployeeRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/post")
public class PostController {

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @PostMapping("/create")
    public Post createPost(@RequestBody Post post) {
        Employee employee = employeeRepository.findById(post.getEmployee().getId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        post.setEmployee(employee);
        return postRepository.save(post);
    }

    @GetMapping("/user/{id}/list")
    public List<Post> getPostsByEmployeeId(@PathVariable Long id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        return postRepository.findByEmployee(employee);
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
    public List<Post> getList() {
        return postRepository.findAll();
    }

    @DeleteMapping("/{id}")
    public void remove(@PathVariable Long id) {
        postRepository.deleteById(id);
    }
}
