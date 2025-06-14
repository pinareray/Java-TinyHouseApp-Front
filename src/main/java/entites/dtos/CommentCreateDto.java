package entites.dtos;

import lombok.Data;

// CommentCreateDto.java
@Data
public class CommentCreateDto {
    private String content;
    private int rating;
    private int userId;
    private int houseId;
}

