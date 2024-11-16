package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class PetService {
    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PetRepository petRepository;

    public List<Pet> getAllPets() {
        // Retrieve all Pet entities from the petRepository.
        // The findAll() method fetches all the pets stored in the database.
        return petRepository.findAll();
    }

    public Pet getPetById(Long petId) {
        // Retrieve the Pet entity from the petRepository using the provided petId.
        // The method getOne() returns a proxy object for the Pet, which is lazily loaded.
        return petRepository.getOne(petId);
    }

    public List<Pet> getPetsByCustomerId(long customerId) {
        // Call the petRepository's method to find pets associated with the provided customerId.
        // This will query the database for all pets that belong to the customer with the given id.
        return petRepository.findPetByCustomerId(customerId);
    }


    public Pet savePet(Pet pet, Long customerId) {
        // Retrieve the Customer object from the repository using the provided customerId.
        // The method getOne is used, which returns a proxy object if the customer exists.
        Customer customer = customerRepository.getOne(customerId);

        // Set the customer associated with the pet. This links the pet to the retrieved customer.
        pet.setCustomer(customer);

        // Save the pet object to the pet repository (persist it in the database).
        // The pet object is now associated with the customer.
        pet = petRepository.save(pet);

        // Add the newly saved pet to the customer's list of pets.
        // This keeps the customer's pets list updated with the new pet.
        customer.getPets().add(pet);

        // Save the customer object to the customer repository.
        // This ensures that the customer's pets list, which was modified above, is persisted.
        customerRepository.save(customer);

        // Return the saved pet object, which includes the customer association.
        return pet;
    }

}