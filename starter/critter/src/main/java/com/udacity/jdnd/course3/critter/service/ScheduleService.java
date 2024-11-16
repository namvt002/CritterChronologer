package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
public class ScheduleService {

    @Autowired
    private ScheduleRepository scheduleRepository;

    @Autowired
    private PetRepository petRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private CustomerRepository customerRepository;

    public List<Schedule> getAllSchedules() {
        // Retrieve all Schedule entities from the scheduleRepository.
        // The findAll() method fetches all the schedule records stored in the database.
        return scheduleRepository.findAll();
    }


    public List<Schedule> getCustomerSchedule(Long customerId) {
        // Retrieve the Customer entity from the customerRepository using the provided customerId.
        // The method getOne() returns a proxy object for the Customer, which is lazily loaded.
        Customer customer = customerRepository.getOne(customerId);

        // Use the customer's pets to find schedules associated with those pets.
        // This assumes that the scheduleRepository has a method to find schedules where the pets are included in the customer's pets list.
        return scheduleRepository.findByPetsIn(customer.getPets());
    }

    public List<Schedule> getPetSchedule(Long petId) {
        // Retrieve the Pet entity from the petRepository using the provided petId.
        // The method getOne() returns a proxy object for the Pet, which is lazily loaded.
        Pet pet = petRepository.getOne(petId);

        // Use the pet to find schedules associated with that pet.
        // This assumes that the scheduleRepository has a method to find schedules where the pet is directly associated with the schedule.
        return scheduleRepository.findByPets(pet);
    }

    public List<Schedule> getEmployeeSchedule(Long employeeId) {
        // Retrieve the Employee entity from the employeeRepository using the provided employeeId.
        // The getOne() method returns a proxy object for the Employee, which is lazily loaded.
        Employee employee = employeeRepository.getOne(employeeId);

        // Use the employee to find schedules associated with that employee.
        // This assumes that the scheduleRepository has a method to find schedules where the employee is directly associated with the schedule.
        return scheduleRepository.findByEmployee(employee);
    }

    public Schedule saveSchedule(Schedule schedule) {
        // Save the provided Schedule entity to the scheduleRepository.
        // The save() method persists the Schedule object in the database.
        return scheduleRepository.save(schedule);
    }

}