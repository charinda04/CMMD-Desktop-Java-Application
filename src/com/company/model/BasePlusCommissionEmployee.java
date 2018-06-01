    /*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.model;

/**
 *
 * @author Charinda
 */
//BasePlusCommissionEmployee class extends CommissionEmployee

public class BasePlusCommissionEmployee extends CommissionEmployee {
    
    private double baseSalary; // base salary per week
    
    // six argument constructor
    public BasePlusCommissionEmployee( String first, String last, String ssn, double sales, double rate, double salary)
    {
        super(first, last, ssn, sales, rate);
        setBaseSalary(salary);
     
    }

    // set base salary
    public void setBaseSalary(double salary) {
        if (salary >= 0.0)
            baseSalary = salary;
        else
            throw new IllegalArgumentException("Base salary must be >= 0.0");
    }

    // return base salary
    public double getBaseSalary() {
        return baseSalary;
    }
    
    //calculate earnings, override abstract method earnings in CommissionEmployee
    @Override
    public double earnings()
    {
        return getBaseSalary()+super.earnings();
    }
    
    // return string representation of BasePlusCommissionEmployee object
     @Override
    public String toString()
    {
        return String.format("%s %s; %s: $%,.2f","base-salaried",super.toString(),"base salary",getBaseSalary());
    }
    
    
}
