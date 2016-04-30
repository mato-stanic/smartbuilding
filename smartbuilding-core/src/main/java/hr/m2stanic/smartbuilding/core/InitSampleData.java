package hr.m2stanic.smartbuilding.core;


import com.google.common.collect.Sets;
import hr.m2stanic.smartbuilding.core.apartment.Admin;
import hr.m2stanic.smartbuilding.core.apartment.ApartmentManager;
import hr.m2stanic.smartbuilding.core.apartment.Tenants;
import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
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
    private ApartmentManager apartmentManager;


    public void init() {
        Role adminRole = initRoles();
        Admin admin = initSmartBuilding();
        AppUser adm = initAdminUser(adminRole, admin);
        initTenants();
    }




    private AppUser initAdminUser(Role adminRole, Admin adm) {

        AppUser admin = appUserManager.getByUsername("admin");
        if (admin == null) {
            admin = new AppUser("admin", "m2stanic");
            admin.setFirstName("Administrator");
            admin.setActive(true);
            admin.setRole(adminRole);
            admin.setApartment(adm);
            admin = appUserManager.save(admin);
        }
        return admin;
    }


    private Admin initSmartBuilding() {

        List<Admin> apartments = apartmentManager.getAllApartments();
        if (apartments.size() == 0) {
            return (Admin) apartmentManager.save(new Admin(null, "Admin"));
        } else return apartments.get(0);
    }


    private void initTenants() {

        Tenants s1a = (Tenants) apartmentManager.getApartment("Stan 1a");
        Tenants s1b = (Tenants) apartmentManager.getApartment("Stan 1b");
//        Tenants s2a = (Tenants) apartmentManager.getApartment("Stan 2a");

        if(s1a ==  null)
            s1a = (Tenants) apartmentManager.save(new Tenants(null, "Stan 1a"));

        if(s1b ==  null)
            s1b = (Tenants) apartmentManager.save(new Tenants(null, "Stan 1b"));

//        if(s2a ==  null)
//            s2a = (Tenants) apartmentManager.save(new Tenants(null, "Stan 2a"));

    }

    private Role initRoles() {

        Role adminRole = roleManager.getRoleByName("admin");
        if (adminRole == null) {
            adminRole = new Role(RoleScope.ADMIN, "admin", "Administrator", "Can administer everything on web",
                    Sets.newHashSet(Permission.values()));
            adminRole = roleManager.save(adminRole);
        }

        Role tenantRole = roleManager.getRoleByName("tenant");
        if (tenantRole == null) {

            Set<Permission> permissions = Sets.newHashSet(Permission.ACCESS_ADMIN_CONSOLE, Permission.MANAGE_USERS);

            tenantRole = new Role(RoleScope.TENANT, "tenant", "Tenant", "Can administer own apartment and users", permissions);
            roleManager.save(tenantRole);
        }

        return adminRole;
    }
}
