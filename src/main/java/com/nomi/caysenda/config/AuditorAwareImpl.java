package com.nomi.caysenda.config;

import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.SecurityUtils;
import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditorAwareImpl implements AuditorAware<String> {
    @Override
    public Optional<String> getCurrentAuditor() {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userDetail!=null){
            return Optional.of(userDetail.getUsername());
        }else {
            return Optional.empty();
        }

    }
}
