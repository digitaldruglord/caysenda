package com.nomi.caysenda.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtils {
    public static CustomUserDetail getPrincipal(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        if (securityContext!=null){
            Authentication authentication = securityContext.getAuthentication();
            if (authentication!=null){
                if (!authentication.getPrincipal().equals("anonymousUser")){
                    CustomUserDetail userDetail = (CustomUserDetail) authentication.getPrincipal();
                    if (userDetail!=null){
                        return userDetail;
                    }
                }

            }
        }
        return null;
    }
    public static boolean isAdmin(CustomUserDetail userDetail){
        GrantedAuthority grantedAuthorityAdmin = userDetail.getAuthorities().stream().filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN")).findAny().orElse(null);
        if (grantedAuthorityAdmin!=null) return true;
        return false;
    }
    public static boolean isEmployeeExtension(CustomUserDetail userDetail){
        GrantedAuthority grantedAuthorityExmployee = userDetail.getAuthorities().stream().filter(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_EMPLOYEE")).findAny().orElse(null);
        if (grantedAuthorityExmployee!=null) return true;
        return false;
    }

}
