package com.charan.employee_managment_system.dto;

import jakarta.validation.constraints.NotBlank;

public class createEmployeewithPhn {

    @NotBlank(message = "this field cannot be empty")
    private String firstName;

    @NotBlank(message = "this field cannot be empty")
    private String lastName;

    @NotBlank(message = "this field cannot be empty")
    private String email;

    @NotBlank(message = "this field cannot be empty")
    private String phn;

    private String address;

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

    public String getPhn() {
        return phn;
    }



    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }



    public void setLastName(String lastName) {
        this.lastName = lastName;
    }



    public void setEmail(String email) {
        this.email = email;
    }



    public void setPhn(String phn) {
        this.phn = phn;
    }



    public void setAddress(String address) {
        this.address = address;
    }
    
}
