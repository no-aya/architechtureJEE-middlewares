package ma.enset.ebankingbackend.mappers;

import ma.enset.ebankingbackend.dtos.BankAccountDTO;
import ma.enset.ebankingbackend.dtos.CustomerDTO;
import ma.enset.ebankingbackend.entities.BankAccount;
import ma.enset.ebankingbackend.entities.Customer;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

//We can use Mapstruct
// Mapstruct is a code generator that greatly simplifies the implementation of mappings between Java bean types based on a convention over configuration approach.
//https://mapstruct.org/


@Service
public class BanKAccountMapperImpl {
    public CustomerDTO fromCustomerToCustomerDTO(Customer customer) {
        CustomerDTO customerDTO = new CustomerDTO();
        /*customerDTO.setID(customer.getID());
        customerDTO.setFirstName(customer.getFirstName());
        customerDTO.setLastName(customer.getLastName());
        customerDTO.setEmail(customer.getEmail());
        customerDTO.setPhoneNumber(customer.getPhoneNumber());*/
        BeanUtils.copyProperties(customer, customerDTO);
        return customerDTO;
    }
    public Customer fromCustomerDTOToCustomer(CustomerDTO customerDTO) {
        Customer customer = new Customer();
        BeanUtils.copyProperties(customerDTO, customer);
        return customer;
    }

    public BankAccount fromCustomerDTOToBankAccount(CustomerDTO customerDTO) {
        BankAccount bankAccount = new BankAccount();
        BeanUtils.copyProperties(customerDTO, bankAccount);
        return bankAccount;
    }

    public BankAccountDTO fromBankAccountToBankAccountDTO(BankAccount bankAccount) {
        BankAccountDTO bankAccountDTO = new BankAccountDTO();
        BeanUtils.copyProperties(bankAccount, bankAccountDTO);
        return bankAccountDTO;
    }
}
