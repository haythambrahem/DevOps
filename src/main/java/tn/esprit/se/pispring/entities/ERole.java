package tn.esprit.se.pispring.entities;

import com.google.common.collect.Sets;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import java.util.Set;
import java.util.stream.Collectors;

import static tn.esprit.se.pispring.entities.EPermission.*;
public enum ERole {
    ROLE_ADMIN(Sets.newHashSet(ROLE_ADMIN_READ,ROLE_ADMIN_WRITE,ROLE_ADMIN_UPDATE,ROLE_ADMIN_DELETE)),
    ROLE_USER(Sets.newHashSet(ROLE_USER_READ,ROLE_USER_WRITE,ROLE_USER_UPDATE,ROLE_USER_DELETE)),
    ROLE_HRE(Sets.newHashSet(ROLE_HRE_READ, ROLE_HRE_WRITE, ROLE_HRE_UPDATE, ROLE_HRE_DELETE)),
    ROLE_HR_ADMIN(Sets.newHashSet(ROLE_HR_ADMIN_READ, ROLE_HR_ADMIN_WRITE, ROLE_HR_ADMIN_UPDATE, ROLE_HR_ADMIN_DELETE)),
    ROLE_CRME(Sets.newHashSet(ROLE_CRME_READ, ROLE_CRME_WRITE, ROLE_CRME_UPDATE, ROLE_CRME_DELETE)),
    ROLE_CRM_ADMIN(Sets.newHashSet(ROLE_CRM_ADMIN_READ, ROLE_CRM_ADMIN_WRITE, ROLE_CRM_ADMIN_UPDATE, ROLE_CRM_ADMIN_DELETE)),
    ROLE_PROJECT(Sets.newHashSet(ROLE_PROJECT_READ, ROLE_PROJECT_WRITE, ROLE_PROJECT_UPDATE, ROLE_PROJECT_DELETE)),
    ROLE_PROJECT_ADMIN(Sets.newHashSet(ROLE_PROJECT_ADMIN_READ, ROLE_PROJECT_ADMIN_WRITE, ROLE_PROJECT_ADMIN_UPDATE, ROLE_PROJECT_ADMIN_DELETE)),
    ROLE_PRODUCT(Sets.newHashSet(ROLE_PRODUCT_READ, ROLE_PRODUCT_WRITE, ROLE_PRODUCT_UPDATE, ROLE_PRODUCT_DELETE)),
    ROLE_PRODUCT_ADMIN(Sets.newHashSet(ROLE_PRODUCT_ADMIN_READ, ROLE_PRODUCT_ADMIN_WRITE, ROLE_PRODUCT_ADMIN_UPDATE, ROLE_PRODUCT_ADMIN_DELETE));

    private final Set<EPermission> permissions;


    ERole(Set<EPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<EPermission> getPermissions() {
        return permissions;
    }
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){

        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority(this.name()));
        return permissions;
    }
}
