package simple.Entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Post {

    public static final String ID_FIELD = "id";
    public static final String TITLE_FIELD = "title";
    public static final String DESCRIPTION_FIELD = "description";
    public static final String EMPLOYEE_FIELD = "employee";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ID_FIELD)
    private Long id;

    @Column(name = TITLE_FIELD, length = 50)
    private String title;

    @Column(name = DESCRIPTION_FIELD, length = 1000)
    private String description;

    @ManyToOne
    @JoinColumn(name = EMPLOYEE_FIELD)
    private Employee employee;
}
