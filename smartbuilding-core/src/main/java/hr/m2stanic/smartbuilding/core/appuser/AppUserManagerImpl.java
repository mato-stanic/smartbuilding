package hr.m2stanic.smartbuilding.core.appuser;

import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@Slf4j
public class AppUserManagerImpl implements AppUserManager {


    @Autowired
    private AppUserRepository appUserRepository;


    @Override
    public AppUser save(AppUser appUser) {
        appUser = appUserRepository.save(appUser);
        return appUser;
    }


    @Override
    public AppUser getByUsername(String username) {
        return appUserRepository.findByUsername(username);
    }

    @Override
    public AppUser getByEmail(String email) {
        return appUserRepository.findByEmail(email);
    }


    @Override
    public List<AppUser> getAllUsers() {
        return appUserRepository.findAll();
    }

    @Override
    public List<AppUser> getAllUsersNotAdmin() {
        return appUserRepository.findAllNotAdmin();
    }

    @Override
    public List<AppUser> getApartmentUsers(Long apartmentId) {
        return appUserRepository.findByApartmentId(apartmentId);
    }


    @Override
    public Page<AppUser> getByRole(Long roleId, Pageable pageable) {
        return appUserRepository.findByRoleId(roleId, pageable);
    }


    @Override
    public AppUser getUser(Long userId) {
        return appUserRepository.findOne(userId);
    }

    @Override
    public boolean delete(Long userId) {
        AppUser appUser = getUser(userId);
        if (appUser != null) {
            appUserRepository.delete(appUser);
            return true;
        }
        return false;
    }

    @Override
    public AppUser getLoggedInUser() {
        Subject subject = SecurityUtils.getSubject();
        return (subject != null && subject.getPrincipal() instanceof AppUser) ? (AppUser) subject.getPrincipal() : null;
    }
}
