package com.charan.employee_managment_system.controller;

import com.charan.employee_managment_system.Service.EmployeeService_jpa;
import com.charan.employee_managment_system.Service.EmployeeService_hql;
import com.charan.employee_managment_system.Service.EmployeeService_sql;
import com.charan.employee_managment_system.dto.createEmployeeBasic;
import com.charan.employee_managment_system.dto.createEmployeewithPhn;
import com.charan.employee_managment_system.model.Employee;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService_jpa jpaService;

    @Autowired
    private EmployeeService_hql hqlService;

    @Autowired
    private EmployeeService_sql sqlService;

    @GetMapping("/{firstName}")
    public ResponseEntity<?> getByName(
            @PathVariable String firstName,
            @RequestParam(defaultValue = "jpa") String mode) {

        Optional<Employee> emp = switch (mode.toLowerCase()) {
            case "hql" -> hqlService.findByNameHQL(firstName);
            case "sql" -> sqlService.findByNameSQL(firstName);
            default -> jpaService.findByName(firstName);
        };

        return emp.<ResponseEntity<?>>map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<?> getByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "jpa") String mode) {

        Optional<Employee> emp = switch (mode.toLowerCase()) {
            case "hql" -> hqlService.findByEmailHQL(email);
            case "sql" -> sqlService.findByEmailSQL(email);
            default -> jpaService.findByEmail(email);
        };

        return emp.<ResponseEntity<?>>map(ResponseEntity::ok)
                  .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<?> createEmployeeWithPhn(
            @RequestBody createEmployeewithPhn req,
            @RequestParam(defaultValue = "jpa") String mode) {

        Employee emp = switch (mode.toLowerCase()) {
            case "hql" -> hqlService.createEmployeeWithPhn(req);
            case "sql" -> sqlService.createEmployeeWithPhn(req);
            default -> jpaService.createEmployeeWithPhn(req);
        };
        return ResponseEntity.ok(emp);
    }

    @PostMapping("/create/basic")
    public ResponseEntity<?> createEmployeeBasic(
            @RequestBody createEmployeeBasic req,
            @RequestParam(defaultValue = "jpa") String mode) {

        Employee emp = switch (mode.toLowerCase()) {
            default -> jpaService.createEmployeeBasic(req);
        };
        return ResponseEntity.ok(emp);
    }

    @DeleteMapping("/delete/{email}")
    public ResponseEntity<String> deleteByEmail(
            @PathVariable String email,
            @RequestParam(defaultValue = "jpa") String mode) {

        String result = switch (mode.toLowerCase()) {
            case "hql" -> hqlService.deleteEmployeeByMailHQL(email);
            case "sql" -> sqlService.deleteEmployeeByMailSQL(email);
            default -> jpaService.deleteEmployeeByMail(email);
        };

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-phn/{id}")
    public ResponseEntity<String> updatePhone(
            @PathVariable Long id,
            @RequestParam String phn,
            @RequestParam(defaultValue = "jpa") String mode) {

        String result = switch (mode.toLowerCase()) {
            case "hql" -> hqlService.updateEmployeePhn(id, phn);
            case "sql" -> sqlService.updateEmployeePhn(id, phn);
            default -> jpaService.updateEmployeePhn(id, phn);
        };

        return ResponseEntity.ok(result);
    }

    @PutMapping("/update-details/{id}")
    public ResponseEntity<?> updateEmployeeDetails(
            @PathVariable Long id,
            @RequestBody Map<String, Object> req,
            @RequestParam(defaultValue = "jpa") String mode) {

        return switch (mode.toLowerCase()) {
            case "hql" -> ResponseEntity.ok(hqlService.updateEmployeeDetails(id, req));
            case "sql" -> ResponseEntity.ok(sqlService.updateEmployeeDetails(id, req));
            default -> jpaService.updateEmployeeDetails(id, req);
        };
    }
}
