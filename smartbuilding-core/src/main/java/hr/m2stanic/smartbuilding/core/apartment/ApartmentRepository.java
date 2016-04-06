package hr.m2stanic.smartbuilding.core.apartment;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;


public interface ApartmentRepository extends JpaRepository<Apartment, Long> {

    @Query("select o from Tenants o")
    Page<Tenants> findAllOperatorGroups(Pageable pageable);

    @Query("select a from Admin a")
    Page<Admin> findAllAgencies(Pageable pageable);

    Apartment findByName(String name);



}
