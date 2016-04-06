package hr.m2stanic.smartbuilding.web.converters;

import org.springframework.core.convert.converter.Converter;

import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.web.dto.ApartmentDTO;
import hr.m2stanic.smartbuilding.web.dto.DTOUtil;


public class CompanyDTOConverter implements Converter<String, ApartmentDTO> {

    private final ApartmentManager apartmentManager;

    public CompanyDTOConverter(ApartmentManager apartmentManager) {
        this.apartmentManager = apartmentManager;
    }

    @Override
    public ApartmentDTO convert(String id) {
        return DTOUtil.toDTO(apartmentManager.getApartment(Long.parseLong(id)));
    }
}
