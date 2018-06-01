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

// salaried employee concrete class extends abstract class employee
public class SalariedEmployee extends Employee{
    
    private double weeklySalary;
    
    //four argument constructor
    public SalariedEmployee(String first, String last, String ssn, double salary){
        super(first,last,ssn); // pass to employee constructor
        setWeeklySalary(salary);
    }
    
    // set salary
    public void setWeeklySalary(double salary){
        if(salary >= 0.0)
            weeklySalary = salary;
        else
            throw new IllegalArgumentException("Weekly salary must be >= 0.0");
        
        
    }
    
    // return salary
    public double getWeeklySalary(){
        return weeklySalary;
    }
    
    // calculate earnings, override abstract method earnings in employee
    @Override
    public double earnings(){
        return getWeeklySalary();
    }
    
    // returns string representaion of salaried employee object
    @Override
    public String toString(){
        return String.format("salaried emploee: %s\n%s: $%,.2f",super.toString(),"weekly salary",getWeeklySalary());
    
    }
    
}
