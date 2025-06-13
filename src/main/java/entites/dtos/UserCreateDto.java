package entites.dtos;

import entites.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserCreateDto {

    @NotBlank(message = "İsim boş bırakılamaz.")
    @Size(max = 50)
    private String firstName;

    @NotBlank(message = "Soyisim boş bırakılamaz.")
    @Size(max = 50)
    private String lastName;

    @Email(message = "Geçerli bir e-posta girin.")
    @NotBlank(message = "Email boş bırakılamaz.")
    private String email;

    @NotBlank(message = "Şifre boş bırakılamaz.")
    private String password;

    private UserRole role;
}

