package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.*;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankDto;
import fan.company.bankomatspringboot.payload.dto.TimeDto;
import fan.company.bankomatspringboot.repository.AuditBankomatRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AuditBankomatService {

    @Autowired
    AuditBankomatRepository repository;

    public void audit(Bankomat bankomat, Users users, Card card, Amal amal, double pulmiqdori) {

        AuditBankomat auditBankomat = new AuditBankomat();
        auditBankomat.setBankomat(bankomat);
        auditBankomat.setUsers(users);
        auditBankomat.setPulMiqdori(pulmiqdori);
        auditBankomat.setAmal(amal);
        auditBankomat.setCard(card);
        repository.save(auditBankomat);

    }

    public Page<AuditBankomat> getAll(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    public ApiResult getOne(Long id) {
        Optional<AuditBankomat> optional = repository.findById(id);
        if (optional.isEmpty())
            return new ApiResult("Bunday Audit mavjud emas!", false);
        return new ApiResult("OK!", true, optional.get());
    }

    public  List<AuditBankomat> getBetweenTime(TimeDto dto) {
        List<AuditBankomat> list = repository.findByCreateAtBetween(dto.getStart(), dto.getEnd());
        if (list.isEmpty())
            return new ArrayList<>();
        return  list;
    }




}
