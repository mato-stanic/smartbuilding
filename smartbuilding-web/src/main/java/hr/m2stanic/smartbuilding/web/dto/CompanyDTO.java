package hr.m2stanic.smartbuilding.web.dto;

import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CompanyDTO {

    @Setter(AccessLevel.PRIVATE)
    private Long id;

    @NotNull
    @Size(min = 2, max = 50, message = "Naziv operatera mora sadržavati minimalno 2 a maksimalno 50 znakova!")
    private String name = "";

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CompanyDTO)) return false;

        CompanyDTO that = (CompanyDTO) o;

        return !(name != null ? !name.equals(that.name) : that.name != null);

    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }
}
