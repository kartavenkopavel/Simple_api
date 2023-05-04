package simple.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Comment {

    public static final String COMMENT_ID_FIELD = "id";
    public static final String COMMENT_TEXT_FIELD = "text";
    public static final String COMMENT_LIKES_FIELD = "likes";
    public static final String POST_FIELD = "post";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = COMMENT_ID_FIELD)
    private Long id;

    @Column(name = COMMENT_TEXT_FIELD)
    @NonNull
    private String text;

    @Column(name = COMMENT_LIKES_FIELD)
    private int likes;

    @ManyToOne
    @JoinColumn(name = POST_FIELD)
    private Post post;

}
