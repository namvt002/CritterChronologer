package com.udacity.jdnd.course3.critter.schedule;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    @Autowired
    ScheduleService scheduleService;
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private PetRepository petRepository;

    private ScheduleDTO convertSchedule(Schedule schedule) {
        // Create a list to hold the employee IDs
        List<Long> employeeIds = new ArrayList<>();
        // Iterate over each employee in the schedule and add their ID to the list
        for (Employee employee : schedule.getEmployee()) {
            employeeIds.add(employee.getId());
        }

        // Create a list to hold the pet IDs
        List<Long> petIds = new ArrayList<>();
        // Iterate over each pet in the schedule and add their ID to the list
        for (Pet pet : schedule.getPets()) {
            petIds.add(pet.getId());
        }

        // Return a new ScheduleDTO with the extracted data
        return new ScheduleDTO(schedule.getId(), employeeIds, petIds, schedule.getDate(), schedule.getActivities());
    }


    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        Schedule schedule = convertDTOToSchedule(scheduleDTO);

        Schedule savedSchedule = scheduleService.saveSchedule(schedule);

        return convertScheduleToDTO(savedSchedule);

    }

    public ScheduleDTO convertScheduleToDTO(Schedule schedule) {
        // Create a new ScheduleDTO object
        ScheduleDTO scheduleDTO = new ScheduleDTO();

        // Copy properties from Schedule to ScheduleDTO using BeanUtils
        BeanUtils.copyProperties(schedule, scheduleDTO);

        // Create a list to hold employee IDs
        List<Long> listOfEmployeeIDs = new ArrayList<>();
        // Iterate over each employee and add their ID to the list
        for (Employee employee : schedule.getEmployee()) {
            listOfEmployeeIDs.add(employee.getId());
        }

        // Create a list to hold pet IDs
        List<Long> listOfPetIDs = new ArrayList<>();
        // Iterate over each pet and add their ID to the list
        for (Pet pet : schedule.getPets()) {
            listOfPetIDs.add(pet.getId());
        }

        // Set the date and activities for the ScheduleDTO
        scheduleDTO.setDate(schedule.getDate());
        scheduleDTO.setActivities(schedule.getActivities());

        // Set the employee and pet IDs in the ScheduleDTO
        scheduleDTO.setEmployeeIds(listOfEmployeeIDs);
        scheduleDTO.setPetIds(listOfPetIDs);

        // Return the fully populated ScheduleDTO
        return scheduleDTO;
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        // Retrieve all schedules from the scheduleService
        List<Schedule> allSchedules = scheduleService.getAllSchedules();

        // Create a list to hold the ScheduleDTOs
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        // Iterate over each Schedule and convert it to a ScheduleDTO
        for (Schedule schedule : allSchedules) {
            ScheduleDTO scheduleDTO = convertSchedule(schedule);  // Convert Schedule to ScheduleDTO
            scheduleDTOs.add(scheduleDTO);  // Add the converted DTO to the list
        }

        // Return the list of ScheduleDTOs
        return scheduleDTOs;
    }


    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        List<Schedule> schedules;

        try {
            // Attempt to retrieve the pet schedule
            schedules = scheduleService.getPetSchedule(petId);
        } catch (Exception exception) {
            // If an error occurs, throw a ResponseStatusException with an error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Pet schedule with id: " + petId + " not found", exception);
        }

        // Create a list to hold the ScheduleDTOs
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        // Iterate over each Schedule and convert it to a ScheduleDTO
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = convertSchedule(schedule);  // Convert Schedule to ScheduleDTO
            scheduleDTOs.add(scheduleDTO);  // Add the converted DTO to the list
        }

        // Return the list of ScheduleDTOs
        return scheduleDTOs;
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        List<Schedule> schedules;

        try {
            // Attempt to retrieve the employee schedule
            schedules = scheduleService.getEmployeeSchedule(employeeId);
        } catch (Exception exception) {
            // If an error occurs, throw a ResponseStatusException with an error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Employee schedule with employee id: " + employeeId + " not found", exception);
        }

        // Create a list to hold the ScheduleDTOs
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        // Iterate over each Schedule and convert it to a ScheduleDTO
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = convertSchedule(schedule);  // Convert Schedule to ScheduleDTO
            scheduleDTOs.add(scheduleDTO);  // Add the converted DTO to the list
        }

        // Return the list of ScheduleDTOs
        return scheduleDTOs;
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        List<Schedule> schedules;

        try {
            // Attempt to retrieve the customer schedule
            schedules = scheduleService.getCustomerSchedule(customerId);
        } catch (Exception exception) {
            // If an error occurs, throw a ResponseStatusException with an error message
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Schedule with owner id " + customerId + " not found", exception);
        }

        // Create a list to hold the ScheduleDTOs
        List<ScheduleDTO> scheduleDTOs = new ArrayList<>();

        // Iterate over each Schedule and convert it to a ScheduleDTO
        for (Schedule schedule : schedules) {
            ScheduleDTO scheduleDTO = convertSchedule(schedule);  // Convert Schedule to ScheduleDTO
            scheduleDTOs.add(scheduleDTO);  // Add the converted DTO to the list
        }

        // Return the list of ScheduleDTOs
        return scheduleDTOs;
    }

    public Schedule convertDTOToSchedule(ScheduleDTO scheduleDTO){
        // Create a new Schedule object to hold the converted data
        Schedule schedule = new Schedule();

        // Copy properties from the ScheduleDTO to the new Schedule object
        BeanUtils.copyProperties(scheduleDTO, schedule);

        // Set the 'date' property of the Schedule object using the value from the DTO
        schedule.setDate(scheduleDTO.getDate());

        // Set the 'activities' property of the Schedule object using the value from the DTO
        schedule.setActivities(scheduleDTO.getActivities());

        // Set the 'employee' property of the Schedule object
        // Find all Employee entities by the list of employee IDs in the DTO and set them to the Schedule
        schedule.setEmployee(employeeRepository.findAllById(scheduleDTO.getEmployeeIds()));

        // Set the 'pets' property of the Schedule object
        // Find all Pet entities by the list of pet IDs in the DTO and set them to the Schedule
        schedule.setPets(petRepository.findAllById(scheduleDTO.getPetIds()));

        // Return the fully populated Schedule object
        return schedule;
    }

}