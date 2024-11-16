package com.udacity.jdnd.course3.critter.user;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.user.CustomerDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeDTO;
import com.udacity.jdnd.course3.critter.user.EmployeeRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 * <p>
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private EmployeeService employeeService;


    private CustomerDTO convertCustomerToCustomerDTO(Customer customer) {
        // Initialize a list to store pet IDs
        List<Long> petIds = new ArrayList<>();

        // Iterate through the customer's pets and add each pet's ID to the petIds list
        for (Pet pet : customer.getPets()) {
            petIds.add(pet.getId());
        }

        // Return a new CustomerDTO object with the customer details and the list of pet IDs
        return new CustomerDTO(customer.getId(), customer.getName(), customer.getPhoneNumber(), customer.getNotes(), petIds);
    }


    private EmployeeDTO convertEmployeeToEmployeeDTO(Employee employee) {
        return new EmployeeDTO(employee.getId(), employee.getName(), employee.getSkills(), employee.getDaysAvailable());
    }

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO) {
        // Create a new Customer object using the data from the CustomerDTO
        Customer customer = new Customer(
                customerDTO.getId(),          // Set the customer's ID
                customerDTO.getName(),        // Set the customer's name
                customerDTO.getPhoneNumber(), // Set the customer's phone number
                customerDTO.getNotes()        // Set the customer's notes
        );

        // Retrieve the list of pet IDs from the CustomerDTO
        List<Long> petIds = customerDTO.getPetIds();

        try {
            // Save the customer and associated pets, and convert the result to a CustomerDTO
            return convertCustomerToCustomerDTO(customerService.saveCustomer(customer, petIds));
        } catch (Exception exception) {
            // If an error occurs, throw a BAD_REQUEST error with a relevant message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Customer could not be saved", exception);
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers() {
        // Retrieve the list of all customers from the service layer
        List<Customer> customers = customerService.getAllCustomers();

        // Create a new list to store the CustomerDTO objects
        List<CustomerDTO> customerDTOList = new ArrayList<>();

        // Loop through each Customer and convert it to CustomerDTO
        for (Customer customer : customers) {
            // Convert each customer to CustomerDTO and add it to the list
            customerDTOList.add(convertCustomerToCustomerDTO(customer));
        }

        // Return the list of CustomerDTOs
        return customerDTOList;
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId) {
        try {
            // Retrieve the customer (owner) based on the pet ID and convert it to CustomerDTO
            return convertCustomerToCustomerDTO(customerService.getCustomerByPetId(petId));
        } catch (Exception exception) {
            // If an error occurs (e.g., pet ID not found), throw a BAD_REQUEST error
            // with a message indicating the pet owner could not be found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Owner pet with id: " + petId + " not found", exception);
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        // Create a new Employee object using the data from the EmployeeDTO
        Employee employee = new Employee(
                employeeDTO.getId(),          // Set the employee's ID
                employeeDTO.getName(),        // Set the employee's name
                employeeDTO.getDaysAvailable(), // Set the employee's available days
                employeeDTO.getSkills()       // Set the employee's skills
        );

        try {
            // Call the service layer to save the employee and return the saved employee as an EmployeeDTO
            return convertEmployeeToEmployeeDTO(employeeService.saveEmployee(employee));
        } catch (Exception exception) {
            // If an error occurs during the saving process, throw a BAD_REQUEST (400) exception
            // with a custom error message and the exception details
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee could not be saved", exception);
        }
    }

    @GetMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try {
            // Retrieve the employee by their ID from the service layer and convert it to EmployeeDTO
            return convertEmployeeToEmployeeDTO(employeeService.getEmployeeById(employeeId));
        } catch (Exception exception) {
            // If an error occurs (e.g., employee with the given ID not found), throw a BAD_REQUEST (400) exception
            // with a custom error message indicating that the employee with the specified ID could not be found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Employee with id: " + employeeId + " not found", exception);
        }
    }


    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try {
            // Update the availability of the employee with the specified ID
            // The daysAvailable set contains the days of the week the employee is available
            employeeService.setEmployeeAvailability(daysAvailable, employeeId);
        } catch (Exception exception) {
            // If an error occurs (e.g., employee with the given ID not found), throw a BAD_REQUEST (400) exception
            // with a custom error message indicating that the employee with the specified ID could not be found
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Employee with id: " + employeeId + " not found", exception);
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        // Retrieve the list of employees based on the requested service date and skills
        List<Employee> employees = employeeService.getEmployeesByService(employeeDTO.getDate(), employeeDTO.getSkills());

        // Create a list to hold the EmployeeDTO objects
        List<EmployeeDTO> employeeDTOList = new ArrayList<>();

        // Loop through each Employee and convert it to EmployeeDTO
        for (Employee employee : employees) {
            EmployeeDTO employeeDTOConverted = convertEmployeeToEmployeeDTO(employee);
            employeeDTOList.add(employeeDTOConverted);
        }

        // Return the list of EmployeeDTOs
        return employeeDTOList;
    }

}