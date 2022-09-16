package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.entity.Users;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.LoginDto;
import fan.company.bankomatspringboot.payload.dto.UsersDto;
import fan.company.bankomatspringboot.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Transactional
public class AuthController {

    @Autowired
    AuthService service;


    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping
    public HttpEntity<?> getAllUsers(@RequestParam Integer page) {
        Page<Users> usersPage = service.getAllUsers(page);
        return ResponseEntity.status(usersPage.hasContent() ? HttpStatus.OK : HttpStatus.CONFLICT).body(usersPage);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        ApiResult apiResult = service.getOne(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PostMapping("/register")
    public HttpEntity<?> register(@RequestBody UsersDto dto) {
        ApiResult apiResult = service.register(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }

    @PostMapping("/login")
    public HttpEntity<?> login(@RequestBody LoginDto dto) {
        ApiResult apiResult = service.login(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody UsersDto dto) {
        ApiResult apiResult = service.edit(id, dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @DeleteMapping("/{id}")
    public HttpEntity<?> delete(@PathVariable Long id) {
        ApiResult apiResult = service.delete(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }


}
