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
//CommissionEmploye class extends Employee
public class CommissionEmployee extends Employee{
    
    private double grossSales; // get weekly sales
    private double commissionRate; // commission pecentage
    
    // five argument constructor
    public CommissionEmployee(String first, String last, String ssn, double sales, double rate)
    {
        super (first, last, ssn);
        setGrossSales(sales);
        setCommissionRate(rate);
    }

    // set gross sales amount
    public void setGrossSales(double sales) {
        if (sales >= 0.0)
            grossSales = sales;
        else
            throw new IllegalArgumentException("Gross sales must be >= 0.0");
    }

    // set commission rate
    public void setCommissionRate(double rate) {
        if (rate > 0.0 && rate < 1.0)
            commissionRate = rate;
        else
            throw new IllegalArgumentException("Commission rate must be > 0.0 and < 1.0");
    }
    

    // return gross sales amount
    public double getGrossSales() {
        return grossSales;
    }

    // return commission rate
    public double getCommissionRate() {
        return commissionRate;
    }
    
    //calculate earnings, override abstract method earnings in employee
    @Override
    public double earnings()
    {
        return getCommissionRate()*getGrossSales();
    }
    
    // return string representation of commission employee object
     @Override
    public String toString()
    {
        return String.format("%s: %s\n%s: $%,.2f; %s: %.2f",
                "commission employee",super.toString(),"gross sales",getGrossSales(),"commission rate",getCommissionRate());
    }
    
    
}
