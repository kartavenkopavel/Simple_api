package simple.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import simple.entity.Comment;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentDto {
    private Long id;
    private String text;
    private int likes;

    public CommentDto(Comment comment) {
        this.id = comment.getId();
        this.text = comment.getText();
        this.likes = comment.getLikes();
    }
}
