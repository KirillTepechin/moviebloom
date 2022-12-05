package moviebloom.dto;

import lombok.Data;
import moviebloom.domain.enums.UserRole;
import org.hibernate.validator.constraints.Length;

@Data
public class RegisterDto {
    @Length(min = 3, max = 20)
    private String login;
    @Length(min = 3, max = 20)
    private String password;
    private UserRole role;
}
