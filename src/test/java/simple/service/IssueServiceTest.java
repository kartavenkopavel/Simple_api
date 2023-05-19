package simple.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;
import simple.entity.Employee;
import simple.entity.Issue;
import simple.repository.EmployeeRepository;
import simple.repository.IssueRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class IssueServiceTest {

    @Mock
    IssueRepository issueRepository;

    @InjectMocks
    IssueService issueService;

    @Mock
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    private Employee getExistingEmployee() {
        return Employee.builder()
                .id(1L)
                .name("Ivan")
                .lastName("Ivanov")
                .build();
    }

    @Test
    void createIssue_WithValidData_ReturnsCreateResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .title("Title")
                .description("description")
                .build();
        Employee employee = new Employee();

        doReturn(employee).when(employeeService).getEmployeeById(employeeId);
        doReturn(issue).when(issueRepository).save(issue);
        doReturn(employee).when(employeeRepository).save(employee);

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(issue, responseEntity.getBody());

        verify(issueRepository, times(1)).save(issue);
        verify(employeeRepository, times(1)).save(employee);
    }

    @Test
    void createIssue_WithMissingDescription_ReturnsCreateResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .title("Title")
                .build();
        var savedIssue = Issue.builder()
                .id(1L)
                .title("Title")
                .build();

        doReturn(getExistingEmployee()).when(employeeService).getEmployeeById(employeeId);
        doReturn(savedIssue).when(issueRepository).save(issue);

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.CREATED, responseEntity.getStatusCode());
        assertEquals(savedIssue, responseEntity.getBody());
    }

    @Test
    void createIssue_WithMissingTitle_ReturnsBadRequestResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .description("description")
                .build();

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'title' field is required", errorResponse.get("error"));
    }

    @Test
    void createIssue_WithEmptyTitle_ReturnsBadRequestResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .title("")
                .description("description")
                .build();

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'title' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void createIssue_WithInvalidTitle_ReturnsBadRequestResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .title("Welcome to a world where limitless possibilities await, where the boundless realm of" +
                        " imagination intertwines with reality, and where dreams blossom into tangible achievements")
                .description("description")
                .build();

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'title' field length should be less then 100 characters", errorResponse.get("error"));
    }

    @Test
    void createIssue_WithInvalidDescription_ReturnsBadRequestResponseEntity() {
        var employeeId = 1L;
        var issue = Issue.builder()
                .title("Title")
                .description("Amidst the verdant splendor of an ancient and mystical forest, a breathtaking "
                        + "creature with resplendent wings unfurled itself from the mighty embrace of an age-old oak tree."
                        + " Its iridescent beauty cast a spell of enchantment upon all who beheld it, evoking "
                        + "a sense of awe and wonder that transcended mortal realms. As the golden orb of "
                        + "the sun dipped beneath the horizon, the majestic being took flight, tracing a celestial"
                        + " path through the ethereal canvas of the night sky. A luminescent trail of stardust cascaded"
                        + " in its wake, painting the heavens with a tapestry of shimmering lights. The soft caress"
                        + " of the wind carried the faint whispers of its melodious song, a haunting melody that wove"
                        + " its way into the deepest recesses of the heart, igniting a profound longing for the magic"
                        + " it embodied. Throughout the annals of time, legends and myths had been woven, speaking"
                        + " of the creature's miraculous ability to grant wishes and manifest miracles,"
                        + " beckoning pilgrims from far and wide to seek its divine presence. With each passing moment,"
                        + " the creature's powers grew stronger, infused with an energy that transcended mortal"
                        + " comprehension. It became a living conduit for the dreams and aspirations of all who dared"
                        + " to believe, spinning a tapestry of dreams and enchantment that forever altered the destinies"
                        + " of those touched by its ethereal grace")
                .build();

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'description' field length should be less then 1000 characters", errorResponse.get("error"));
    }

    @Test
    void createIssue_WithMissingEmployee_ReturnsBadRequestResponseEntity() {
        Long employeeId = null;
        var issue = Issue.builder()
                .title("Title")
                .description("description")
                .build();

        var responseEntity = issueService.createIssue(employeeId, issue);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        verify(issueRepository, never()).save(issue);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void getIssueById_WithExistingIssue_ReturnsIssue() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("description")
                .build();

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);

        var result = issueService.getIssueById(id);

        assertNotNull(result);
        assertEquals(existingIssue, result);
    }

    @Test
    void getIssueById_WithMissingIssue_ThrowsResponseStatusExceptionWithNotFoundStatus() {
        var id = 1L;

        doReturn(Optional.empty()).when(issueRepository).findById(id);

        assertThrows(ResponseStatusException.class, () -> issueService.getIssueById(id), "Not Found");
        try {
            issueService.getIssueById(id);
        } catch (ResponseStatusException e) {
            assertEquals(HttpStatus.NOT_FOUND, e.getStatus());
        }
    }

    @Test
    void getIssueList_WithValidData_ReturnsOkResponseEntity() {
        var issueList = List.of(
                Issue.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .build(),
                Issue.builder()
                        .id(2L)
                        .title("Title")
                        .description("Description")
                        .build()
        );

        doReturn(issueList).when(issueRepository).findAll();

        var responseEntity = issueService.getIssueList();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(issueList, responseEntity.getBody());
    }

    @Test
    void getIssueList_ReturnsEmptyResponseEntityBody() {
        var issueList = List.of();

        doReturn(issueList).when(issueRepository).findAll();

        var responseEntity = issueService.getIssueList();

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void search_WithContainsQueryInIssue_ReturnsOkResponseEntity() {
        var query = "topic";
        var issueList = List.of(
                Issue.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .employee(getExistingEmployee())
                        .build(),
                Issue.builder()
                        .id(2L)
                        .title("Topic")
                        .description("Description")
                        .employee(getExistingEmployee())
                        .build()
        );

        doReturn(issueList).when(issueRepository).findAll();

        var responseEntity = issueService.search(query);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(1, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals(issueList.get(1), responseEntity.getBody().get(0));
    }

    @Test
    void search_WithContainsQueryInEmployee_ReturnsOkResponseEntity() {
        var query = "ivan";
        var issueList = List.of(
                Issue.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .employee(getExistingEmployee())
                        .build(),
                Issue.builder()
                        .id(2L)
                        .title("Topic")
                        .description("Description")
                        .employee(getExistingEmployee())
                        .build()
        );

        doReturn(issueList).when(issueRepository).findAll();

        var responseEntity = issueService.search(query);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals(issueList, responseEntity.getBody());
    }

    @Test
    void search_WithNoContainsQuery_ReturnsOkResponseEntity() {
        var query = "text";
        var issueList = List.of(
                Issue.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .build(),
                Issue.builder()
                        .id(2L)
                        .title("Topic")
                        .description("Description")
                        .build()
        );
        var emptyList = List.of();

        doReturn(emptyList).when(issueRepository).findAll();

        var responseEntity = issueService.search(query);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).isEmpty());
    }

    @Test
    void getEmployeeIssues_WhenExistingIssues_ReturnsOkResponseEntityList() {
        var id = 1L;
        var issueList = List.of(
                Issue.builder()
                        .id(1L)
                        .title("Title")
                        .description("Description")
                        .build(),
                Issue.builder()
                        .id(2L)
                        .title("Topic")
                        .description("Description")
                        .build()
        );

        doReturn(getExistingEmployee()).when(employeeService).getEmployeeById(id);
        doReturn(issueList).when(issueRepository).findByEmployee(getExistingEmployee());

        var responseEntity = issueService.getEmployeeIssues(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(2, Objects.requireNonNull(responseEntity.getBody()).size());
        assertEquals(issueList, responseEntity.getBody());
    }

    @Test
    void editIssue_WithValidData_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("Description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Title edited");
        issueMap.put(Issue.DESCRIPTION_FIELD, "Description edited");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);
        doReturn(existingIssue).when(issueRepository).save(existingIssue);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingIssue, responseEntity.getBody());
        assertEquals("Title edited", existingIssue.getTitle());
        assertEquals("Description edited", existingIssue.getDescription());
    }

    @Test
    void editIssue_WithAddDescription_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Title edited");
        issueMap.put(Issue.DESCRIPTION_FIELD, "Description edited");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);
        doReturn(existingIssue).when(issueRepository).save(existingIssue);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingIssue, responseEntity.getBody());
        assertEquals("Title edited", existingIssue.getTitle());
        assertEquals("Description edited", existingIssue.getDescription());
    }

    @Test
    void editIssue_WithAddEmptyDescription_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("Description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Title edited");
        issueMap.put(Issue.DESCRIPTION_FIELD, "");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);
        doReturn(existingIssue).when(issueRepository).save(existingIssue);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingIssue, responseEntity.getBody());
        assertEquals("Title edited", existingIssue.getTitle());
        assertTrue(existingIssue.getDescription().isEmpty());
    }

    @Test
    void editIssue_WithExcludeDescription_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("Description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Title edited");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);
        doReturn(existingIssue).when(issueRepository).save(existingIssue);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(existingIssue, responseEntity.getBody());
        assertEquals("Title edited", existingIssue.getTitle());
        assertEquals("Description", existingIssue.getDescription());
    }

    @Test
    void editIssue_WithEmptyTitle_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("Description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "");
        issueMap.put(Issue.DESCRIPTION_FIELD, "Description edited");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'title' field must have at least then 1 character", errorResponse.get("error"));
    }

    @Test
    void editIssue_WithInvalidTitle_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Welcome to a world where limitless possibilities await, where the boundless realm of" +
                                    " imagination intertwines with reality, and where dreams blossom into tangible achievements");
        issueMap.put(Issue.DESCRIPTION_FIELD, "Description edited");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'title' field length should be less then 100 characters", errorResponse.get("error"));
    }

    @Test
    void editIssue_WithInvalidDescription_ReturnsBadRequestResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("description")
                .build();
        Map<String, Object> issueMap = new HashMap<>();
        issueMap.put(Issue.TITLE_FIELD, "Title edited");
        issueMap.put(Issue.DESCRIPTION_FIELD, "Amidst the verdant splendor of an ancient and mystical forest,"
                + " a breathtaking creature with resplendent wings unfurled itself from the mighty embrace"
                + " of an age-old oak tree. Its iridescent beauty cast a spell of enchantment upon all"
                + " who beheld it, evoking a sense of awe and wonder that transcended mortal realms."
                + " As the golden orb of the sun dipped beneath the horizon, the majestic being took flight,"
                + " tracing a celestial path through the ethereal canvas of the night sky. A luminescent"
                + " trail of stardust cascaded in its wake, painting the heavens with a tapestry of shimmering lights."
                + " The soft caress of the wind carried the faint whispers of its melodious song, a haunting melody"
                + " that wove its way into the deepest recesses of the heart, igniting a profound longing for the magic"
                + " it embodied. Throughout the annals of time, legends and myths had been woven, speaking"
                + " of the creature's miraculous ability to grant wishes and manifest miracles, beckoning pilgrims"
                + " from far and wide to seek its divine presence. With each passing moment, the creature's powers"
                + " grew stronger, infused with an energy that transcended mortal comprehension. It became a living"
                + " conduit for the dreams and aspirations of all who dared to believe, spinning a tapestry of dreams"
                + " and enchantment that forever altered the destinies of those touched by its ethereal grace");

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);

        var responseEntity = issueService.editIssue(issueMap, id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.BAD_REQUEST, responseEntity.getStatusCode());

        var errorResponse = (Map<String, String>) responseEntity.getBody();
        assertNotNull(errorResponse);
        assertEquals(1, errorResponse.size());
        assertTrue(errorResponse.containsKey("error"));
        assertEquals("The 'description' field length should be less then 1000 characters", errorResponse.get("error"));
    }

    @Test
    void removeIssue_WithExistingIssue_ReturnsOkResponseEntity() {
        var id = 1L;
        var existingIssue = Issue.builder()
                .id(id)
                .title("Title")
                .description("Description")
                .build();

        doReturn(Optional.of(existingIssue)).when(issueRepository).findById(id);

        var responseEntity = issueService.removeIssue(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(issueRepository, times(1)).findById(id);
        verify(issueRepository, times(1)).deleteById(id);
    }

    @Test
    void removeIssue_WithMissingIssue_ReturnsNotFoundResponseEntity() {
        var id = 1L;

        doReturn(Optional.empty()).when(issueRepository).findById(id);

        var responseEntity = issueService.removeIssue(id);

        assertNotNull(responseEntity);
        assertEquals(HttpStatus.NOT_FOUND, responseEntity.getStatusCode());
        assertNull(responseEntity.getBody());

        verify(issueRepository, times(1)).findById(id);
        verify(issueRepository, times(0)).deleteById(id);
    }
}