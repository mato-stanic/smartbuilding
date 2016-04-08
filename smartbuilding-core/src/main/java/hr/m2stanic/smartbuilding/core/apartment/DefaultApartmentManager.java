package hr.m2stanic.smartbuilding.core.apartment;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class DefaultApartmentManager implements ApartmentManager {



    @Autowired
    private ApartmentRepository apartmentRepository;

    @Autowired
    private ApartmentLayoutRepository apartmentLayoutRepository;

    @Autowired
    private ApartmentCronJobRepository apartmentCronJobRepository;



    @Override
    public Apartment save(Apartment apartment) {
        return apartmentRepository.save(apartment);
    }

    @Override
    public Apartment getApartment(Long id) {
        return apartmentRepository.findOne(id);
    }

    @Override
    public Apartment getApartment(String name) {
        return apartmentRepository.findByName(name);
    }

    @Override
    public List<Admin> getAllApartments() {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "name");
        List<Admin> agencies = apartmentRepository.findAllAgencies(pageable).getContent();
        return agencies != null ? agencies : new ArrayList<>();
    }

    @Override
    public List<Tenants> getAllTenantApartments() {
        Pageable pageable = new PageRequest(0, 100, Sort.Direction.ASC, "name");
        List<Tenants> agencies = apartmentRepository.findAllOperatorGroups(pageable).getContent();
        return agencies != null ? agencies : new ArrayList<>();
    }

    @Override
    public ApartmentLayout getApartmentRoomStates(Apartment apartment) {
        return apartmentLayoutRepository.findByApartment(apartment);
    }

    @Override
    public ApartmentLayout save(ApartmentLayout apartmentLayout) {
        return apartmentLayoutRepository.save(apartmentLayout);
    }

    @Override
    public ApartmentCronJob save(ApartmentCronJob apartmentCronJob) {
        return apartmentCronJobRepository.save(apartmentCronJob);
    }

    @Override
    public List<ApartmentCronJob> getApartmentCronJobsForRoom(Apartment apartment, String room) {
        return apartmentCronJobRepository.findByApartmentAndRoom(apartment, room);
    }

    @Override
    public void delete(Long operatorId) {
        apartmentRepository.delete(operatorId);
    }
}
