package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.user.EmployeeSkill;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
public class EmployeeService {

    @Autowired
    private EmployeeRepository employeeRepository;

    //Get employee by employeeId
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId).orElse(null);
    }

    public List<Employee> getEmployeesByService(LocalDate date, Set<EmployeeSkill> skills) {
        List<Employee> result = new ArrayList<>(); // List containing found employees

        // Get the list of available employees on the corresponding weekday
        List<Employee> employees = employeeRepository.findByDaysAvailable(date.getDayOfWeek());

        // Filter through the list of employees
        for (Employee employee : employees) {
            // Check if employee has all required skills
            if (employee.getSkills().containsAll(skills)) {
                result.add(employee); // If satisfied, add to the result list
            }
        }

        return result; // Returns a list of matching employees
    }

    public void setEmployeeAvailability(Set<DayOfWeek> days, Long employeeId) {
        // Check if Employee exists before retrieving it
        if (!employeeRepository.existsById(employeeId)) {
            throw new IllegalArgumentException("Employee not found with id: " + employeeId);
        }

        // If exists, get Employee from database
        Employee employee = employeeRepository.getOne(employeeId);

        // Update availability information
        employee.setDaysAvailable(days);

        // Save Employee after changes
        employeeRepository.save(employee);
    }

    // save the employee
    public Employee saveEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

}