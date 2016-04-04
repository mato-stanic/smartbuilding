package hr.m2stanic.smartbuilding.core.appuser;


import hr.m2stanic.smartbuilding.core.company.Apartment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor {

    Page<AppUser> findByRoleId(Long roleId, Pageable pageable);

    AppUser findByUsername(String username);

    @Query("select u from AppUser u where u.apartment.name not like 'ADMIN' order by u.apartment.name asc")
    List<AppUser> findAllNotAdmin();

    AppUser findByEmail(String email);

    @Query("select u from AppUser u where u.apartment.id = ?1")
    List<AppUser> findByApartmentId(Long apartmentId);

    List<AppUser> findByApartment(Apartment apartment);




}
