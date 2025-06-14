package entites.dtos;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseDto {
    private int id;
    private String title;
    private String description;
    private double price;
    private String location;
    private String status;
    private LocalDate availableFrom;
    private LocalDate availableTo;

    private int commentCount;
    private double averageRating;
    private List<CommentDto> comments;
    private UserDto host;
}
