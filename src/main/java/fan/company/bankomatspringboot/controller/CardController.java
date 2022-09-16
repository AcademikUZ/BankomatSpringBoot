package fan.company.bankomatspringboot.controller;

import fan.company.bankomatspringboot.entity.Card;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.CardDto;
import fan.company.bankomatspringboot.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/card")
@Transactional
public class CardController {

    @Autowired
    CardService service;


    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR')")
    @GetMapping
    public HttpEntity<?> getAllCard(@RequestParam Integer page) {
        Page<Card> CardPage = service.getAllCard(page);
        return ResponseEntity.status(CardPage.hasContent() ? HttpStatus.OK : HttpStatus.CONFLICT).body(CardPage);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR', 'ROLE_BANKDAGI_MASUL')")
    @GetMapping("/{id}")
    public HttpEntity<?> getOne(@PathVariable Long id) {
        ApiResult apiResult = service.getOne(id);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.OK : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR', 'ROLE_BANKDAGI_MASUL')")
    @PostMapping
    public HttpEntity<?> add(@RequestBody CardDto dto) {
        ApiResult apiResult = service.add(dto);
        return ResponseEntity.status(apiResult.isSuccess() ? HttpStatus.CREATED : HttpStatus.CONFLICT).body(apiResult);
    }

    @PreAuthorize(value = "hasAnyRole('ROLE_DIREKTOR', 'ROLE_BANKDAGI_MASUL')")
    @PutMapping("/{id}")
    public HttpEntity<?> edit(@PathVariable Long id, @RequestBody CardDto dto) {
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
