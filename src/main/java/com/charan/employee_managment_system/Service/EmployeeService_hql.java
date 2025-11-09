package com.charan.employee_managment_system.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.charan.employee_managment_system.dto.createEmployeeBasic;
import com.charan.employee_managment_system.dto.createEmployeewithPhn;
import com.charan.employee_managment_system.model.Employee;
import com.charan.employee_managment_system.repository.EmployeeRepository;

import jakarta.annotation.PostConstruct;

@Service
public class EmployeeService_hql {

    @Autowired
    EmployeeRepository repo;
    // List<Employee> emps = Arrays.asList(new Employee("charan","teja","charan@gmail.com","address","8977279201"));

    // public List<Employee> getEmployees(){
    //     return emps;
    // }

    // public Employee getEmployeeById(Long id) {
    //     return emps.stream()
    //                 .filter(p -> p.getId() == id)
    //                 .findFirst().get();
    // }
    @PostConstruct
    public void init() {
        if (repo.count() == 0) {  // only insert once if DB is empty
            Employee e1 = new Employee("Kalla", "Charan", "charan@gmail.com", "VZM", "1234567890");
            Employee e2 = new Employee("Ravi", "Teja", "ravi@example.com", "Hyderabad", "9876543210");
            Employee e3 = new Employee("Sai", "Kiran", "sai.kiran@example.com", "Bangalore", "9988776655");

            repo.saveAll(List.of(e1, e2, e3));
            System.out.println("Initial employees added from service layer!");
        } else {
            System.out.println("Employees already exist, skipping seeding.");
        }
    }

    public Optional<Employee> findByNameHQL(String firstName){
        return repo.findByfirstNameIgnoreCaseHQL(firstName);
    }

    public Optional<Employee> findByEmailHQL(String email){
        return repo.findByEmailHQL(email);
    }

    public Employee createEmployeeWithPhn(createEmployeewithPhn request) {
    Employee emp = new Employee(
        request.getFirstName(),
        request.getLastName(),
        request.getEmail(),
        request.getAddress(),
        request.getPhn()
    );
    return repo.save(emp);
    }

    public Employee createEmployeeBasic(createEmployeeBasic request){
        Employee emp = new Employee(
        request.getFirstName(),
        request.getLastName(),
        request.getEmail(),
        request.getAddress(),
        request.getPhn()
    );
    return repo.save(emp);
    }

    @Transactional
    public String deleteEmployeeByMailHQL(String email) {
        if(!repo.existsByemailHQL(email)){
            return "There is no employee with email : " + email;
        }
        repo.deleteByemailHQL(email);
        return "Employee deleted with email "+email + "successfully";
    }

    public String updateEmployeePhn(Long id,String phn){

        int updated =  repo.updateEmployeePhnHQL(id, phn);
        if(updated > 0)
            return "Phone number updated successfully for ID : "+id + "  (NOTE: if there any extra fields this api will ignore them)";

        return "There is no Employee with ID : " + id+", please check once again"; 
    }

    @Transactional
    public String updateEmployeeDetails(Long id, Map<String,Object> req){
        // Employee emp = repo.findByid(id)
        // .orElseThrow(() -> new Exception(
        //                 "Employee with Id " + id + " not found"));

        String phn = (String) req.getOrDefault("phn",null);
        String address = (String) req.getOrDefault("address",null);
        String lastName = (String) req.getOrDefault("lastName",null);

        int updated = repo.updateEmployeeDetailsHQL(id,phn,address,lastName);
        if(updated > 0)
            return "Employee with id "+ id + " updated successfully"; 
        return "Employee with id "+id+" not found";
    }
    
}