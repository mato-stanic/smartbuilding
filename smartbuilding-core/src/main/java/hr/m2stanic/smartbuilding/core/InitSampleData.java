package hr.m2stanic.smartbuilding.core;


import com.google.common.collect.Sets;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import hr.m2stanic.smartbuilding.core.company.*;
import hr.m2stanic.smartbuilding.core.security.Permission;
import hr.m2stanic.smartbuilding.core.security.Role;
import hr.m2stanic.smartbuilding.core.security.RoleManager;
import hr.m2stanic.smartbuilding.core.security.RoleScope;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class InitSampleData {

    @Autowired
    private AppUserManager appUserManager;


    @Autowired
    private RoleManager roleManager;

    @Autowired
    private CompanyManager companyManager;


    public void init() {
        Role adminRole = initRoles();
        Admin agency = initEkipAgency();
        AppUser admin = initAdminUser(adminRole, agency);
        initOperators();
    }




    private AppUser initAdminUser(Role adminRole, Admin ekip) {

        AppUser admin = appUserManager.getByUsername("admin");
        if (admin == null) {
            admin = new AppUser("admin", "m2stanic");
            admin.setFirstName("Administrator");
            admin.setActive(true);
            admin.setRole(adminRole);
            admin.setCompany(ekip);
            admin = appUserManager.save(admin);
        }
        return admin;
    }


    private Admin initEkipAgency() {

        List<Admin> agencies = companyManager.getAllAgencies();
        if (agencies.size() == 0) {
            return (Admin) companyManager.save(new Admin(null, "EKIP"));
        } else return agencies.get(0);
    }


    private void initOperators() {

        UserGroup s1a = (UserGroup) companyManager.getCompany("Stan 1a");
        UserGroup s1b = (UserGroup) companyManager.getCompany("Stan 1b");
        UserGroup s2a = (UserGroup) companyManager.getCompany("Stan 2a");

        if(s1a ==  null)
            s1a = (UserGroup) companyManager.save(new UserGroup(null, "Stan 1a"));

        if(s1b ==  null)
            s1b = (UserGroup) companyManager.save(new UserGroup(null, "Stan 1b"));

        if(s2a ==  null)
            s2a = (UserGroup) companyManager.save(new UserGroup(null, "Stan 1c"));

    }

    private Role initRoles() {

        Role adminRole = roleManager.getRoleByName("admin");
        if (adminRole == null) {
            adminRole = new Role(RoleScope.ADMIN, "admin", "Administrator", "Can administer tariffs and other entities",
                    Sets.newHashSet(Permission.values()));
            adminRole = roleManager.save(adminRole);
        }

        Role operatorRole = roleManager.getRoleByName("operator");
        if (operatorRole == null) {

            Set<Permission> permissions = Sets.newHashSet(Permission.ACCESS_ADMIN_CONSOLE, Permission.MANAGE_TARIFFS_OPERATOR,
                    Permission.MANAGE_PACKETS_OPERATOR, Permission.MANAGE_ZONES_OPERATOR, Permission.MANAGE_OPTIONS_OPERATOR);

            operatorRole = new Role(RoleScope.OPERATOR, "operator", "Operator", "Can administer tariffs and other entities", permissions);
            roleManager.save(operatorRole);
        }

        return adminRole;
    }
}
