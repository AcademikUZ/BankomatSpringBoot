package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.entity.Bankomat;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankomatDto;
import fan.company.bankomatspringboot.service.BankomatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bankomat")
@Transactional
public class BankomatController {

    @Autowired
    BankomatService service;


    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping
    public HttpEntity<?> getAllBankomat(@RequestParam Integer page) {
        Page<Bankomat> bankomatPage = service.getAllBankomat(page);
        return ResponseEntity.status(bankomatPage.hasContent() ? HttpStatus.OK : HttpStatus.CONFLICT).body(bankomatPage);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        ApiResult apiResult = service.getOne(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PostMapping
    public HttpEntity<?> add(@RequestBody BankomatDto dto) {
        ApiResult apiResult = service.add(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody BankomatDto dto) {
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
