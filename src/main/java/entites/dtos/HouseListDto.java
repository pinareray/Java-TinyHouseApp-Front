package entites.dtos;

import lombok.*;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HouseListDto {
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

    private UserDto host;

    @Override
    public String toString() {
        return title; // Ekranda görünen ev ismi olacak
    }
}
