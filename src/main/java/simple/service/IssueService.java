package simple.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.entity.Issue;
import simple.repository.IssueRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static simple.service.ServiceUtils.*;

@Service
public class IssueService {

    @Autowired
    private IssueRepository issueRepository;

    @Autowired
    private EmployeeService employeeService;

    public ResponseEntity<Object> createIssue(Issue issue) {
        if (issue.getTitle() == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'title' field is required"));
        }
        if (issue.getTitle().length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'title' field must have at least then 1 character"));
        }
        if (issue.getTitle().length() > 100) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'title' field length should be less then 100 characters"));
        }
        if (issue.getDescription() != null && issue.getDescription().length() > 1000) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'description' field length should be less then 1000 characters"));
        }
        if (issue.getEmployee() == null || issue.getEmployee().getId() == null) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The employee 'id' field is required"));
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

    public ResponseEntity<Object> editIssue(Map<String, Object> issueMap, Long id) {
        Issue issue = getIssueById(id);

        String title = (String) issueMap.get(Issue.TITLE_FIELD);
        if (title.length() < 1) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'title' field must have at least then 1 character"));
        }
        if (title.length() > 100) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'title' field length should be less then 100 characters"));
        }

        String description = (String) issueMap.get(Issue.DESCRIPTION_FIELD);
        if (description != null && description.length() > 1000) {
            return ResponseEntity.badRequest()
                    .body(createErrorResponse("The 'description' field length should be less then 1000 characters"));
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

    public ResponseEntity<List<Issue>> getEmployeeIssues(Long id) {
        Employee employee = employeeService.getEmployeeById(id);

        return ResponseEntity.status(HttpStatus.OK).body(issueRepository.findByEmployee(employee));
    }

    public ResponseEntity<List<Issue>> search(String query) {
        List<Issue> issueList = issueRepository.findAll();
        String lowerCaseQuery = query.toLowerCase();

        issueList = issueList.stream()
                .filter(post -> post.getTitle().toLowerCase().contains(lowerCaseQuery) ||
                    post.getDescription().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getName().toLowerCase().contains(lowerCaseQuery) ||
                    post.getEmployee().getLastName().toLowerCase().contains(lowerCaseQuery))
                .collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(issueList);
    }

    public ResponseEntity<List<Issue>> getIssueList() {
        return ResponseEntity.status(HttpStatus.OK).body(issueRepository.findAll());
    }

    public ResponseEntity<Void> removeIssue(Long id) {
        Optional<Issue> optionalPost = issueRepository.findById(id);
        if (optionalPost.isPresent()) {
            issueRepository.deleteById(id);
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
