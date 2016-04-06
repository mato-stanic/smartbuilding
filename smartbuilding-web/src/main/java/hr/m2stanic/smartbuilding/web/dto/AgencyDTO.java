package hr.m2stanic.smartbuilding.web.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class AgencyDTO extends ApartmentDTO {

    public AgencyDTO(Long id, String name) {
        super(id, name);
    }
}
