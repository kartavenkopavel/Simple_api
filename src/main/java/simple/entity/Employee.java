package simple.entity;

import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = ID_FIELD)
    private Long id;

    @Column(name = NAME_FIELD, length = 20)
    @NonNull
    private String name;

    @Column(name = LAST_NAME_FIELD, length = 100)
    @NonNull
    private String lastName;

    @Column(name = CREATED_AT_FIELD)
    private String createdAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy | HH:mm:ss"));
}