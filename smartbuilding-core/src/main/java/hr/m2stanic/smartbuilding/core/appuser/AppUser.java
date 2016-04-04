package hr.m2stanic.smartbuilding.core.appuser;

import com.google.common.base.Objects;
import hr.m2stanic.smartbuilding.core.company.Apartment;
import hr.m2stanic.smartbuilding.core.company.Apartment;
import hr.m2stanic.smartbuilding.core.security.Role;
import lombok.*;
import org.hibernate.annotations.Type;
import org.joda.time.LocalDateTime;

import javax.persistence.*;
import java.io.Serializable;


@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class AppUser implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "user_seq")
    @SequenceGenerator(name = "user_seq", sequenceName = "user_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "active")
    private boolean active = true;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "password", nullable = false)
    private String password;


    @Column(name = "email")
    private String email;

    @ManyToOne
    @JoinColumn(name = "apartment_id")
    private Apartment apartment;


    @ManyToOne(optional = true)
    @JoinColumn(name = "role_id")
    private Role role = null;

    @Column(name = "last_login")
    @Type(type = "org.jadira.usertype.dateandtime.joda.PersistentLocalDateTime")
    private LocalDateTime lastLogin = null;

    public AppUser(String username, String password) {
        this.username = username;
        this.password = password;
    }


    public String getFullName() {
        StringBuilder fullName = new StringBuilder();

        if (firstName != null) fullName.append(firstName);

        if (lastName != null) fullName.append(" ").append(lastName);

        String fn = fullName.toString().trim();
        return fn.length() > 0 ? fn : username;
    }

    @Override
    public String toString() {
        return Objects.toStringHelper(this)

                .add("username", username).add("active", active).add("firstName", firstName).add("lastName", lastName).add("email", email).toString();
    }


}
