package simple.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import simple.entity.Issue;
import simple.service.IssueService;
import simple.swagger.schema.request.IssueRequest;

import java.util.List;

@RestController
@RequestMapping("api/post")
public class IssueController {

    @Autowired
    private IssueService issueService;

    @PostMapping("/create")
    @Operation(
            tags = {"Issue"},
            operationId = "issue",
            summary = "Create new issue",
            description = "Before creating a issue, you need to create a employee",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    content = @Content(schema = @Schema(implementation = IssueRequest.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "Created",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Issue.class))
                    )
            }
    )
    public ResponseEntity<?> createIssue(@RequestBody Issue issue) {
        return issueService.createIssue(issue);
    }

    @GetMapping("/employee/{id}/list")
    @Operation(
            tags = {"Issue"},
            operationId = "id",
            summary = "Getting issue list by employee id",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Issue.class))
                    )
            }
    )
    public List<Issue> getEmployeePosts(@PathVariable Long id) {
        return issueService.getEmployeeIssues(id);
    }

    @GetMapping("/search/{query}")
    @Operation(
            tags = {"Issue"},
            operationId = "query",
            summary = "Getting some issue or employee by query",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Issue.class))
                    )
            }
    )
    public List<Issue> search(@PathVariable String query) {
        return issueService.search(query);
    }

    @GetMapping("/{id}")
    @Operation(
            tags = {"Issue"},
            operationId = "id",
            summary = "Getting issue by id",
            parameters = {@Parameter(name = "id", example = "1")},
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Issue.class))
                    )
            }
    )
    public Issue getById(@PathVariable Long id) {
        return issueService.getIssueById(id);
    }

    @GetMapping("/list")
    @Operation(
            tags = {"Issue"},
            summary = "Getting issues list",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK",
                            content = @Content(mediaType = "application/json", schema = @Schema(implementation = Issue.class))
                    )
            }
    )
    public List<Issue> getList() {
        return issueService.getIssueList();
    }

    @DeleteMapping("/{id}")
    @Operation(
            tags = {"Issue"},
            summary = "Delete issue",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "OK"
                    )
            }
    )
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return issueService.remove(id);
    }
}
