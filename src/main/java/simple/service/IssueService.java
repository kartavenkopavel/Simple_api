package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.entity.Issue;
import simple.repository.IssueRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private EmployeeService employeeService;

    public ResponseEntity<Object> createIssue(Issue issue) {
        if (issue.getTitle() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'title' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }
        if (issue.getEmployee() == null || issue.getEmployee().getId() == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'id' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        Employee employee = employeeService.getEmployeeById(issue.getEmployee().getId());
        issue.setEmployee(employee);

        Issue savedIssue = issueRepository.save(issue);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedIssue);
    }

    public Issue getIssueById(Long id) {
        return issueRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    public ResponseEntity<Object> editIssueDescription(Map<String, Object> issueMap, Long id) {
        Issue issue = getIssueById(id);

        if (issueMap.get(Issue.TITLE_FIELD) == null) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "The 'title' field is required");
            return ResponseEntity.badRequest().body(errorResponse);
        }

        for (String key : issueMap.keySet()) {
            switch (key) {
                case Issue.TITLE_FIELD:
                    issue.setTitle((String) issueMap.get(Issue.TITLE_FIELD));
                    break;
                case Issue.DESCRIPTION_FIELD:
                    issue.setDescription((String) issueMap.get(Issue.DESCRIPTION_FIELD));
                    break;
            }
        }

        Issue savedIssue = issueRepository.save(issue);
        return ResponseEntity.status(HttpStatus.OK).body(savedIssue);
    }

    public List<Issue> getEmployeeIssues(Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        return issueRepository.findByEmployee(employee);
    }

    public List<Issue> search(String query) {
        List<Issue> issueList = issueRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();

        issueList = issueList.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    post.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getName().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getLastName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        return issueList;
    }

    public List<Issue> getIssueList() {
        return issueRepository.findAll();
    }

    public ResponseEntity<Void> remove(Long id) {
        Optional<Issue> optionalPost = issueRepository.findById(id);
        if (optionalPost.isPresent()) {
            issueRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
