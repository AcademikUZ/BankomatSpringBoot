package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.Role;
import fan.company.bankomatspringboot.entity.Users;
import fan.company.bankomatspringboot.entity.enums.RoleName;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.LoginDto;
import fan.company.bankomatspringboot.payload.dto.UsersDto;
import fan.company.bankomatspringboot.repository.RoleRepository;
import fan.company.bankomatspringboot.repository.UsersRepository;
import fan.company.bankomatspringboot.security.PasswordValidator;
import fan.company.bankomatspringboot.security.tokenGenerator.JwtProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AuthService implements UserDetailsService {

    @Autowired
    UsersRepository repository;
    @Autowired
    PasswordValidator passwordValidator;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    JwtProvider jwtProvider;
    @Autowired
    JavaMailSender javaMailSender;
    @Autowired
    PasswordEncoder passwordEncoder;


    public Page<Users> getAllUsers(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    public ApiResult getOne(Long id) {
        Users userInSystem = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!userInSystem.getRoles().getRoleName().equals(RoleName.ROLE_DIREKTOR))
            return new ApiResult("Sizda bunday huquq yo'q", false);
        Optional<Users> optionalUsers = repository.findById(id);
        if (optionalUsers.isEmpty())
            return new ApiResult("Bunday user mavjud emas!", false);
        return new ApiResult("OK!", true, optionalUsers.get());
    }

    public ApiResult register(UsersDto dto) {

        try {
            Users userInSystem = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (userInSystem != null) {
                if (!userInSystem.getRoles().getRoleName().equals(RoleName.ROLE_DIREKTOR)) {
                    return new ApiResult("Sizda bunday huquq yo'q", false);
                }
            } else {
                return new ApiResult("Avval tizimga kiring", false);
            }

            Optional<Users> optionalUsers = repository.findByEmail(dto.getEmail());
            if (optionalUsers.isPresent())
                return new ApiResult("Bunday username mavjud!", false);
            boolean valid = passwordValidator.isValid(dto.getPassword());
            if (!valid)
                return new ApiResult("Password mustahkam bo'lishi kerak!", false);
            Optional<Role> optionalRole = roleRepository.findById(dto.getRolesId());
            if (optionalRole.isEmpty())
                return new ApiResult("Bunday role mavjud emas!", false);
            Users users = new Users(
                    dto.getFullName(), dto.getEmail(), passwordEncoder.encode(dto.getPassword()), optionalRole.get()
            );
            repository.save(users);
            return new ApiResult("Saqlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }

    }

    public ApiResult edit(Long id, UsersDto dto) {

        try {

            Users userInSystem = (Users) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

            if (userInSystem != null) {
                if (!userInSystem.getRoles().getRoleName().equals(RoleName.ROLE_DIREKTOR)) {
                    return new ApiResult("Sizda bunday huquq yo'q", false);
                }
            } else {
                return new ApiResult("Avval tizimga kiring", false);
            }

            Optional<Users> optionalUsers = repository.findById(id);
            if (optionalUsers.isEmpty())
                return new ApiResult("Bunday User mavjud emas!", false);
            boolean valid = passwordValidator.isValid(dto.getPassword());
            if (!valid)
                return new ApiResult("Password mustahkam bo'lishi kerak!", false);
            Optional<Role> optionalRole = roleRepository.findById(dto.getRolesId());
            if (optionalRole.isEmpty())
                return new ApiResult("Bunday role mavjud emas!", false);

            Users users = optionalUsers.get();
            users.setFullName(dto.getFullName());
            users.setPassword(passwordEncoder.encode(dto.getPassword()));
            users.setEmail(dto.getEmail());
            users.setRoles(optionalRole.get());
            repository.save(users);
            return new ApiResult("Tahrirlandi!", true);
        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }
    }

    public ApiResult login(LoginDto loginDto) {

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                loginDto.getEmail(),
                loginDto.getPassword()
        ));

        /**
         * Authentication ishlatilmasa quyidagi ululni ishlatish mumkin
         */

//            Optional<User> optionalUser = userRepository.findByEmail(loginDto.getUsername());
//            if (!optionalUser.isPresent()) {
//                return new ApiResult("Login yoki parol xato!", false);
//            }
//
//            if (!passwordEncoder.matches(loginDto.getPassword(), optionalUser.get().getPassword())) {
//                return new ApiResult("Login yoki parol xato!", false);
//            }

        Users users = (Users) authenticate.getPrincipal();
        String token = jwtProvider.generatorToken(users.getEmail(), users.getRoles());
        return new ApiResult("Token", true, token);
    }

    public ApiResult delete(Long id) {
        try {
            boolean exists = repository.existsById(id);
            if (exists) {
                repository.deleteById(id);
                return new ApiResult("O'chirildi", true);
            }
            return new ApiResult("Bunday User mavjud emas", false);
        } catch (Exception e) {
            return new ApiResult("Xatolik", false);
        }
    }

    public Boolean sendMail(String sendingEmail, String message) {


        String button = "<input type='button' onclick='http://localhost:8080/api/auth/verifyEmail?emailCode=" + message + "&email=" + sendingEmail + "'; value='Tasdiqlash' />";

        try {

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom("company@fan.uz");
            simpleMailMessage.setTo(sendingEmail);
            simpleMailMessage.setSubject("company@fan.uz tizimida accaountni tasdiqlash");
            simpleMailMessage.setText(button);
            System.out.println(button);
            javaMailSender.send(simpleMailMessage);
            return true;

        } catch (MailException e) {
            e.printStackTrace();
            return false;
        }

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        /**
         * Usernameni email orqali topish
         */

        return repository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + " topilmadi!"));
    }

}
