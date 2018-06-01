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

// hourly employee extends employee
public class HourlyEmployee extends Employee{
    private double wage;
    private double hours;
    
    // fie argument constructor
    public HourlyEmployee(String first, String last, String ssn, double hourlyWage,double hoursWorked){
        
        super(first,last,ssn);
        setWage(hourlyWage);
        setHours(hoursWorked);
    
    }
    
    // set wage
    public void setWage( double hourlyWage){
        if(hourlyWage>= 0.0)
            wage = hourlyWage;
        else 
            throw new IllegalArgumentException("Hourly wage must be >= 0.0");
    }
    
    // return wage
    public double getWage()
    {
        return wage;
    }
    
    // set hours worked
    public void setHours( double hoursWorked)
    {
        if((hoursWorked >= 0.0 ) && (hoursWorked <= 168.0))
            hours = hoursWorked;
        else
            throw new IllegalArgumentException ("Hours worked must be >= 0.0 and <= 168.0");
    }
    
    // return hours worked
    public double getHours()
    {
        return hours;
    }
    
    //calculate earnings, override abstract method earnings in employee
    
    @Override
    public double earnings()
    {
        if(getHours() <= 40) // no over time
            return getWage()*getHours();
        else
            return 40*getWage() + (getHours() - 40 )*getWage()*1.5;
            
    }
    
    
    // return string representation of hourly employee object
    @Override
    public String toString()
    {
        return String.format("hourly employee: %s\n%s: $%,.2f; %s: %,.2f", 
                super.toString(),"hourly wage",getWage(),"hours worked",getHours());
    }
}
