/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.model;

/**
 *
 * @author dissanay_im14028
 */
//employee abstract superclass
public abstract class Employee {

    private String firstName;
    private String lastName;

    private String socialSecurityNumber;
    
    
    //three argument constructor
    public Employee(String firstName, String lastName, String socialSecurityNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.socialSecurityNumber = socialSecurityNumber;
    }

    //set first name
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    //set last name
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // set social security number
    public void setSocialSecurityNumber(String socialSecurityNumber) {
        this.socialSecurityNumber = socialSecurityNumber;
    }

    // return first name
    public String getFirstName() {
        return firstName;
    }

    // return last name
    public String getLastName() {
        return lastName;
    }

    // return social security number
    public String getSocialSecurityNumber() {
        return socialSecurityNumber;
    }
    
    // return string representaion of employee object
    public String toString(){
        return String.format("%s %s\nsocial security number: %s", getFirstName(),getLastName(),getSocialSecurityNumber());
    }
    
    // abstract method overridden by concrete subclasses
    public abstract double earnings();
    
   
    
}
