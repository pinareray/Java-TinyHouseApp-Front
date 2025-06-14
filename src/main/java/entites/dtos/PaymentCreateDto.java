package entites.dtos;

import lombok.Data;

@Data
public class PaymentCreateDto {
    private double amount;
    private int reservationId;
    private int userId;
}

