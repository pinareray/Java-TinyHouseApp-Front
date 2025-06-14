package entites.dtos;

import lombok.Data;

// CommentUpdateDto.java
@Data
public class CommentUpdateDto {
    private int id;
    private String content;
    private int rating;
    private int userId; // yetki kontrolü için
}
