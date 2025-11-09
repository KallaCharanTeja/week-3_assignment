package com.charan.employee_managment_system.Service;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.charan.employee_managment_system.dto.createEmployeeBasic;
import com.charan.employee_managment_system.dto.createEmployeewithPhn;
import com.charan.employee_managment_system.model.Employee;
import com.charan.employee_managment_system.repository.EmployeeRepository;
import jakarta.annotation.PostConstruct;

@Service
public class EmployeeService_jpa {

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

    public Optional<Employee> findByName(String firstName){
        return repo.findByfirstNameIgnoreCase(firstName);
    }

    public Optional<Employee> findByEmail(String email){
        return repo.findByEmail(email);
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
    public String deleteEmployeeByMail(String email) {
        if(!repo.existsByemail(email)){
            return "There is no employee with email : " + email;
        }
        repo.deleteByemail(email);
        return "Employee deleted with email "+email + "successfully";
    }

    public String updateEmployeePhn(Long id,String phn){
        Optional<Employee> optemp = repo.findByid(id);
        if (optemp.isEmpty())
            return "Employee with ID " + id + " not found";
        
        Employee emp = optemp.get();
        emp.setPhn(phn);
        repo.save(emp);

        return "Phone Number of Employee with id "+ id + " updated successfully (Remaining fields are ignored if any)"; 
    }

    @Transactional
    public ResponseEntity<Map<String, Object>> updateEmployeeDetails(Long id, Map<String,Object> req){
        // Employee emp = repo.findByid(id)
        // .orElseThrow(() -> new Exception(
        //                 "Employee with Id " + id + " not found"));

        Optional<Employee> optemp = repo.findByid(id);
        if (optemp.isEmpty())
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("message","Employee with ID "+id+ "Not found"));
        
        Employee emp = optemp.get();
        if(req.containsKey("lastName")) 
            emp.setLastName((String) req.get("lastName"));

        if(req.containsKey("phn"))
            emp.setPhn((String) req.get("phn"));

        if(req.containsKey("address"))
            emp.setAddress((String) req.get("address"));

        repo.save(emp);

        return ResponseEntity.ok(Map.of(
        "message", "Employee details updated successfully",
        "id", id,
        "updatedFields", req.keySet(),
        "Note","If there are any other fields in the body they will be ignored",
        "updated employee details",emp
    ));
    }
    
}