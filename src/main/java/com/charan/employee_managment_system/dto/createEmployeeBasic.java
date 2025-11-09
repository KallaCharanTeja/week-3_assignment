package com.charan.employee_managment_system.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class createEmployeeBasic {
    
    @NotBlank(message = "this field cannot be empty")
    private String firstName;

    @NotBlank(message = "this field cannot be empty")
    private String lastName;

    @NotBlank(message = "this field cannot be empty")
    @Email
    private String email;

    private String address;

    private String phn;

    public String getPhn() {
        return phn;
    }

    public String getAddress() {
        return address;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }
}
