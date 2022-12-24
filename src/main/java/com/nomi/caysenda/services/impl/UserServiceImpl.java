package com.nomi.caysenda.services.impl;


import com.google.common.hash.Hashing;
import com.nomi.caysenda.api.admin.model.user.request.AdminUserRegisterRequest;
import com.nomi.caysenda.api.admin.model.user.responses.AdminUserRegisterResponse;
import com.nomi.caysenda.controller.requests.UserRegisterRequest;
import com.nomi.caysenda.controller.requests.user.UserUpdateRequest;
import com.nomi.caysenda.controller.responses.UserRegisterResponse;
import com.nomi.caysenda.controller.responses.user.UserChangePasswordResponse;
import com.nomi.caysenda.dto.AddressDTO;
import com.nomi.caysenda.dto.UserDTO;
import com.nomi.caysenda.entity.*;
import com.nomi.caysenda.exceptions.user.UserLoginException;
import com.nomi.caysenda.exceptions.user.UserRegisterException;
import com.nomi.caysenda.repositories.*;
import com.nomi.caysenda.security.CustomUserDetail;
import com.nomi.caysenda.security.JwtTokenProvider;
import com.nomi.caysenda.security.SecurityUtils;
import com.nomi.caysenda.services.AddressService;
import com.nomi.caysenda.services.MyMailSender;
import com.nomi.caysenda.services.ProgressService;
import com.nomi.caysenda.services.UserService;
import com.nomi.caysenda.utils.UploadFileUtils;
import com.nomi.caysenda.utils.UserUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.mail.MailSender;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.function.Function;

@Service
public class UserServiceImpl implements UserService {
    @Autowired UserRepository userRepository;
    @Autowired RoleRepository roleRepository;
    @Autowired JwtTokenProvider jwtTokenProvider;
    @Autowired PasswordEncoder passwordEncoder;
    @Autowired MyMailSender mailSender;
    @Autowired AddressService addressService;
    @Autowired AddressProvinceRepository provinceRepository;
    @Autowired AddressDictrictRepository dictrictRepository;
    @Autowired AddressWardRepository wardRepository;
    @Autowired TaskScheduler taskScheduler;
    @Autowired ProgressService progressService;
    private static final String USER_PROGRESS = "USER_PROGRESS";
    @Override
    public Optional<UserEntity> findById(Integer userId) {
        return userRepository.findById(userId);
    }

    @Override
    public Page<UserEntity> findAll(Pageable pageable) {
        Page<UserEntity> entities = userRepository.findAll(pageable);
        return entities;
    }

    @Override
    public Page<UserEntity> search(String keyword, Pageable pageable) {
        Page<UserEntity> entities = userRepository.findAllByUsernameLikeOrPhonenumberLikeOrEmailLike("%"+keyword+"%","%"+keyword+"%","%"+keyword+"%",pageable);
        return entities;
    }

    @Override
    public void update(UserUpdateRequest updateRequest) throws UserRegisterException {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        if (userRepository.existsByPhonenumberAndIdNot(updateRequest.getPhoneNumber(),userDetail.getUserId())){
            throw new UserRegisterException(UserRegisterException.PHONE_EXIST_MESSAGE,UserRegisterException.PHONE_EXIST_CODE);
        }
        UserEntity userEntity = userRepository.findById(SecurityUtils.getPrincipal().getUserId()).orElse(null);
        if (userEntity!=null){
            userEntity.setFullName(updateRequest.getFullName());
            userEntity.setPhonenumber(updateRequest.getPhoneNumber());
            userRepository.save(userEntity);
        }
    }

    @Override
    public Map login(String userName, String password) throws UserLoginException {
        Map map = new HashMap<>();
        UserEntity userEntity = userRepository.findByUsernameOrEmailOrPhonenumber(userName,userName,userName);
        if (userEntity!=null){
            UserDTO userDTO = UserUtils.convertEntitytoDTO(userEntity);
            if (passwordEncoder.matches(password,userDTO.getPassword())){
                String token = jwtTokenProvider.generateToken(userDTO);
                map.put("success",true);
                map.put("user",userEntity);
                map.put("roles",roleRepository.findByUserId(userDTO.getId()));
                map.put("token",token);
            }else {
                throw new UserLoginException(UserLoginException.MESSAGE_FAIL,UserLoginException.STAUS_FAIL,UserLoginException.CODE_INCORECT);
            }
        }else {
              throw new UserLoginException(UserLoginException.MESSAGE_NOT_FOUND,UserLoginException.STAUS_FAIL,UserLoginException.CODE_NOT_FOUND);
        }
        return map;
    }

    @Override
    public Map register(UserDTO userDTO) throws UserRegisterException {
        userDTO.setStatus(1);
        UserEntity userEntity = UserUtils.convertEntitytoDTO(userDTO);
        try{
            userRepository.save(userEntity);
        }catch ( DataIntegrityViolationException e){
            throw new UserRegisterException(UserRegisterException.USERNAME_EMAIL_EXIST_MESSAGE,UserRegisterException.USERNAME_EMAIL_EXIST_CODE);
        }
        return null;
    }

    @Override
    public UserRegisterResponse register(UserRegisterRequest request) throws UserRegisterException {
        if (!checkData(request)){
            throw new UserRegisterException(UserRegisterException.MISSING_INFOMATION_MESSAGE,
                    UserRegisterException.MISSING_INFOMATION_CODE);
        }
        if (userRepository.existsByUsernameOrEmailOrPhonenumber(request.getUserName(),request.getUserName(),request.getUserName())){
            throw new UserRegisterException(
                    UserRegisterException.USERNAME_EMAIL_EXIST_MESSAGE,
                    UserRegisterException.USERNAME_EMAIL_EXIST_CODE
            );
        }
        if (userRepository.existsByUsernameOrEmailOrPhonenumber(request.getEmail(),request.getEmail(),request.getEmail())){
            throw new UserRegisterException(
                    UserRegisterException.USERNAME_EMAIL_EXIST_MESSAGE,
                    UserRegisterException.USERNAME_EMAIL_EXIST_CODE
            );
        }

        if (userRepository.existsByUsernameOrEmailOrPhonenumber(request.getPhone(),request.getPhone(),request.getPhone())){
            throw new UserRegisterException(
                    UserRegisterException.PHONE_EXIST_MESSAGE,
                    UserRegisterException.PHONE_EXIST_CODE
            );
        }

        if (!request.getPassword().equals(request.getConfirmPassword())){
            throw new UserRegisterException(
                    UserRegisterException.CONFIRM_PASSWORD_INCORECT_CODE,
                    UserRegisterException.CONFIRM_PASSWORD_INCORECT_MESSAGE
            );
        }

        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(request.getUserName());
            userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            userEntity.setEmail(request.getEmail());
            userEntity.setPhonenumber(request.getPhone());
            RoleEntity roleEntity = roleRepository.findByCode("ROLE_CUSTOMER");
            userEntity.setRoles(List.of(roleEntity));
            userEntity.setStatus(1);
            userRepository.save(userEntity);
            return new UserRegisterResponse(true,"Đăng ký thành công");
        }catch ( DataIntegrityViolationException e){
            throw new UserRegisterException(
                    UserRegisterException.USERNAME_EMAIL_EXIST_MESSAGE,
                    UserRegisterException.USERNAME_EMAIL_EXIST_CODE
            );
        }
    }
    Boolean checkData(UserRegisterRequest request){
        if (    request.getPhone()!=null && !request.getPhone().equals("") &&
                request.getEmail()!=null && !request.getEmail().equals("") &&
                request.getUserName()!=null && !request.getUserName().equals("") &&
                request.getPassword()!=null && !request.getPassword().equals("") &&
                request.getConfirmPassword()!=null && !request.getConfirmPassword().equals("")){
            return true;
        }
        return false;
    }
    @Override
    public AdminUserRegisterResponse register(AdminUserRegisterRequest userDTO) throws UserRegisterException {
        if (!userDTO.getPassword().equals(userDTO.getConfirmPassword())){
            throw new UserRegisterException(
                    UserRegisterException.CONFIRM_PASSWORD_INCORECT_CODE,
                    UserRegisterException.CONFIRM_PASSWORD_INCORECT_MESSAGE
            );
        }
        try {
            UserEntity userEntity = new UserEntity();
            userEntity.setUsername(userDTO.getUsername());
            userEntity.setPassword(passwordEncoder.encode(userDTO.getPassword()));
            userEntity.setPhonenumber(userDTO.getPhoneNumber());
            userEntity.setEmail(userDTO.getEmail());
            RoleEntity roleEntity = roleRepository.findByCode(userDTO.getRoleCode()!=null?userDTO.getRoleCode():"ROLE_EMPLOYEE");
            userEntity.setRoles(List.of(roleEntity));
            userEntity.setStatus(1);
            userRepository.save(userEntity);
            return new AdminUserRegisterResponse(true,"register success");
        }catch ( DataIntegrityViolationException e){
            throw new UserRegisterException(
                    UserRegisterException.USERNAME_EMAIL_EXIST_MESSAGE,
                    UserRegisterException.USERNAME_EMAIL_EXIST_CODE
            );
        }
    }

    @Override
    public AdminUserRegisterResponse update(AdminUserRegisterRequest request) {
        UserEntity userEntity = userRepository.findById(request.getId()).orElse(null);
        if (userEntity!=null){
            if (request.getPhoneNumber()!=null)userEntity.setPhonenumber(request.getPhoneNumber());
            if (request.getEmail()!=null)userEntity.setEmail(request.getEmail());
            if (request.getRoleCode()!=null){
                RoleEntity roleEntity = roleRepository.findByCode(request.getRoleCode()!=null?request.getRoleCode():"ROLE_EMPLOYEE");
                userEntity.setRoles(List.of(roleEntity));
            }
            if (request.getPassword()!=null)userEntity.setPassword(passwordEncoder.encode(request.getPassword()));
            userRepository.save(userEntity);
        }
        return new AdminUserRegisterResponse(true,"update success");
    }

    @Override
    public UserDTO findByUserNameOrEmail(String param) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(param,param);
        if (userEntity!=null){
            return UserUtils.convertEntitytoDTO(userEntity);
        }
        return null;
    }

    @Override
    public List<UserEntity> findAllByRole(String role) {
        RoleEntity roleEntity = roleRepository.findByCode(role);
        return userRepository.findAllByRolesContains(roleEntity);
    }

    @Override
    public UserChangePasswordResponse changePassword(String passwordOld, String passwordNew, String passwordNewConfirm) {
        CustomUserDetail userDetail = SecurityUtils.getPrincipal();
        UserEntity userEntity = userRepository.findById(userDetail.getUserId()).orElse(null);
        if (userEntity!=null){
          if (passwordEncoder.matches(passwordOld,userEntity.getPassword())){
                if (passwordNew.equals(passwordNewConfirm)){
                    userEntity.setPassword(passwordEncoder.encode(passwordNew));
                    userRepository.save(userEntity);
                    return new UserChangePasswordResponse(true,"Thay đổi mật khẩu thành công");
                }
                return new UserChangePasswordResponse(false,"Xác thực mật khẩu không đúng. Vui lòng kiểm tra lại");
          }else {
              return new UserChangePasswordResponse(false,"Mật khẩu không hợp lệ. Vui lòng kiểm tra lại");
          }
        }
        return new UserChangePasswordResponse(false,"Lỗi không hợp lệ vui lòng liên hệ nhà phát triển");
    }

    @Override
    public UserChangePasswordResponse newPassword(Integer userId, String passwordNew, String passwordNewConfirm) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        if (userEntity!=null && passwordNew.equals(passwordNewConfirm)){
            userEntity.setPassword(passwordEncoder.encode(passwordNew.trim()));
            userRepository.save(userEntity);
            return new UserChangePasswordResponse(true,"Tạo mới mật khẩu thành công");
        }
        return new UserChangePasswordResponse(true,"Tạo mới mật khẩu thất bại");
    }

    @Override
    public void sendEmailForgotPassword(String userName) {
        UserEntity userEntity = userRepository.findByUsernameOrEmail(userName,userName);

        mailSender.sendEmailForgotPassword(userEntity);
    }

    @Override
    public void generateExcel() throws IOException {
        progressService.save(0,100,USER_PROGRESS);
        taskScheduler.schedule(() -> {
            File s = UploadFileUtils.getPath("static/");
            File folder = new File(s.getPath()+"/excel");
            FileOutputStream outputStream = null;
            try {
                outputStream = new FileOutputStream(folder.getPath()+"/export-users.xlsx");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            XSSFWorkbook ouputbook = new XSSFWorkbook();
            XSSFSheet outSheet = ouputbook.createSheet("HOADON");
            generateHeader(outSheet.createRow(0));
            Integer i = 1;
            List<UserEntity> users = userRepository.findAll();
            for (UserEntity userEntity:users){
                AddressDTO addressDTO =addressService.findAddressDefault(userEntity.getId());
                Row cells = outSheet.createRow(i++);
                Cell idCell = cells.createCell(new CellAddress("A1").getColumn());
                Cell nameCell = cells.createCell(new CellAddress("B1").getColumn());
                Cell phoneCell = cells.createCell(new CellAddress("C1").getColumn());
                Cell emailCell = cells.createCell(new CellAddress("D1").getColumn());
                Cell addressCell = cells.createCell(new CellAddress("E1").getColumn());
                Cell wardCell = cells.createCell(new CellAddress("F1").getColumn());
                Cell dictrictCell = cells.createCell(new CellAddress("G1").getColumn());
                Cell provinceCell = cells.createCell(new CellAddress("H1").getColumn());

                if (userEntity.getUsername()!=null) nameCell.setCellValue(userEntity.getUsername());
                if (userEntity.getPhonenumber()!=null) phoneCell.setCellValue(userEntity.getPhonenumber());
                if (userEntity.getEmail()!=null) emailCell.setCellValue(userEntity.getEmail());
                if (userEntity.getId()!=null) idCell.setCellValue(userEntity.getId());

                if (addressDTO!=null){
                    AddressProviceEntity proviceEntity = provinceRepository.findById(addressDTO.getProvince()).orElse(null);
                    AddressDictrictEntity dictrictEntity = dictrictRepository.findById(addressDTO.getDictrict()).orElse(null);
                    AddressWardsEntity wardEntity = wardRepository.findById(addressDTO.getWard()).orElse(null);
                    if (proviceEntity!=null) provinceCell.setCellValue(proviceEntity.getName());
                    if (dictrictEntity!=null) dictrictCell.setCellValue(dictrictEntity.getName());
                    if (wardEntity!=null) wardCell.setCellValue(wardEntity.getName());
                    if (addressDTO.getAddress()!=null) addressCell.setCellValue(addressDTO.getAddress());

                }
                progressService.save(i,users.size(),USER_PROGRESS);
            }
            try {
                ouputbook.write(outputStream);
                outputStream.close();
                ouputbook.close();
                progressService.delete(USER_PROGRESS);
            } catch (IOException e) {
                e.printStackTrace();
            }

        },new Date());



    }
    private void generateHeader(Row cells){
        cells.createCell(new CellAddress("A1").getColumn()).setCellValue("mã thành viên");
        cells.createCell(new CellAddress("B1").getColumn()).setCellValue("Tên thành viên");
        cells.createCell(new CellAddress("C1").getColumn()).setCellValue("Số điện thoại");
        cells.createCell(new CellAddress("D1").getColumn()).setCellValue("Email");
        cells.createCell(new CellAddress("E1").getColumn()).setCellValue("Số nhà");
        cells.createCell(new CellAddress("F1").getColumn()).setCellValue("Xã/Phường");
        cells.createCell(new CellAddress("G1").getColumn()).setCellValue("Quận/Huyện");
        cells.createCell(new CellAddress("H1").getColumn()).setCellValue("Thành phố");

    }
}
