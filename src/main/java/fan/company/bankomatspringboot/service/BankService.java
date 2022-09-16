package fan.company.bankomatspringboot.service;

import fan.company.bankomatspringboot.entity.Address;
import fan.company.bankomatspringboot.entity.Bank;
import fan.company.bankomatspringboot.payload.dto.ApiResult;
import fan.company.bankomatspringboot.payload.dto.BankDto;
import fan.company.bankomatspringboot.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class BankService {

    @Autowired
    BankRepository repository;
    

    public Page<Bank> getAllBank(Integer page) {
        Pageable pageable = PageRequest.of(page, 10);
        return repository.findAll(pageable);
    }

    public ApiResult getOne(Long id) {
        Optional<Bank> optionalBank = repository.findById(id);
        if (optionalBank.isEmpty())
            return new ApiResult("Bunday Bank mavjud emas!", false);
        return new ApiResult("OK!", true, optionalBank.get());
    }

    public ApiResult add(BankDto dto) {

        try {

            Optional<Bank> optionalBank = repository.findByName(dto.getName());
            if (optionalBank.isPresent())
                return new ApiResult("Bunday Bank mavjud!", false);

            Address address = new Address(dto.getStreet(), dto.getCity());

            Bank bank = new Bank(dto.getName(), address);

            repository.save(bank);

            return new ApiResult("Saqlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }

    }

    public ApiResult edit(Long id, BankDto dto) {

        try {

            Optional<Bank> optionalBank = repository.findById(id);
            if (optionalBank.isEmpty())
                return new ApiResult("Bunday Bank mavjud emas!", false);

            Bank bank = optionalBank.get();
            bank.setName(dto.getName());
            bank.getAddress().setStreet(dto.getStreet());
            bank.getAddress().setCity(dto.getCity());

            repository.save(bank);

            return new ApiResult("Taxrirlandi!", true);

        } catch (Exception e) {
            return new ApiResult(e.getMessage(), false);
        }
    }


    public ApiResult delete(Long id) {
        try {
            boolean exists = repository.existsById(id);
            if (exists) {
                repository.deleteById(id);
                return new ApiResult("O'chirildi", true);
            }
            return new ApiResult("Bunday Bank mavjud emas", false);
        } catch (Exception e) {
            return new ApiResult("Xatolik", false);
        }
    }

}
