package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.entity.Bank;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankDto;
import fan.company.bankomatspringboot.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bank")
@Transactional
public class BankController {

    @Autowired
    BankService service;


    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping
    public HttpEntity<?> getAllBank(@RequestParam Integer page) {
        Page<Bank> bankPage = service.getAllBank(page);
        return ResponseEntity.status(bankPage.hasContent() ? HttpStatus.OK : HttpStatus.CONFLICT).body(bankPage);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        ApiResult apiResult = service.getOne(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PostMapping
    public HttpEntity<?> add(@RequestBody BankDto dto) {
        ApiResult apiResult = service.add(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody BankDto dto) {
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
