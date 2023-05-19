package simple.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import simple.entity.Comment;
import simple.entity.Issue;
import simple.repository.CommentRepository;
import simple.repository.IssueRepository;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {

    @Mock
    CommentRepository commentRepository;

    @InjectMocks
    CommentService commentService;

    @Mock
    IssueService issueService;

    @Mock
    IssueRepository issueRepository;

    @Test
    void createComment_WithValidData_ReturnsCreatedResponseEntity() {
        var issueId = 1L;
        var comment = Comment.builder()
                .text("Text")
                .likes(1)
                .build();
        Issue issue = new Issue();

        doReturn(issue).when(issueService).getIssueById(issueId);
        doReturn(comment).when(commentRepository).save(comment);
        doReturn(issue).when(issueRepository).save(issue);

        var responseEntity = commentService.createComment(issueId, comment);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(comment, responseEntity.getBody());

        verify(commentRepository, times(1)).save(comment);
        verify(issueRepository, times(1)).save(issue);
    }

    @Test
    void createComment_WithMissingIssueId_ReturnsBadRequestResponseEntity() {
        Long issueId = null;
        var comment = Comment.builder()
                .text("Text")
                .likes(1)
                .build();

        var responseEntity = commentService.createComment(issueId, comment);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'issueId' param is required", errorResponse.get("error"));

        verify(commentRepository, never()).save(comment);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void createComment_WithMissingText_ReturnsBadRequestResponseEntity() {
        Long issueId = 1L;
        var comment = Comment.builder()
                .likes(1)
                .build();
        Issue issue = new Issue();

        var responseEntity = commentService.createComment(issueId, comment);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'text' field is required", errorResponse.get("error"));

        verify(commentRepository, never()).save(comment);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void createComment_WithEmptyText_ReturnsBadRequestResponseEntity() {
        Long issueId = 1L;
        var comment = Comment.builder()
                .text("")
                .likes(1)
                .build();
        Issue issue = new Issue();

        var responseEntity = commentService.createComment(issueId, comment);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'text' field must have at least then 1 character", errorResponse.get("error"));

        verify(commentRepository, never()).save(comment);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void createComment_WithInvalidText_ReturnsBadRequestResponseEntity() {
        Long issueId = 1L;
        var comment = Comment.builder()
                .text("A".repeat(401))
                .likes(1)
                .build();
        Issue issue = new Issue();

        var responseEntity = commentService.createComment(issueId, comment);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'text' field length should be less than 400 characters", errorResponse.get("error"));

        verify(commentRepository, never()).save(comment);
        verify(issueRepository, never()).save(any());
    }

    @Test
    void getIssueComments_WithExistingComments_ReturnsOkResponseEntity() {
        var issueId = 1L;
        var commentList = List.of(
                Comment.builder()
                        .id(1L)
                        .text("Text")
                        .likes(1)
                        .build(),
                Comment.builder()
                        .id(2L)
                        .text("Text")
                        .likes(1)
                        .build()
        );
        var existingIssue = Issue.builder()
                .id(issueId)
                .title("Title")
                .description("Description")
                .build();

        doReturn(existingIssue).when(issueService).getIssueById(issueId);
        doReturn(commentList).when(commentRepository).findByIssue(existingIssue);

        var responseEntity = commentService.getIssueComments(issueId);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals(commentList, responseEntity.getBody());

        verify(issueService, times(1)).getIssueById(issueId);
        verify(commentRepository, times(1)).findByIssue(existingIssue);
    }

    @Test
    void deleteComment_WithExistingComment_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingComment = Comment.builder()
                .id(id)
                .text("Text")
                .likes(1)
                .build();

        doReturn(Optional.of(existingComment)).when(commentRepository).findById(id);

        var responseEntity = commentService.deleteComment(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(1)).deleteById(id);
    }

    @Test
    void deleteComment_WithNonExistingComment_ReturnsOkResponseEntity() {
        var id = 1L;

        doReturn(Optional.empty()).when(commentRepository).findById(id);

        var responseEntity = commentService.deleteComment(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(commentRepository, times(1)).findById(id);
        verify(commentRepository, times(0)).deleteById(id);
    }
}