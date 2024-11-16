package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
public class CustomerService {
    @Autowired
    private PetRepository petRepository;
    @Autowired
    private CustomerRepository customerRepository;


    // Get all customer
    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }


    public Customer saveCustomer(Customer customer, List<Long> petIds) {
        List<Pet> customerPets = new ArrayList<>();

        // check if list petIds not empty
        if (petIds != null && !petIds.isEmpty()) {
            for (Long petId : petIds) {
                Pet pet = petRepository.getOne(petId); // Get Pet by petId
                if (pet != null) {
                    customerPets.add(pet);  // Add pet to customer's pet list
                }
            }
        }

        customer.setPets(customerPets); // Assign pets to customers
        return customerRepository.save(customer); // Save customer and return results
    }

    // Get customer by petId
    public Customer getCustomerByPetId(Long petId) {
        return petRepository.getOne(petId).getCustomer();
    }


}