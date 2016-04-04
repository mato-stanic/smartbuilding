package hr.m2stanic.smartbuilding.core.company;


import com.google.common.base.Objects;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Entity
@Inheritance
@DiscriminatorColumn(name = "user_type")
@Table(name = "organization")
public abstract class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
    @SequenceGenerator(name = "company_seq", sequenceName = "company_seq", allocationSize = 1)
    @Setter(AccessLevel.PRIVATE)
    private Long id;


    @Column(unique = true)
    @NotNull
    @Size(min = 2, max = 256, message = "Naziv firme mora sadržavati minimalno 2 a maksimalno 256 znakova!")
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Apartment apartment = (Apartment) o;
        boolean sameName = Objects.equal(getName(), apartment.getName());
        return sameName;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public String toString() {
        return name;
    }
}
