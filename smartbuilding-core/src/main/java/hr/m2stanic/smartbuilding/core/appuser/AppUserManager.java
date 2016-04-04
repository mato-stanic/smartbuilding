package hr.m2stanic.smartbuilding.core.appuser;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AppUserManager {

    AppUser save(AppUser appUser);

    AppUser getByUsername(String username);

    AppUser getByEmail(String email);

    List<AppUser> getAllUsers();

    List<AppUser> getAllUsersNotAdmin();

    List<AppUser> getApartmentUsers(Long operatorId);

    Page<AppUser> getByRole(Long roleId, Pageable pageable);

    AppUser getUser(Long userId);

    boolean delete(Long userId);

    AppUser getLoggedInUser();
}
