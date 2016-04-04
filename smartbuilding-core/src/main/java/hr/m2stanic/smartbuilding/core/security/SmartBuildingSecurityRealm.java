package hr.m2stanic.smartbuilding.core.security;

import hr.m2stanic.smartbuilding.core.appuser.AppUser;
import hr.m2stanic.smartbuilding.core.appuser.AppUserManager;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authc.*;
import org.apache.shiro.authc.credential.SimpleCredentialsMatcher;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.authz.permission.WildcardPermission;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

@Slf4j
public class SmartBuildingSecurityRealm extends AuthorizingRealm {


    @Autowired
    private AppUserManager appUserManager;


    private SimpleCredentialsMatcher simpleCredentialsMatcher = new SimpleCredentialsMatcher();


    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {

        Set<String> roles = new HashSet<>();
        Set<org.apache.shiro.authz.Permission> permissions = new HashSet<>();

        Collection<AppUser> principalsList = principals.byType(AppUser.class);
        for (AppUser appUser : principalsList) {
            Role role = appUser.getRole();
            if (role != null) {
                roles.add(role.getName());
                for (Permission permission : role.getPermissions()) {
                    permissions.add(new WildcardPermission(permission.getValue()));
                }
            }
        }

        SimpleAuthorizationInfo info = new SimpleAuthorizationInfo(roles);
        info.setRoles(roles);
        info.setObjectPermissions(permissions);
        return info;
    }


    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {

        UsernamePasswordToken upat = (UsernamePasswordToken) token;
        AppUser appUser = appUserManager.getByUsername(upat.getUsername());
        if (appUser != null && appUser.isActive()) {
            AuthenticationInfo authInfo = new SimpleAuthenticationInfo(appUser, appUser.getPassword(), getName());
            if (simpleCredentialsMatcher.doCredentialsMatch(token, authInfo)) {
                log.debug("Successfull login: " + appUser.getUsername());
                return authInfo;
            }
        }
        throw new AuthenticationException("Invalid username/password combination!");
    }
}
