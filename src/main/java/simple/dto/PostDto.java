package simple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple.entity.Post;

import javax.xml.stream.events.Comment;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String description;
    private List<Comment> comment;

    public PostDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.description = post.getDescription();
    }
}
