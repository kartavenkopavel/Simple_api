package simple.entity;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class Employee {

    public static final String ID_FIELD = "id";
    public static final String NAME_FIELD = "name";
    public static final String LAST_NAME_FIELD = "lastName";
    public static final String CREATED_AT_FIELD = "createdAt";
    public static final String ISSUES_FIELD = "issues";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ID_FIELD)
    private Long id;

    @Column(name = NAME_FIELD, length = 20)
    @Size(min = 1, message = "The 'name' field must have at least then 1 character")
    private String name;

    @Column(name = LAST_NAME_FIELD, length = 100)
    @Size(min = 1, message = "The 'lastName' field must have at least then 1 character")
    private String lastName;

    @Column(name = CREATED_AT_FIELD)
    private String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss"));

    @OneToMany
    @JoinColumn(name = ISSUES_FIELD)
    @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
    private List<Issue> issues;

    public void addIssue(Issue issue) {
        if (this.issues == null) {
            this.issues = new ArrayList<>();
        }
        this.issues.add(issue);
        issue.setEmployee(this);
    }
}