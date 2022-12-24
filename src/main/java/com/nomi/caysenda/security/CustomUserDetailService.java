package com.nomi.caysenda.security;

import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.RoleEntity;
import com.nomi.caysenda.entity.UserEntity;
import com.nomi.caysenda.repositories.RoleRepository;
import com.nomi.caysenda.repositories.UserRepository;
import com.nomi.caysenda.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class CustomUserDetailService implements UserDetailsService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    RoleRepository roleRepository;

    @Override
    public CustomUserDetail loadUserByUsername(String s) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsernameOrEmailOrPhonenumber(s,s,s);
        if (userEntity==null){
            throw new UsernameNotFoundException("User not found");
        }
        List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
        List<RoleEntity> roleEntities = roleRepository.findByUserId(userEntity.getId());
        if (roleEntities.size()>0){
            for (RoleEntity roleEntity: roleEntities) {
                authorities.add(new SimpleGrantedAuthority(roleEntity.getCode()));
            }
        }else {
            authorities.add(new SimpleGrantedAuthority("ROLE_SUBSCRIBER"));
        }
        CustomUserDetail userDetail = new CustomUserDetail(userEntity.getUsername(),userEntity.getPassword(),true,true,true,true,authorities,userEntity.getId());
        return userDetail;
    }
}
