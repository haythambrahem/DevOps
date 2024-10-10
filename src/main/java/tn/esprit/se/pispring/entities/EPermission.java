package tn.esprit.se.pispring.entities;

import lombok.AllArgsConstructor;


@AllArgsConstructor
public enum EPermission {
    //Permission for Admin
    ROLE_ADMIN_READ("admin:read"),
    ROLE_ADMIN_WRITE("admin:write"),
    ROLE_ADMIN_UPDATE("admin:update"),
    ROLE_ADMIN_DELETE("admin:delete"),

    //Permission for User
    ROLE_USER_READ("user:read"),
    ROLE_USER_WRITE("user:write"),
    ROLE_USER_UPDATE("user:update"),
    ROLE_USER_DELETE("user:delete"),

    //Permission for HR
    ROLE_HRE_READ("hre:read"),
    ROLE_HRE_WRITE("hre:write"),
    ROLE_HRE_UPDATE("hre:update"),
    ROLE_HRE_DELETE("hre:delete"),

    //Permission for Admin-HR
    ROLE_HR_ADMIN_READ("hr_admin:read"),
    ROLE_HR_ADMIN_WRITE("hr_admin:write"),
    ROLE_HR_ADMIN_UPDATE("hr_admin:update"),
    ROLE_HR_ADMIN_DELETE("hr_admin:delete"),

    //Permission for CRM
    ROLE_CRME_READ("crme:read"),
    ROLE_CRME_WRITE("crme:write"),
    ROLE_CRME_UPDATE("crme:update"),
    ROLE_CRME_DELETE("crme:delete"),

    //Permission for Admin-CRM
    ROLE_CRM_ADMIN_READ("crm_admin:read"),
    ROLE_CRM_ADMIN_WRITE("crm_admin:write"),
    ROLE_CRM_ADMIN_UPDATE("crm_admin:update"),
    ROLE_CRM_ADMIN_DELETE("crm_admin:delete"),

    //Permission for PROJECT
    ROLE_PROJECT_READ("project:read"),
    ROLE_PROJECT_WRITE("project:write"),
    ROLE_PROJECT_UPDATE("project:update"),
    ROLE_PROJECT_DELETE("project:delete"),

    //Permission for Admin-PROJECT
    ROLE_PROJECT_ADMIN_READ("project_admin:read"),
    ROLE_PROJECT_ADMIN_WRITE("project_admin:write"),
    ROLE_PROJECT_ADMIN_UPDATE("project_admin:update"),
    ROLE_PROJECT_ADMIN_DELETE("project_admin:delete"),

    //Permission for PRODUCT
    ROLE_PRODUCT_READ("product:read"),
    ROLE_PRODUCT_WRITE("product:write"),
    ROLE_PRODUCT_UPDATE("product:update"),
    ROLE_PRODUCT_DELETE("product:delete"),

    //Permission for Admin-PRODUCT
    ROLE_PRODUCT_ADMIN_READ("product_admin:read"),
    ROLE_PRODUCT_ADMIN_WRITE("product_admin:write"),
    ROLE_PRODUCT_ADMIN_UPDATE("product_admin:update"),
    ROLE_PRODUCT_ADMIN_DELETE("product_admin:delete");

    private final String permission;

    public String getPermission() {
        return permission;
    }
}

