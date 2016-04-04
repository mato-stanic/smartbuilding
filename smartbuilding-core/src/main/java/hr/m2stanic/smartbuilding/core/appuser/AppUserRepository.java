package hr.m2stanic.smartbuilding.core.appuser;


import hr.m2stanic.smartbuilding.core.company.Company;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface AppUserRepository extends JpaRepository<AppUser, Long>, JpaSpecificationExecutor {

    Page<AppUser> findByRoleId(Long roleId, Pageable pageable);

    AppUser findByUsername(String username);

    @Query("select u from AppUser u where u.company.name not like 'ADMIN' order by u.company.name asc")
    List<AppUser> findAllNotAdmin();

    AppUser findByEmail(String email);

    @Query("select u from AppUser u where u.company.id = ?1")
    List<AppUser> findByCompanyId(Long companyId);

    List<AppUser> findByCompany(Company company);




}
