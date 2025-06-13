package entites.dtos;

import entites.enums.UserRole;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDto {
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private UserRole role;
    private boolean isActive;
}

