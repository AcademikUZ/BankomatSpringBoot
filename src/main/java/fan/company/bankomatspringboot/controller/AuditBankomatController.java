package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.entity.AuditBankomat;
import fan.company.bankomatspringboot.entity.Bank;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankDto;
import fan.company.bankomatspringboot.payload.dto.TimeDto;
import fan.company.bankomatspringboot.service.AuditBankomatService;
import fan.company.bankomatspringboot.service.BankService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auditbankomat")
@Transactional
public class AuditBankomatController {

    @Autowired
    AuditBankomatService service;


    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping
    public HttpEntity<?> getAllBank(@RequestParam Integer page) {
        Page<AuditBankomat> bankPage = service.getAll(page);
        return ResponseEntity.status(bankPage.hasContent() ? HttpStatus.OK : HttpStatus.CONFLICT).body(bankPage);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        ApiResult apiResult = service.getOne(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping("/getOneBetweenTime")
    public HttpEntity<?> getBetweenTime(@RequestBody TimeDto dto) {
        List<AuditBankomat> list = service.getBetweenTime(dto);
        return ResponseEntity.status(!list.isEmpty() ? HttpStatus.OK : HttpStatus.CONFLICT).body(list);
    }


}
