package hr.m2stanic.smartbuilding.web.dto;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserDTO {

    private Long id;

    private String username;

    private boolean active = true;

    private String firstName;

    private String lastName;

    private String password;

    private String email;

    private RoleDTO role = null;

    private ApartmentDTO apartment;

    public String getFullName() {
        StringBuilder fullName = new StringBuilder();
        if (firstName != null) fullName.append(firstName);
        if (lastName != null) {
            if (fullName.length() > 0) fullName.append(" ");
        }
        if (fullName.length() == 0) fullName.append(username);
        return fullName.toString();
    }
}
