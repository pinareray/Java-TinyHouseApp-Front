package entites.dtos;

import entites.enums.ReservationStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class ReservationDto {
    private int id;
    private LocalDate startDate;
    private LocalDate endDate;
    private ReservationStatus status;
    private LocalDate createdAt;
    private LocalDate updatedAt;

    private UserDto renter;
    private HouseListDto house;
}
