package entites.dtos;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AdminSummaryDto {
    private int totalUsers;
    private int totalHosts;
    private int totalRenters;
    private int activeReservations;
    private int monthlyReservations;
    private double monthlyIncome;
}
