package simple.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Issue {

    public static final String ID_FIELD = "id";
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String USER_FIELD = "employee";
    public static final String COMMENT_FIELD = "comment";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_FIELD)
    private Long id;

    @Column(name = TITLE_FIELD, length = 100)
    @NonNull
    private String title;

    @Column(name = DESCRIPTION_FIELD, length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = USER_FIELD)
    @NonNull
    private Employee employee;

    @OneToMany
    @JoinColumn(name = COMMENT_FIELD)
    private List<Comment> comment;
}
