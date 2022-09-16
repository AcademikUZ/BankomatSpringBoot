package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.CardDto;
import fan.company.bankomatspringboot.payload.dto.PayDto;
import fan.company.bankomatspringboot.service.PayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.transaction.Transactional;

@RestController
@RequestMapping("/api/transaction")
@Transactional
public class PayController {

    @Autowired
    PayService service;

    @PreAuthorize(value = "hasAnyRole('ROLE_MIJOZ')")
    @PostMapping("/carddanCardgaPulTransaksiyasi")
    public HttpEntity<?> carddanCardgaPulTransaksiyasi(@RequestBody PayDto dto) {
        ApiResult apiResult = service.carddanCardgaPulTransaksiyasi(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MIJOZ')")
    @PostMapping("/cardgaPulTashashTransaksiyasi")
    public HttpEntity<?> cardgaPulTashashTransaksiyasi(@RequestBody PayDto dto) {
        ApiResult apiResult = service.cardgaPulTashashTransaksiyasi(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MIJOZ')")
    @PostMapping("/carddanPulYechishTransaksiyasi")
    public HttpEntity<?> carddanPulYechishTransaksiyasi(@RequestBody PayDto dto) {
        ApiResult apiResult = service.carddanPulYechishTransaksiyasi(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_MASUL_XODIM')")
    @PostMapping("/bankomatniToldirish")
    public HttpEntity<?> bankomatniToldirish(@RequestBody PayDto dto) {
        ApiResult apiResult = service.bankomatniToldirish(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

}
