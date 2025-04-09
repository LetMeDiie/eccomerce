package kz.amihady.eccomerce.customer.service;

import kz.amihady.eccomerce.customer.mapper.AddressMapper;
import kz.amihady.eccomerce.customer.mapper.CustomerMapper;
import kz.amihady.eccomerce.customer.repo.CustomerRepository;
import kz.amihady.eccomerce.customer.request.CustomerRequest;
import kz.amihady.eccomerce.customer.response.AddressResponse;
import kz.amihady.eccomerce.customer.response.CustomerResponse;
import kz.amihady.eccomerce.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;
    private final AddressMapper addressMapper;

    public UUID createCustomer(CustomerRequest request){
        var customer = customerMapper.fromRequest(request);
        return customerRepository.save(customer).getId();
    }

    public CustomerResponse findById(UUID id){
        var customer =  customerRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Клиент не найден."));
        return customerMapper.fromCustomer(customer);
    }

    public AddressResponse findAddress(UUID id){
        var customer =  customerRepository.findById(id)
                .orElseThrow(()-> new EntityNotFoundException("Клиент не найден."));
        return addressMapper.fromAddress(customer.getAddress());

    }
}
