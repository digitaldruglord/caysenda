package com.nomi.caysenda.repositories;

import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity,Integer> {
    @Query("SELECT new com.nomi.caysenda.dto.AddressDTO(a.id,a.phoneNumber,a.fullname,a.email,a.address,a.province,a.dictrict,a.ward,a.active) FROM AddressEntity a WHERE a.userAddress.id=?1 AND a.active=true ")
    AddressDTO findByDefaultAddress(Integer userId);
    @Query("SELECT a FROM AddressEntity a WHERE a.userAddress.id=?1 AND a.active=true ")
    AddressEntity findByDefaultAddressEntity(Integer userId);
    @Query("UPDATE AddressEntity SET active=?1 WHERE userAddress.id=?2")
    void updateActive(Boolean active,Integer userId);
    AddressEntity findByIdAndUserAddress_Id(Integer addressId,Integer userId);
    @Query("SELECT new com.nomi.caysenda.dto.AddressDTO(a.id,a.phoneNumber,a.fullname,a.email,a.address,a.province,a.dictrict,a.ward,a.active) FROM AddressEntity a WHERE a.userAddress.id=?1")
    List<AddressDTO> findAllAddressDTOByUserAddress_Id(Integer userId);
    List<AddressEntity> findAllByUserAddress_Id(Integer userId);
}
