package com.udacity.jdnd.course3.critter.pet;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/pet")
public class PetController {

    @Autowired
    PetService petService;

    private PetDTO convertPetToPetDTO(Pet pet) {
        return new PetDTO(pet.getId(), pet.getType(), pet.getName(), pet.getCustomer().getId(), pet.getBirthDate(), pet.getNotes());
    }

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        // Create a new Pet object using the data from the PetDTO
        // The Pet constructor is called with the type, name, birth date, and notes from the DTO
        Pet pet = new Pet(petDTO.getType(), petDTO.getName(), petDTO.getBirthDate(), petDTO.getNotes());

        try {
            // Attempt to save the pet using the petService
            // The pet is saved with the owner ID provided in the DTO
            Pet savedPet = petService.savePet(pet, petDTO.getOwnerId());

            // Convert the saved Pet object to a PetDTO and return it
            return convertPetToPetDTO(savedPet);
        } catch (Exception exception) {
            // If an error occurs (e.g., pet could not be saved), throw a ResponseStatusException
            // with a BAD_REQUEST status and a relevant error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet could not be saved", exception);
        }
    }


    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try {
            // Attempt to retrieve the Pet object by its ID using the petService
            Pet pet = petService.getPetById(petId);

            // If the Pet is found, convert it to PetDTO and return it
            return convertPetToPetDTO(pet);
        } catch (Exception exception) {
            // If an error occurs (e.g., pet is not found), throw a ResponseStatusException
            // with a BAD_REQUEST status and a relevant error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet with id: " + petId + " not found", exception);
        }
    }


    @GetMapping
    public List<PetDTO> getPets() {
        // Retrieve the list of pets from the petService
        List<Pet> pets = petService.getAllPets();

        // Create an empty list to store the converted PetDTO objects
        List<PetDTO> petDTOs = new ArrayList<>();

        // Iterate over each Pet object and convert it to a PetDTO
        for (Pet pet : pets) {
            PetDTO petDTO = convertPetToPetDTO(pet);  // Convert Pet to PetDTO
            petDTOs.add(petDTO);  // Add the converted PetDTO to the list
        }

        // Return the list of PetDTOs
        return petDTOs;
    }


    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
        try {
            // Retrieve the list of pets associated with the given owner (customer) ID
            List<Pet> pets = petService.getPetsByCustomerId(ownerId);

            // Create an empty list to store the PetDTO objects
            List<PetDTO> petDTOs = new ArrayList<>();

            // Iterate over each Pet and convert it to a PetDTO
            for (Pet pet : pets) {
                PetDTO petDTO = convertPetToPetDTO(pet);  // Convert Pet to PetDTO
                petDTOs.add(petDTO);  // Add the converted PetDTO to the list
            }

            // Return the list of PetDTOs
            return petDTOs;

        } catch (Exception exception) {
            // If an error occurs, throw a ResponseStatusException with an error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Owner pet with id " + ownerId + " not found", exception);
        }
    }

}