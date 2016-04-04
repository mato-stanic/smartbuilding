package hr.m2stanic.smartbuilding.web.dto;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserGroupDTO extends CompanyDTO {

    public UserGroupDTO(Long id, String name) {
        super(id, name);
    }



}
