package hr.m2stanic.smartbuilding.web.converters;

import org.springframework.core.convert.converter.Converter;

import hr.m2stanic.smartbuilding.core.company.CompanyManager;
import hr.m2stanic.smartbuilding.web.dto.CompanyDTO;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;


public class CompanyDTOConverter implements Converter<String, CompanyDTO> {

    private final CompanyManager companyManager;

    public CompanyDTOConverter(CompanyManager companyManager) {
        this.companyManager = companyManager;
    }

    @Override
    public CompanyDTO convert(String id) {
        return DTOUtil.toDTO(companyManager.getApartment(Long.parseLong(id)));
    }
}
