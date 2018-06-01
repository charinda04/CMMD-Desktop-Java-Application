/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.company.gui;

import com.company.connector.JavaConnect;
import com.company.model.BasePlusCommissionEmployee;
import com.company.model.CommissionEmployee;
import com.company.model.Employee;
import com.company.model.HourlyEmployee;
import com.company.model.SalariedEmployee;
import com.itextpdf.text.Document;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;
import java.awt.Desktop;
import java.io.File;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.*;
import java.util.GregorianCalendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.WARNING_MESSAGE;
import net.proteanit.sql.DbUtils;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Charinda
 */
public class MainMenu extends javax.swing.JFrame {

    /**
     * Creates new form MainMenu
     */
    
    Connection conn;
    PreparedStatement pst;
    ResultSet rs;
    
    
    
    ResultSet rs1;
    ResultSet rs2;
    ResultSet rs3;
    ResultSet rs4;
    
    Employee e[] = new Employee[4];
    ArrayList<SalariedEmployee> se2= new ArrayList<>();
    
    
    SalariedEmployee se1 = new SalariedEmployee("","","",0.0);
    HourlyEmployee he1 = new HourlyEmployee("","","",0.0,0.0);
    CommissionEmployee ce1 = new CommissionEmployee("","","",0.0,0.1);
    BasePlusCommissionEmployee bpce1 = new BasePlusCommissionEmployee("","","",0.0,0.1,0.0);
    
    SalariedEmployee se;
    HourlyEmployee he;
    CommissionEmployee ce;
    BasePlusCommissionEmployee bpce;
    //ArrayList<Double> list = new ArrayList<>();
    
    String sssn;
    String hssn;
    String cssn;
    String bcssn;
    
    int year;
    int month;
    int day;
    int week;
    
    
    public MainMenu() {
        initComponents();
        conn = JavaConnect.ConnectorDb();
        
        //fillSalariedComboBox();
        updateSalariedEmployeeTable();
        updateHourlyEmployeeTable();
        updateCommissionEmployeeTable();
        updateBasePlusCommissionEmployeeTable();
        currentDateTime();
        paySalarySE();
        paySalaryHE();
        paySalaryCE();
        paySalaryBPCE();
        updatePayWETable();
        updatePayHETable();
        updatePayCETable();
        updatePayBPCETable();
    }
    
    public void currentDateTime(){
        Calendar cal = new GregorianCalendar();
        month = cal.get(Calendar.MONTH) + 1;
        year = cal.get(Calendar.YEAR);
        day = cal.get(Calendar.DAY_OF_MONTH);
        
        week = cal.get(Calendar.WEEK_OF_YEAR);
        
        //second = cal.get(Calendar.SECOND) ;
        //minute = cal.get(Calendar.MINUTE);
        //hour = cal.get(Calendar.HOUR_OF_DAY
    }

    public void updatePayBPCETable()
    {
       try {
            String sql = "SELECT employee.`Social_Security_Number`,`First_Name`,`Last_Name`,`Earnings` FROM `salary`,`employee` "
                    + "WHERE `Week` = ? AND employee.`Social_Security_Number` = salary.`Social_Security_Number` AND `Base_Plus_Commission` = '1';";
            pst =  conn.prepareStatement(sql);
            pst.setString(1,String.format("%d",week));
            rs   = pst.executeQuery();
            pbcTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    
    public void updatePayCETable()
    {
       try {
            String sql = "SELECT employee.`Social_Security_Number`,`First_Name`,`Last_Name`,`Earnings` FROM `salary`,`employee` "
                    + "WHERE `Week` = ? AND employee.`Social_Security_Number` = salary.`Social_Security_Number` AND `Commission` = '1';";
            pst =  conn.prepareStatement(sql);
            pst.setString(1,String.format("%d",week));
            rs   = pst.executeQuery();
            pceTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    
    
    public void updatePayHETable()
    {
       try {
            String sql = "SELECT employee.`Social_Security_Number`,`First_Name`,`Last_Name`,`Earnings` FROM `salary`,`employee` "
                    + "WHERE `Week` = ? AND employee.`Social_Security_Number` = salary.`Social_Security_Number` AND `Hourly` = '1';";
            pst =  conn.prepareStatement(sql);
            pst.setString(1,String.format("%d",week));
            rs   = pst.executeQuery();
            pheTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    
    
    public void updatePayWETable()
    {
       try {
            String sql = "SELECT employee.`Social_Security_Number`,`First_Name`,`Last_Name`,`Earnings` FROM `salary`,`employee` "
                    + "WHERE `Week` = ? AND employee.`Social_Security_Number` = salary.`Social_Security_Number` AND `Salaried` = '1';";
            pst =  conn.prepareStatement(sql);
            pst.setString(1,String.format("%d",week));
            rs   = pst.executeQuery();
            pweTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }
    
    public void paySalaryBPCE()
    {
        try {
            String sql = "SELECT * FROM `employee` WHERE `Base_Plus_Commission` = '1';";
            pst = conn.prepareStatement(sql);
            rs4 = pst.executeQuery();
            
            
            if(rs4.first())
            {
                pbcssnTxt.setText(rs4.getString("Social_Security_Number"));
                pbcfnTxt.setText(rs4.getString("First_Name"));
                pbclnTxt.setText(rs4.getString("Last_Name"));
                pbcbsTxt.setText(rs4.getString("Base_Salary"));
                pbccrTxt.setText(rs4.getString("Commission_Rate"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    

    
    public void paySalaryCE()
    {
        try {
            String sql = "SELECT * FROM `employee` WHERE `Commission` = '1';";
            pst = conn.prepareStatement(sql);
            rs3 = pst.executeQuery();
            
            
            if(rs3.first())
            {
                pcssnTxt.setText(rs3.getString("Social_Security_Number"));
                pcfnTxt.setText(rs3.getString("First_Name"));
                pclnTxt.setText(rs3.getString("Last_Name"));
                pccrTxt.setText(rs3.getString("Commission_Rate"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    

    
    public void paySalaryHE()
    {
        try {
            String sql = "SELECT * FROM `employee` WHERE `Hourly` = '1';";
            pst = conn.prepareStatement(sql);
            rs2 = pst.executeQuery();
            
            
            if(rs2.first())
            {
                phssnTxt.setText(rs2.getString("Social_Security_Number"));
                phfnTxt.setText(rs2.getString("First_Name"));
                phlnTxt.setText(rs2.getString("Last_Name"));
                phhwTxt.setText(rs2.getString("Hourly_Wage"));
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    
    
    public void paySalarySE()
    {
        try {
            String sql = "SELECT * FROM `employee` WHERE `Salaried` = '1';";
            pst = conn.prepareStatement(sql);
            rs1 = pst.executeQuery();
            
            
            if(rs1.first())
            {
                psssnTxt.setText(rs1.getString("Social_Security_Number"));
                psfnTxt.setText(rs1.getString("First_Name"));
                pslnTxt.setText(rs1.getString("Last_Name"));
                pswsTxt.setText(rs1.getString("Weekly_Salary"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    
    public void updateBasePlusCommissionEmployeeTable()
    {
        try {
            String sql = "SELECT `Social_Security_Number`,`First_Name`,`Last_Name`,`Base_Salary`,`Commission_Rate` FROM `employee` WHERE `Base_Plus_Commission` = '1';";
            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            rs   = pst.executeQuery();
            bcTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }    
    
    
    public void updateCommissionEmployeeTable()
    {
        
        try {
            String sql = "SELECT `Social_Security_Number`,`First_Name`,`Last_Name`,`Commission_Rate` FROM `employee` WHERE `Commission` = '1';";
            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            rs   = pst.executeQuery();
            cTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }    
    
    
    
    public void updateSalariedEmployeeTable()
    {
        try {
            String sql = "SELECT `Social_Security_Number`,`First_Name`,`Last_Name`,`Weekly_Salary` FROM `employee` WHERE `Salaried` = '1';";
            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            rs   = pst.executeQuery();
            sTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void updateHourlyEmployeeTable()
    {
        try {
            String sql = "SELECT `Social_Security_Number`,`First_Name`,`Last_Name`,`Hourly_Wage` FROM `employee` WHERE `Hourly` = '1';";
            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            rs   = pst.executeQuery();
            hTable.setModel(DbUtils.resultSetToTableModel(rs));
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jPanel16 = new javax.swing.JPanel();
        jLabel46 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        jPanel10 = new javax.swing.JPanel();
        jPanel14 = new javax.swing.JPanel();
        saddBtn = new javax.swing.JButton();
        sssnTxt = new javax.swing.JTextField();
        sfnTxt = new javax.swing.JTextField();
        slnTxt = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        swsTxt = new javax.swing.JTextField();
        jPanel15 = new javax.swing.JPanel();
        supdateBtn = new javax.swing.JButton();
        sdeleteBtn = new javax.swing.JButton();
        jLabel38 = new javax.swing.JLabel();
        jLabel39 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        sTable = new javax.swing.JTable();
        jPanel11 = new javax.swing.JPanel();
        jPanel17 = new javax.swing.JPanel();
        hAddBtn = new javax.swing.JButton();
        hssnTxt = new javax.swing.JTextField();
        hfnTxt = new javax.swing.JTextField();
        hlnTxt = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel18 = new javax.swing.JLabel();
        hhwTxt = new javax.swing.JTextField();
        jPanel18 = new javax.swing.JPanel();
        hUpdateBtn = new javax.swing.JButton();
        jLabel8 = new javax.swing.JLabel();
        hDeleteBtn = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        hTable = new javax.swing.JTable();
        jPanel12 = new javax.swing.JPanel();
        jPanel19 = new javax.swing.JPanel();
        cAddBtn = new javax.swing.JButton();
        cssnTxt = new javax.swing.JTextField();
        cfnTxt = new javax.swing.JTextField();
        clnTxt = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel19 = new javax.swing.JLabel();
        ccrTxt = new javax.swing.JTextField();
        jPanel20 = new javax.swing.JPanel();
        cUpdateBtn = new javax.swing.JButton();
        cDeleteBtn = new javax.swing.JButton();
        jLabel40 = new javax.swing.JLabel();
        jLabel41 = new javax.swing.JLabel();
        jScrollPane3 = new javax.swing.JScrollPane();
        cTable = new javax.swing.JTable();
        jPanel13 = new javax.swing.JPanel();
        jPanel21 = new javax.swing.JPanel();
        bcAddBtn = new javax.swing.JButton();
        bcssnTxt = new javax.swing.JTextField();
        bcfnTxt = new javax.swing.JTextField();
        bclnTxt = new javax.swing.JTextField();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel20 = new javax.swing.JLabel();
        bcbsTxt = new javax.swing.JTextField();
        jLabel21 = new javax.swing.JLabel();
        bccrTxt = new javax.swing.JTextField();
        jPanel22 = new javax.swing.JPanel();
        bcUpdateBtn = new javax.swing.JButton();
        jLabel16 = new javax.swing.JLabel();
        bcDeleteBtn = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jScrollPane4 = new javax.swing.JScrollPane();
        bcTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel6 = new javax.swing.JPanel();
        jPanel23 = new javax.swing.JPanel();
        psfnTxt = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        psssnTxt = new javax.swing.JTextField();
        jLabel23 = new javax.swing.JLabel();
        pswsTxt = new javax.swing.JTextField();
        jButton19 = new javax.swing.JButton();
        jLabel22 = new javax.swing.JLabel();
        jLabel24 = new javax.swing.JLabel();
        jButton4 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel42 = new javax.swing.JLabel();
        pslnTxt = new javax.swing.JTextField();
        jScrollPane5 = new javax.swing.JScrollPane();
        pweTable = new javax.swing.JTable();
        jPanel7 = new javax.swing.JPanel();
        jPanel24 = new javax.swing.JPanel();
        phfnTxt = new javax.swing.JTextField();
        jButton20 = new javax.swing.JButton();
        jButton21 = new javax.swing.JButton();
        phssnTxt = new javax.swing.JTextField();
        jLabel25 = new javax.swing.JLabel();
        phhwTxt = new javax.swing.JTextField();
        payHourlyEmployeeSalary = new javax.swing.JButton();
        jLabel26 = new javax.swing.JLabel();
        jLabel27 = new javax.swing.JLabel();
        jButton23 = new javax.swing.JButton();
        jButton24 = new javax.swing.JButton();
        jLabel34 = new javax.swing.JLabel();
        phwTxt = new javax.swing.JTextField();
        jLabel43 = new javax.swing.JLabel();
        phlnTxt = new javax.swing.JTextField();
        jScrollPane6 = new javax.swing.JScrollPane();
        pheTable = new javax.swing.JTable();
        jPanel8 = new javax.swing.JPanel();
        jPanel25 = new javax.swing.JPanel();
        pcfnTxt = new javax.swing.JTextField();
        jButton25 = new javax.swing.JButton();
        jButton26 = new javax.swing.JButton();
        pcssnTxt = new javax.swing.JTextField();
        jLabel28 = new javax.swing.JLabel();
        pccrTxt = new javax.swing.JTextField();
        jButton27 = new javax.swing.JButton();
        jLabel29 = new javax.swing.JLabel();
        jLabel30 = new javax.swing.JLabel();
        jButton28 = new javax.swing.JButton();
        jButton29 = new javax.swing.JButton();
        jLabel35 = new javax.swing.JLabel();
        pcgsTxt = new javax.swing.JTextField();
        pclnTxt = new javax.swing.JTextField();
        jLabel44 = new javax.swing.JLabel();
        jScrollPane7 = new javax.swing.JScrollPane();
        pceTable = new javax.swing.JTable();
        jPanel9 = new javax.swing.JPanel();
        jPanel26 = new javax.swing.JPanel();
        pbcfnTxt = new javax.swing.JTextField();
        jButton30 = new javax.swing.JButton();
        jButton31 = new javax.swing.JButton();
        pbcssnTxt = new javax.swing.JTextField();
        jLabel31 = new javax.swing.JLabel();
        pbcbsTxt = new javax.swing.JTextField();
        jButton32 = new javax.swing.JButton();
        jLabel32 = new javax.swing.JLabel();
        jLabel33 = new javax.swing.JLabel();
        jButton33 = new javax.swing.JButton();
        jButton34 = new javax.swing.JButton();
        jLabel36 = new javax.swing.JLabel();
        pbccrTxt = new javax.swing.JTextField();
        jLabel37 = new javax.swing.JLabel();
        pbcgsTxt = new javax.swing.JTextField();
        jLabel45 = new javax.swing.JLabel();
        pbclnTxt = new javax.swing.JTextField();
        jScrollPane8 = new javax.swing.JScrollPane();
        pbcTable = new javax.swing.JTable();
        jPanel27 = new javax.swing.JPanel();
        jPanel29 = new javax.swing.JPanel();
        jButton15 = new javax.swing.JButton();
        SalariedEmployeeReportBtn = new javax.swing.JButton();
        jButton17 = new javax.swing.JButton();
        jButton18 = new javax.swing.JButton();
        jButton22 = new javax.swing.JButton();
        jLabel47 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("CMMD - Enterprices");

        jPanel1.setBackground(new java.awt.Color(255, 255, 153));

        jPanel2.setBackground(new java.awt.Color(51, 153, 255));
        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Main Menu"));

        jButton1.setBackground(new java.awt.Color(255, 255, 204));
        jButton1.setText("Employee");
        jButton1.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setBackground(new java.awt.Color(255, 255, 204));
        jButton2.setText("Pay Salary");
        jButton2.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jButton7.setBackground(new java.awt.Color(255, 255, 204));
        jButton7.setText("Pay Sheet");
        jButton7.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jButton8.setBackground(new java.awt.Color(255, 255, 204));
        jButton8.setText("Log Out");
        jButton8.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setBackground(new java.awt.Color(255, 255, 204));
        jButton9.setText("Exit");
        jButton9.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(jButton7, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE)
                    .addComponent(jButton9, javax.swing.GroupLayout.DEFAULT_SIZE, 154, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(25, 25, 25)
                .addComponent(jButton1)
                .addGap(25, 25, 25)
                .addComponent(jButton2)
                .addGap(25, 25, 25)
                .addComponent(jButton7)
                .addGap(25, 25, 25)
                .addComponent(jButton8)
                .addGap(25, 25, 25)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel3.setLayout(new java.awt.CardLayout());

        jPanel16.setLayout(null);

        jLabel46.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imges/Untitled-1_1.jpg"))); // NOI18N
        jLabel46.setText("jLabel46");
        jPanel16.add(jLabel46);
        jLabel46.setBounds(-20, -30, 850, 760);

        jPanel3.add(jPanel16, "card4");

        jPanel14.setBorder(javax.swing.BorderFactory.createTitledBorder("Add"));

        saddBtn.setText("Add");
        saddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saddBtnActionPerformed(evt);
            }
        });

        slnTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                slnTxtActionPerformed(evt);
            }
        });

        jLabel3.setText("Last Name");

        jLabel2.setText("First Name");

        jLabel1.setText("Social Security No");

        jLabel17.setText("Weekly Salary");

        javax.swing.GroupLayout jPanel14Layout = new javax.swing.GroupLayout(jPanel14);
        jPanel14.setLayout(jPanel14Layout);
        jPanel14Layout.setHorizontalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3))
                .addGap(60, 60, 60)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(sssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 161, Short.MAX_VALUE)
                        .addComponent(jLabel17)
                        .addGap(18, 18, 18)
                        .addComponent(swsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 91, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(slnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel14Layout.createSequentialGroup()
                        .addComponent(sfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(saddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel14Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {sssnTxt, swsTxt});

        jPanel14Layout.setVerticalGroup(
            jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel14Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(sssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel17)
                    .addComponent(swsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(sfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(saddBtn))
                .addGap(30, 30, 30)
                .addGroup(jPanel14Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(slnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel15.setBorder(javax.swing.BorderFactory.createTitledBorder("Update & Delete"));

        supdateBtn.setText("Update");
        supdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supdateBtnActionPerformed(evt);
            }
        });

        sdeleteBtn.setText("Delete");
        sdeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sdeleteBtnActionPerformed(evt);
            }
        });

        jLabel38.setText("Select a row to update");

        jLabel39.setText("Select a row to delete");

        javax.swing.GroupLayout jPanel15Layout = new javax.swing.GroupLayout(jPanel15);
        jPanel15.setLayout(jPanel15Layout);
        jPanel15Layout.setHorizontalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addGap(30, 30, 30)
                .addComponent(jLabel38)
                .addGap(88, 88, 88)
                .addComponent(supdateBtn)
                .addGap(40, 40, 40)
                .addComponent(jLabel39)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 187, Short.MAX_VALUE)
                .addComponent(sdeleteBtn)
                .addContainerGap())
        );
        jPanel15Layout.setVerticalGroup(
            jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel15Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel15Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(sdeleteBtn)
                    .addComponent(jLabel38)
                    .addComponent(jLabel39)
                    .addComponent(supdateBtn))
                .addContainerGap())
        );

        sTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Social Security Number", "First Name", "Last Name", "Weekly Salary"
            }
        ));
        sTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                sTableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(sTable);

        javax.swing.GroupLayout jPanel10Layout = new javax.swing.GroupLayout(jPanel10);
        jPanel10.setLayout(jPanel10Layout);
        jPanel10Layout.setHorizontalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel14, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel15, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane1))
                .addContainerGap())
        );
        jPanel10Layout.setVerticalGroup(
            jPanel10Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel10Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel14, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel15, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Salaried Employee", jPanel10);

        jPanel17.setBorder(javax.swing.BorderFactory.createTitledBorder("Add"));

        hAddBtn.setText("Add");
        hAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hAddBtnActionPerformed(evt);
            }
        });

        hssnTxt.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

        hlnTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hlnTxtActionPerformed(evt);
            }
        });

        jLabel5.setText("Last Name");

        jLabel6.setText("First Name");

        jLabel7.setText("Social Security No");

        jLabel18.setText("Hourly Wage");

        javax.swing.GroupLayout jPanel17Layout = new javax.swing.GroupLayout(jPanel17);
        jPanel17.setLayout(jPanel17Layout);
        jPanel17Layout.setHorizontalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel7)
                    .addComponent(jLabel6)
                    .addComponent(jLabel5))
                .addGap(60, 60, 60)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addComponent(hssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel18))
                    .addGroup(jPanel17Layout.createSequentialGroup()
                        .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(hlnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGap(18, 18, 18)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(hhwTxt, javax.swing.GroupLayout.DEFAULT_SIZE, 41, Short.MAX_VALUE)
                    .addComponent(hAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel17Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {hhwTxt, hssnTxt});

        jPanel17Layout.setVerticalGroup(
            jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel17Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel7)
                    .addComponent(hssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel18)
                    .addComponent(hhwTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(29, 29, 29)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(hfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(hAddBtn))
                .addGap(30, 30, 30)
                .addGroup(jPanel17Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(hlnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel17Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {hhwTxt, hssnTxt});

        jPanel18.setBorder(javax.swing.BorderFactory.createTitledBorder("Update & Delete"));

        hUpdateBtn.setText("Update");
        hUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hUpdateBtnActionPerformed(evt);
            }
        });

        jLabel8.setText("Select a row to update");

        hDeleteBtn.setText("Delete");
        hDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hDeleteBtnActionPerformed(evt);
            }
        });

        jLabel4.setText("Select a row to delete");

        javax.swing.GroupLayout jPanel18Layout = new javax.swing.GroupLayout(jPanel18);
        jPanel18.setLayout(jPanel18Layout);
        jPanel18Layout.setHorizontalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(95, 95, 95)
                .addComponent(hUpdateBtn)
                .addGap(96, 96, 96)
                .addComponent(jLabel4)
                .addGap(18, 18, 18)
                .addComponent(hDeleteBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel18Layout.setVerticalGroup(
            jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel18Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel18Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(hUpdateBtn)
                    .addComponent(hDeleteBtn)
                    .addComponent(jLabel4)
                    .addComponent(jLabel8))
                .addContainerGap())
        );

        hTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Social Security Number", "First Name", "Last Name", "Hourly Wage"
            }
        ));
        hTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                hTableMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(hTable);

        javax.swing.GroupLayout jPanel11Layout = new javax.swing.GroupLayout(jPanel11);
        jPanel11.setLayout(jPanel11Layout);
        jPanel11Layout.setHorizontalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel17, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel18, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane2))
                .addContainerGap())
        );
        jPanel11Layout.setVerticalGroup(
            jPanel11Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel11Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel17, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel18, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Hourly Employee", jPanel11);

        jPanel19.setBorder(javax.swing.BorderFactory.createTitledBorder("Add"));

        cAddBtn.setText("Add");
        cAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cAddBtnActionPerformed(evt);
            }
        });

        clnTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clnTxtActionPerformed(evt);
            }
        });

        jLabel9.setText("Last Name");

        jLabel10.setText("First Name");

        jLabel11.setText("Social Security No");

        jLabel19.setText("Commission Rate");

        javax.swing.GroupLayout jPanel19Layout = new javax.swing.GroupLayout(jPanel19);
        jPanel19.setLayout(jPanel19Layout);
        jPanel19Layout.setHorizontalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel9))
                .addGap(60, 60, 60)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(clnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(cssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(jLabel19)
                        .addGap(18, 18, 18)
                        .addComponent(ccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 28, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel19Layout.createSequentialGroup()
                        .addComponent(cfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(cAddBtn, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jPanel19Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {ccrTxt, cssnTxt});

        jPanel19Layout.setVerticalGroup(
            jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel19Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(cssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel19)
                    .addComponent(ccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(cfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cAddBtn))
                .addGap(30, 30, 30)
                .addGroup(jPanel19Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel9)
                    .addComponent(clnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        jPanel20.setBorder(javax.swing.BorderFactory.createTitledBorder("Update & Delete"));

        cUpdateBtn.setText("Update");
        cUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cUpdateBtnActionPerformed(evt);
            }
        });

        cDeleteBtn.setText("Delete");
        cDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cDeleteBtnActionPerformed(evt);
            }
        });

        jLabel40.setText("Select a row to update");

        jLabel41.setText("Select a row to delete");

        javax.swing.GroupLayout jPanel20Layout = new javax.swing.GroupLayout(jPanel20);
        jPanel20.setLayout(jPanel20Layout);
        jPanel20Layout.setHorizontalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel40)
                .addGap(108, 108, 108)
                .addComponent(cUpdateBtn)
                .addGap(193, 193, 193)
                .addComponent(jLabel41)
                .addGap(18, 18, 18)
                .addComponent(cDeleteBtn)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel20Layout.setVerticalGroup(
            jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel20Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel20Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(cUpdateBtn)
                    .addComponent(cDeleteBtn)
                    .addComponent(jLabel40)
                    .addComponent(jLabel41))
                .addContainerGap())
        );

        cTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Social Security Number", "First Name", "Last Name", "Commission Rate"
            }
        ));
        cTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                cTableMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(cTable);

        javax.swing.GroupLayout jPanel12Layout = new javax.swing.GroupLayout(jPanel12);
        jPanel12.setLayout(jPanel12Layout);
        jPanel12Layout.setHorizontalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel19, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel20, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane3))
                .addContainerGap())
        );
        jPanel12Layout.setVerticalGroup(
            jPanel12Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel12Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel19, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jPanel20, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Commission Employee", jPanel12);

        jPanel21.setBorder(javax.swing.BorderFactory.createTitledBorder("Add"));

        bcAddBtn.setText("Add");
        bcAddBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcAddBtnActionPerformed(evt);
            }
        });

        bclnTxt.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bclnTxtActionPerformed(evt);
            }
        });

        jLabel13.setText("Last Name");

        jLabel14.setText("First Name");

        jLabel15.setText("Social Security No");

        jLabel20.setText("Base Salary");

        jLabel21.setText("Commission Rate");

        javax.swing.GroupLayout jPanel21Layout = new javax.swing.GroupLayout(jPanel21);
        jPanel21.setLayout(jPanel21Layout);
        jPanel21Layout.setHorizontalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel13))
                .addGap(60, 60, 60)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(bcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(bcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel21)
                            .addComponent(jLabel20)))
                    .addGroup(jPanel21Layout.createSequentialGroup()
                        .addComponent(bclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 142, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(bcbsTxt)
                    .addComponent(bccrTxt)
                    .addComponent(bcAddBtn, javax.swing.GroupLayout.DEFAULT_SIZE, 117, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel21Layout.setVerticalGroup(
            jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel21Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel15)
                    .addComponent(bcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel20)
                    .addComponent(bcbsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(32, 32, 32)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel14)
                    .addComponent(bcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel21)
                    .addComponent(bccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(31, 31, 31)
                .addGroup(jPanel21Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(bclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(bcAddBtn))
                .addContainerGap(19, Short.MAX_VALUE))
        );

        jPanel22.setBorder(javax.swing.BorderFactory.createTitledBorder("Update & Delete"));

        bcUpdateBtn.setText("Update");
        bcUpdateBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcUpdateBtnActionPerformed(evt);
            }
        });

        jLabel16.setText("Select a row to update");

        bcDeleteBtn.setText("Delete");
        bcDeleteBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bcDeleteBtnActionPerformed(evt);
            }
        });

        jLabel12.setText("Select a row to delete");

        javax.swing.GroupLayout jPanel22Layout = new javax.swing.GroupLayout(jPanel22);
        jPanel22.setLayout(jPanel22Layout);
        jPanel22Layout.setHorizontalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addGap(105, 105, 105)
                .addComponent(bcUpdateBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 155, Short.MAX_VALUE)
                .addComponent(jLabel12)
                .addGap(73, 73, 73)
                .addComponent(bcDeleteBtn)
                .addContainerGap())
        );
        jPanel22Layout.setVerticalGroup(
            jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel22Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel22Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(bcUpdateBtn)
                    .addComponent(jLabel16)
                    .addComponent(bcDeleteBtn)
                    .addComponent(jLabel12))
                .addContainerGap())
        );

        bcTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Social Security Number", "First Name", "Last Name", "Base Salary", "Commission Rate"
            }
        ));
        bcTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                bcTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(bcTable);

        javax.swing.GroupLayout jPanel13Layout = new javax.swing.GroupLayout(jPanel13);
        jPanel13.setLayout(jPanel13Layout);
        jPanel13Layout.setHorizontalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel22, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jScrollPane4))
                .addContainerGap())
        );
        jPanel13Layout.setVerticalGroup(
            jPanel13Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel13Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(jPanel21, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(18, 18, 18)
                .addComponent(jPanel22, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 401, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        jTabbedPane1.addTab("Base Plus Commission Employee", jPanel13);

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane1)
        );

        jPanel3.add(jPanel4, "card2");

        jButton6.setText("Last");
        jButton6.setContentAreaFilled(false);
        jButton6.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton5.setText("First");
        jButton5.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jLabel23.setText("First Name");

        jButton19.setText("Pay Salary");
        jButton19.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton19.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton19ActionPerformed(evt);
            }
        });

        jLabel22.setText("Social Seccurity No");

        jLabel24.setText("Weekly Salary");

        jButton4.setText("Previous");
        jButton4.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton3.setText("Next");
        jButton3.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jLabel42.setText("Last Name");

        javax.swing.GroupLayout jPanel23Layout = new javax.swing.GroupLayout(jPanel23);
        jPanel23.setLayout(jPanel23Layout);
        jPanel23Layout.setHorizontalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel22)
                        .addGap(30, 30, 30)
                        .addComponent(psssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(psfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pswsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jLabel42)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pslnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel23Layout.createSequentialGroup()
                        .addComponent(jButton3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton6))
                    .addComponent(jButton19))
                .addGap(10, 10, 10))
        );
        jPanel23Layout.setVerticalGroup(
            jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel23Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel22)
                    .addComponent(psssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton3)
                    .addComponent(jButton4)
                    .addComponent(jButton5)
                    .addComponent(jButton6))
                .addGap(19, 19, 19)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel23)
                    .addComponent(psfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton19))
                .addGap(19, 19, 19)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel42)
                    .addComponent(pslnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel23Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel24)
                    .addComponent(pswsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pweTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane5.setViewportView(pweTable);

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane5)
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel23, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Salaried Employee", jPanel6);

        jButton20.setText("Last");
        jButton20.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton20ActionPerformed(evt);
            }
        });

        jButton21.setText("First");
        jButton21.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton21ActionPerformed(evt);
            }
        });

        jLabel25.setText("First Name");

        payHourlyEmployeeSalary.setText("Pay Salary");
        payHourlyEmployeeSalary.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                payHourlyEmployeeSalaryActionPerformed(evt);
            }
        });

        jLabel26.setText("Social Seccurity No");

        jLabel27.setText("Hourly Wage");

        jButton23.setText("Previous");
        jButton23.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton23ActionPerformed(evt);
            }
        });

        jButton24.setText("Next");
        jButton24.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton24ActionPerformed(evt);
            }
        });

        jLabel34.setText("Hours Worked");

        jLabel43.setText("Last Name");

        javax.swing.GroupLayout jPanel24Layout = new javax.swing.GroupLayout(jPanel24);
        jPanel24.setLayout(jPanel24Layout);
        jPanel24Layout.setHorizontalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel26)
                        .addGap(30, 30, 30)
                        .addComponent(phssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel25)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(phfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel27)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(phhwTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(phwTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jLabel43)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(phlnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel24Layout.createSequentialGroup()
                        .addComponent(jButton24)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton23)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton21)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton20))
                    .addComponent(payHourlyEmployeeSalary))
                .addContainerGap())
        );
        jPanel24Layout.setVerticalGroup(
            jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel24Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel26)
                    .addComponent(phssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton24)
                    .addComponent(jButton23)
                    .addComponent(jButton21)
                    .addComponent(jButton20))
                .addGap(19, 19, 19)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel25)
                    .addComponent(phfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(payHourlyEmployeeSalary))
                .addGap(19, 19, 19)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel43)
                    .addComponent(phlnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel27)
                    .addComponent(phhwTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel24Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel34)
                    .addComponent(phwTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(20, Short.MAX_VALUE))
        );

        pheTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane6.setViewportView(pheTable);

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane6)
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel24, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane6, javax.swing.GroupLayout.PREFERRED_SIZE, 411, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Hourly Employee", jPanel7);

        jButton25.setText("Last");
        jButton25.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton25ActionPerformed(evt);
            }
        });

        jButton26.setText("First");
        jButton26.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton26ActionPerformed(evt);
            }
        });

        jLabel28.setText("First Name");

        jButton27.setText("Pay Salary");
        jButton27.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton27ActionPerformed(evt);
            }
        });

        jLabel29.setText("Social Seccurity No");

        jLabel30.setText("Commission Rate");

        jButton28.setText("Previous");
        jButton28.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton28ActionPerformed(evt);
            }
        });

        jButton29.setText("Next");
        jButton29.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton29ActionPerformed(evt);
            }
        });

        jLabel35.setText("Gross Sales");

        jLabel44.setText("Last Name");

        javax.swing.GroupLayout jPanel25Layout = new javax.swing.GroupLayout(jPanel25);
        jPanel25.setLayout(jPanel25Layout);
        jPanel25Layout.setHorizontalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel29)
                        .addGap(30, 30, 30)
                        .addComponent(pcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel30)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel35)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pcgsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jLabel44)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel25Layout.createSequentialGroup()
                        .addComponent(jButton29)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton28)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton26)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton25))
                    .addComponent(jButton27))
                .addContainerGap())
        );
        jPanel25Layout.setVerticalGroup(
            jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel25Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel29)
                    .addComponent(pcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton29)
                    .addComponent(jButton28)
                    .addComponent(jButton26)
                    .addComponent(jButton25))
                .addGap(19, 19, 19)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel28)
                    .addComponent(pcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton27))
                .addGap(19, 19, 19)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel44)
                    .addComponent(pclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel30)
                    .addComponent(pccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel25Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel35)
                    .addComponent(pcgsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(26, Short.MAX_VALUE))
        );

        pceTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane7.setViewportView(pceTable);

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane7)
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel25, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jScrollPane7, javax.swing.GroupLayout.PREFERRED_SIZE, 405, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(62, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Commission Employee", jPanel8);

        jButton30.setText("Last");
        jButton30.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton30ActionPerformed(evt);
            }
        });

        jButton31.setText("First");
        jButton31.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton31ActionPerformed(evt);
            }
        });

        jLabel31.setText("First Name");

        jButton32.setText("Pay Salary");
        jButton32.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton32ActionPerformed(evt);
            }
        });

        jLabel32.setText("Social Seccurity No");

        jLabel33.setText("Basic Salary");

        jButton33.setText("Previous");
        jButton33.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton33ActionPerformed(evt);
            }
        });

        jButton34.setText("Next");
        jButton34.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton34ActionPerformed(evt);
            }
        });

        jLabel36.setText("Commission Rate");

        jLabel37.setText("Gross Sales");

        jLabel45.setText("Last Name");

        javax.swing.GroupLayout jPanel26Layout = new javax.swing.GroupLayout(jPanel26);
        jPanel26.setLayout(jPanel26Layout);
        jPanel26Layout.setHorizontalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel32)
                        .addGap(30, 30, 30)
                        .addComponent(pbcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbcbsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel36)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel37)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbcgsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jLabel45)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(pbclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(28, 28, 28)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel26Layout.createSequentialGroup()
                        .addComponent(jButton34)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton33)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jButton31)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jButton30))
                    .addComponent(jButton32))
                .addContainerGap())
        );
        jPanel26Layout.setVerticalGroup(
            jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel26Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel32)
                    .addComponent(pbcssnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton34)
                    .addComponent(jButton33)
                    .addComponent(jButton31)
                    .addComponent(jButton30))
                .addGap(19, 19, 19)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel31)
                    .addComponent(pbcfnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jButton32))
                .addGap(19, 19, 19)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel45)
                    .addComponent(pbclnTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel33)
                    .addComponent(pbcbsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel36)
                    .addComponent(pbccrTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(20, 20, 20)
                .addGroup(jPanel26Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel37)
                    .addComponent(pbcgsTxt, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pbcTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane8.setViewportView(pbcTable);

        javax.swing.GroupLayout jPanel9Layout = new javax.swing.GroupLayout(jPanel9);
        jPanel9.setLayout(jPanel9Layout);
        jPanel9Layout.setHorizontalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addComponent(jScrollPane8)
        );
        jPanel9Layout.setVerticalGroup(
            jPanel9Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel9Layout.createSequentialGroup()
                .addGap(21, 21, 21)
                .addComponent(jPanel26, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        jTabbedPane2.addTab("Base Plus Commission Employee", jPanel9);

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2)
        );

        jPanel3.add(jPanel5, "card3");

        jPanel27.setBackground(new java.awt.Color(255, 255, 255));
        jPanel27.setForeground(new java.awt.Color(204, 255, 204));

        jPanel29.setBackground(new java.awt.Color(102, 102, 102));
        jPanel29.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Weekly Pay Sheets", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 0, 14), new java.awt.Color(0, 0, 0))); // NOI18N
        jPanel29.setForeground(new java.awt.Color(204, 255, 0));
        jPanel29.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jButton15.setBackground(new java.awt.Color(255, 255, 204));
        jButton15.setText("All Employees");
        jButton15.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton15.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton15ActionPerformed(evt);
            }
        });

        SalariedEmployeeReportBtn.setBackground(new java.awt.Color(255, 255, 204));
        SalariedEmployeeReportBtn.setText("Weekly Salaried Employee");
        SalariedEmployeeReportBtn.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        SalariedEmployeeReportBtn.setDebugGraphicsOptions(javax.swing.DebugGraphics.NONE_OPTION);
        SalariedEmployeeReportBtn.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                SalariedEmployeeReportBtnMouseEntered(evt);
            }
        });
        SalariedEmployeeReportBtn.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                SalariedEmployeeReportBtnActionPerformed(evt);
            }
        });

        jButton17.setBackground(new java.awt.Color(255, 255, 204));
        jButton17.setText("Hourly Employee");
        jButton17.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton17.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton17ActionPerformed(evt);
            }
        });

        jButton18.setBackground(new java.awt.Color(255, 255, 204));
        jButton18.setText("Commission Employee");
        jButton18.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton18.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton18ActionPerformed(evt);
            }
        });

        jButton22.setBackground(new java.awt.Color(255, 255, 204));
        jButton22.setText("Base Plus Commission Employee");
        jButton22.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        jButton22.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton22ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel29Layout = new javax.swing.GroupLayout(jPanel29);
        jPanel29.setLayout(jPanel29Layout);
        jPanel29Layout.setHorizontalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(SalariedEmployeeReportBtn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton17, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton18, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jButton22, javax.swing.GroupLayout.DEFAULT_SIZE, 358, Short.MAX_VALUE)
            .addComponent(jButton15, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel29Layout.setVerticalGroup(
            jPanel29Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel29Layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addComponent(SalariedEmployeeReportBtn)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 22, Short.MAX_VALUE)
                .addComponent(jButton17)
                .addGap(18, 18, 18)
                .addComponent(jButton18)
                .addGap(18, 18, 18)
                .addComponent(jButton22)
                .addGap(18, 18, 18)
                .addComponent(jButton15)
                .addGap(24, 24, 24))
        );

        jLabel47.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imges/Sample_Payslip.png"))); // NOI18N
        jLabel47.setText("jLabel47");

        javax.swing.GroupLayout jPanel27Layout = new javax.swing.GroupLayout(jPanel27);
        jPanel27.setLayout(jPanel27Layout);
        jPanel27Layout.setHorizontalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel27Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(32, 32, 32))
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(175, 175, 175)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(200, Short.MAX_VALUE))
        );
        jPanel27Layout.setVerticalGroup(
            jPanel27Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel27Layout.createSequentialGroup()
                .addGap(139, 139, 139)
                .addComponent(jPanel29, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(39, 39, 39)
                .addComponent(jLabel47, javax.swing.GroupLayout.PREFERRED_SIZE, 188, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(118, Short.MAX_VALUE))
        );

        jPanel3.add(jPanel27, "card5");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        setSize(new java.awt.Dimension(954, 774));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        jPanel3.removeAll();
        jPanel3.repaint();
        jPanel3.revalidate();
            
        jPanel3.add(jPanel4);
        jPanel3.repaint();
        jPanel3.revalidate();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        
        psssnTxt.setEditable(false);
        psfnTxt.setEditable(false);
        pswsTxt.setEditable(false);
        
        phssnTxt.setEditable(false);
        phfnTxt.setEditable(false);
        phhwTxt.setEditable(false);

        pcssnTxt.setEditable(false);
        pcfnTxt.setEditable(false);
        pccrTxt.setEditable(false);

        pbcssnTxt.setEditable(false);
        pbcfnTxt.setEditable(false);
        pbccrTxt.setEditable(false);
        pbcbsTxt.setEditable(false);

        
        jPanel3.removeAll();
        jPanel3.repaint();
        jPanel3.revalidate();
            
        jPanel3.add(jPanel5);
        jPanel3.repaint();
        jPanel3.revalidate();
        
        paySalarySE();
        paySalaryHE();
        paySalaryCE();
        paySalaryBPCE();
    }//GEN-LAST:event_jButton2ActionPerformed

    private void slnTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_slnTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_slnTxtActionPerformed

    private void saddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saddBtnActionPerformed
        if(sssnTxt.getText().isEmpty() || sfnTxt.getText().isEmpty() || 
                slnTxt.getText().isEmpty() || swsTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty");
        }else
        {
            try {
                String sql = "SELECT * FROM `employee` WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, sssnTxt.getText());
                rs = pst.executeQuery();
                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null, "Social Securiy number already exits");
                }
                else
                {
                    se = new SalariedEmployee(sfnTxt.getText(),slnTxt.getText(),sssnTxt.getText(),Double.parseDouble(swsTxt.getText()));
                    
                    String sql1 = "INSERT INTO `employee`(`Social_Security_Number`, `First_Name`, `Last_Name`, `Salaried`, `Weekly_Salary`) VALUES (?,?,?,?,?);";
                    pst = conn.prepareStatement(sql1);
                    pst.setString(1,se.getSocialSecurityNumber() );
                    pst.setString(2,se.getFirstName());
                    pst.setString(3,se.getLastName() );
                    pst.setString(4, "1");
                    pst.setString(5,String.format("%.2f", se.getWeeklySalary()));
                    
                    pst.execute();
                    updateSalariedEmployeeTable();
                    JOptionPane.showMessageDialog(null, "Saved");
                    sssnTxt.setText("");
                    sfnTxt.setText("");
                    slnTxt.setText("");
                    swsTxt.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Weekly salary must be >= 0.0");
            }
        }
            
    }//GEN-LAST:event_saddBtnActionPerformed

    private void hAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hAddBtnActionPerformed
        if(hssnTxt.getText().isEmpty() || hfnTxt.getText().isEmpty() || 
                hlnTxt.getText().isEmpty() || hhwTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty");
        }else
        {
            try {
                String sql = "SELECT * FROM `employee` WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, hssnTxt.getText());
                rs = pst.executeQuery();
                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null, "Social Securiy number already exits");
                }
                else
                {
                    he = new HourlyEmployee(hfnTxt.getText(),hlnTxt.getText(), hssnTxt.getText(), Double.parseDouble(hhwTxt.getText()),0.0);
                    
                    String sql1 = "INSERT INTO `employee`(`Social_Security_Number`, `First_Name`, `Last_Name`, `Hourly`, `Hourly_Wage`) VALUES (?,?,?,?,?);";
                    pst = conn.prepareStatement(sql1);
                    pst.setString(1,he.getSocialSecurityNumber());
                    pst.setString(2,he.getFirstName() );
                    pst.setString(3, he.getLastName());
                    pst.setString(4, "1");
                    pst.setString(5, String.format("%.2f", he.getWage()));
                    pst.execute();
                    updateHourlyEmployeeTable();
                    JOptionPane.showMessageDialog(null, "Saved");
                    hssnTxt.setText("");
                    hfnTxt.setText("");
                    hlnTxt.setText("");
                    hhwTxt.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Hourly wage must be >= 0.0");
            }
        }
            

    }//GEN-LAST:event_hAddBtnActionPerformed

    private void hlnTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hlnTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_hlnTxtActionPerformed

    private void cAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cAddBtnActionPerformed
        if(cssnTxt.getText().isEmpty() || cfnTxt.getText().isEmpty() || 
                clnTxt.getText().isEmpty() || ccrTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty");
        }else
        {
            try {
                String sql = "SELECT * FROM `employee` WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, cssnTxt.getText());
                rs = pst.executeQuery();
                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null, "Social Securiy number already exits");
                }
                else
                {
                    
                    ce = new CommissionEmployee(cfnTxt.getText(),clnTxt.getText(),cssnTxt.getText(),0.0,Double.parseDouble(ccrTxt.getText()));
                    
                    String sql1 = "INSERT INTO `employee`(`Social_Security_Number`, `First_Name`, `Last_Name`, `Commission`, `Commission_Rate`) VALUES (?,?,?,?,?);";
                    pst = conn.prepareStatement(sql1);
                    pst.setString(1,ce.getSocialSecurityNumber());
                    pst.setString(2, ce.getFirstName());
                    pst.setString(3,ce.getLastName() );
                    pst.setString(4, "1");
                    pst.setString(5,String.format("%.2f", ce.getCommissionRate()) );
                    pst.execute();
                    updateCommissionEmployeeTable();
                    JOptionPane.showMessageDialog(null, "Saved");
                    cssnTxt.setText("");
                    cfnTxt.setText("");
                    clnTxt.setText("");
                    ccrTxt.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Commission rate must be > 0.0 and < 1.0");
            }
        }
            

    }//GEN-LAST:event_cAddBtnActionPerformed

    private void clnTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clnTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_clnTxtActionPerformed

    private void bcAddBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcAddBtnActionPerformed
        if(bcssnTxt.getText().isEmpty() || bcfnTxt.getText().isEmpty() || 
                bclnTxt.getText().isEmpty() || bcbsTxt.getText().isEmpty() || bccrTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty");
        }else
        {
            try {
                String sql = "SELECT * FROM `employee` WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, bcssnTxt.getText());
                rs = pst.executeQuery();
                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null, "Social Securiy number already exits");
                }
                else
                {
                    bpce = new BasePlusCommissionEmployee( bcfnTxt.getText(),bclnTxt.getText(), bcssnTxt.getText(),0.0,Double.parseDouble(bccrTxt.getText()),Double.parseDouble(bcbsTxt.getText()));
                    
                    String sql1 = "INSERT INTO `employee`(`Social_Security_Number`, `First_Name`, `Last_Name`, `Base_Plus_Commission`, `Base_Salary`, `Commission_Rate`) VALUES (?,?,?,?,?,?);";
                    pst = conn.prepareStatement(sql1);
                    pst.setString(1, bpce.getSocialSecurityNumber());
                    pst.setString(2,bpce.getFirstName());
                    pst.setString(3, bpce.getLastName());
                    pst.setString(4, "1");
                    pst.setString(5, String.format("%.2f", bpce.getBaseSalary()));
                    pst.setString(6, String.format("%.2f", bpce.getCommissionRate()));
                    pst.execute();
                    updateBasePlusCommissionEmployeeTable();
                    JOptionPane.showMessageDialog(null, "Saved");
                    bcssnTxt.setText("");
                    bcfnTxt.setText("");
                    bclnTxt.setText("");
                    bcbsTxt.setText("");
                    bccrTxt.setText("");
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Base salary must be >= 0.0");
            }
        }
            

    }//GEN-LAST:event_bcAddBtnActionPerformed

    private void bclnTxtActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bclnTxtActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_bclnTxtActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        try {
            if(rs1.first())
            {
                psssnTxt.setText(rs1.getString("Social_Security_Number"));
                psfnTxt.setText(rs1.getString("First_Name"));
                pslnTxt.setText(rs1.getString("Last_Name"));
                pswsTxt.setText(rs1.getString("Weekly_Salary"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton21ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton21ActionPerformed
        try {
            if(rs2.first())
            {
                phssnTxt.setText(rs2.getString("Social_Security_Number"));
                phfnTxt.setText(rs2.getString("First_Name"));
                phlnTxt.setText(rs2.getString("Last_Name"));
                phhwTxt.setText(rs2.getString("Hourly_Wage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton21ActionPerformed

    private void jButton26ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton26ActionPerformed
        try {
            if(rs3.first())
            {
                pcssnTxt.setText(rs3.getString("Social_Security_Number"));
                pcfnTxt.setText(rs3.getString("First_Name"));
                pclnTxt.setText(rs3.getString("Last_Name"));
                pccrTxt.setText(rs3.getString("Commission_Rate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton26ActionPerformed

    private void jButton31ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton31ActionPerformed
        try {
            if(rs4.first())
            {
                pbcssnTxt.setText(rs4.getString("Social_Security_Number"));
                pbcfnTxt.setText(rs4.getString("First_Name"));
                pbclnTxt.setText(rs4.getString("Last_Name"));
                pbcbsTxt.setText(rs4.getString("Base_Salary"));
                pbccrTxt.setText(rs4.getString("Commission_Rate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton31ActionPerformed

    private void sTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_sTableMouseClicked
        int row = sTable.getSelectedRow();
        
        sssn = (String) sTable.getModel().getValueAt(row, 0);
        

        
        sssnTxt.setText((String) sTable.getModel().getValueAt(row, 0));
        sfnTxt.setText((String) sTable.getModel().getValueAt(row, 1));
        slnTxt.setText((String) sTable.getModel().getValueAt(row, 2));
        swsTxt.setText(String.format("%.2f", sTable.getModel().getValueAt(row, 3)));
    }//GEN-LAST:event_sTableMouseClicked

    private void supdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supdateBtnActionPerformed
        if( sfnTxt.getText().isEmpty() || slnTxt.getText().isEmpty() || swsTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty, please select a row");
        }
        else
        {
            try {
                
                se1.setFirstName(sfnTxt.getText());
                se1.setLastName(slnTxt.getText());
                se1.setWeeklySalary(Double.parseDouble(swsTxt.getText()));
                
                    
                    

                String sql = "UPDATE `employee` SET `First_Name`=?,`Last_Name`=?,`Weekly_Salary`=? WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, se1.getFirstName());
                pst.setString(2, se1.getLastName());
                pst.setString(3, String.format("%.2f", se1.getWeeklySalary()));
                pst.setString(4, sssn);
                pst.execute();
                sssnTxt.setText("");
                sfnTxt.setText("");
                slnTxt.setText("");
                swsTxt.setText("");
                JOptionPane.showMessageDialog(null, "Saved");
                updateSalariedEmployeeTable();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Weekly salary must be >= 0.0");
            }
            
        }
    }//GEN-LAST:event_supdateBtnActionPerformed

    private void sdeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sdeleteBtnActionPerformed
        try {
            
            int row = sTable.getSelectedRow();
           
            String ssn = (String) sTable.getModel().getValueAt(row, 0);
            String sql = "DELETE FROM `employee` WHERE `Social_Security_Number` = ?";

            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            pst.setString(1,ssn );
           
            int r = pst.executeUpdate();
            updateSalariedEmployeeTable();
            JOptionPane.showMessageDialog(null,"Done");
            sssnTxt.setText("");
            sfnTxt.setText("");
            slnTxt.setText("");
            swsTxt.setText("");
            
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null,"Select a row first","",WARNING_MESSAGE);
        }        
    }//GEN-LAST:event_sdeleteBtnActionPerformed

    private void hUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hUpdateBtnActionPerformed
        if( hfnTxt.getText().isEmpty() || hlnTxt.getText().isEmpty() || hhwTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty, please select a row");
        }
        else
        {
            try {
                
                
                he1.setFirstName( hfnTxt.getText());
                he1.setLastName(hlnTxt.getText());
                he1.setWage(Double.parseDouble(hhwTxt.getText()));
                
                
                String sql = "UPDATE `employee` SET `First_Name`=?,`Last_Name`=?,`Hourly_Wage`=? WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1,he1.getFirstName());
                pst.setString(2,he1.getLastName() );
                pst.setString(3, String.format("%.2f", he1.getWage()));
                pst.setString(4, hssn);
                pst.execute();
                hssnTxt.setText("");
                hfnTxt.setText("");
                hlnTxt.setText("");
                hhwTxt.setText("");
                JOptionPane.showMessageDialog(null, "Saved");
                updateHourlyEmployeeTable();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Hourly wage must be >= 0.0");
            }
            
        }

    }//GEN-LAST:event_hUpdateBtnActionPerformed

    private void hDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hDeleteBtnActionPerformed
        try {
            
            int row = hTable.getSelectedRow();
           
            String ssn = (String) hTable.getModel().getValueAt(row, 0);
            String sql = "DELETE FROM `employee` WHERE `Social_Security_Number` = ?";

            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            pst.setString(1,ssn );
           
            int r = pst.executeUpdate();
            updateHourlyEmployeeTable();
            JOptionPane.showMessageDialog(null,"Done");
            hssnTxt.setText("");
            hfnTxt.setText("");
            hlnTxt.setText("");
            hhwTxt.setText("");
            
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null,"Select a row first","",WARNING_MESSAGE);
        }        

    }//GEN-LAST:event_hDeleteBtnActionPerformed

    private void hTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hTableMouseClicked
        int row = hTable.getSelectedRow();
        
        hssn = (String) hTable.getModel().getValueAt(row, 0);
        
        hssnTxt.setText((String) hTable.getModel().getValueAt(row, 0));
        hfnTxt.setText((String) hTable.getModel().getValueAt(row, 1));
        hlnTxt.setText((String) hTable.getModel().getValueAt(row, 2));
        hhwTxt.setText(String.format("%.2f", hTable.getModel().getValueAt(row, 3)));

    }//GEN-LAST:event_hTableMouseClicked

    private void cUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cUpdateBtnActionPerformed
        if( cfnTxt.getText().isEmpty() || clnTxt.getText().isEmpty() || ccrTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty, please select a row");
        }
        else
        {
            try {
                
                ce1.setFirstName(cfnTxt.getText());
                ce1.setLastName(clnTxt.getText());
                ce1.setCommissionRate(Double.parseDouble(ccrTxt.getText()));
                
                
                String sql = "UPDATE `employee` SET `First_Name`=?,`Last_Name`=?,`Commission_Rate`=? WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, ce1.getFirstName());
                pst.setString(2, ce1.getLastName());
                pst.setString(3, String.format("%.2f", ce1.getCommissionRate()));
                pst.setString(4, cssn);
                pst.execute();
                cssnTxt.setText("");
                cfnTxt.setText("");
                clnTxt.setText("");
                ccrTxt.setText("");
                JOptionPane.showMessageDialog(null, "Saved");
                updateCommissionEmployeeTable();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Commission rate must be > 0.0 and < 1.0");
            }
            
        }


    }//GEN-LAST:event_cUpdateBtnActionPerformed

    private void cDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cDeleteBtnActionPerformed
        try {
            
            int row = cTable.getSelectedRow();
           
            String ssn = (String) cTable.getModel().getValueAt(row, 0);
            String sql = "DELETE FROM `employee` WHERE `Social_Security_Number` = ?";

            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            pst.setString(1,ssn );
           
            int r = pst.executeUpdate();
            updateCommissionEmployeeTable();
            JOptionPane.showMessageDialog(null,"Done");
            cssnTxt.setText("");
            cfnTxt.setText("");
            clnTxt.setText("");
            ccrTxt.setText("");
            
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null,"Select a row first","",WARNING_MESSAGE);
        }        


    }//GEN-LAST:event_cDeleteBtnActionPerformed

    private void cTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cTableMouseClicked
        int row = cTable.getSelectedRow();
        
        cssn = (String) cTable.getModel().getValueAt(row, 0);
        
        cssnTxt.setText((String) cTable.getModel().getValueAt(row, 0));
        cfnTxt.setText((String) cTable.getModel().getValueAt(row, 1));
        clnTxt.setText((String) cTable.getModel().getValueAt(row, 2));
        ccrTxt.setText(String.format("%.2f", cTable.getModel().getValueAt(row, 3)));

    }//GEN-LAST:event_cTableMouseClicked

    private void bcUpdateBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcUpdateBtnActionPerformed
        if( bcfnTxt.getText().isEmpty() || bclnTxt.getText().isEmpty() || bcbsTxt.getText().isEmpty() || bccrTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null, "Text fields cannot be empty, please select a row");
        }
        else
        {
            try {
                
                bpce1.setFirstName(bcfnTxt.getText());
                bpce1.setLastName(bclnTxt.getText());
                bpce1.setBaseSalary( Double.parseDouble(bcbsTxt.getText()));
                bpce1.setCommissionRate(Double.parseDouble(bccrTxt.getText()));
                
                String sql = "UPDATE `employee` SET `First_Name`=?,`Last_Name`=?,`Commission_Rate`=?,`Base_Salary`=? WHERE `Social_Security_Number` = ?;";
                pst = conn.prepareStatement(sql);
                pst.setString(1, bpce1.getFirstName() );
                pst.setString(2, bpce1.getLastName());
                pst.setString(3, String.format("%.2f", bpce1.getCommissionRate()));
                pst.setString(4,String.format("%.2f", bpce1.getBaseSalary()));
                pst.setString(5, bcssn);
                pst.execute();
                bcssnTxt.setText("");
                bcfnTxt.setText("");
                bclnTxt.setText("");
                bccrTxt.setText("");
                bcbsTxt.setText("");
                JOptionPane.showMessageDialog(null, "Saved");
                updateBasePlusCommissionEmployeeTable();
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }  catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Base salary must be >= 0.0");
            }
            
        }



    }//GEN-LAST:event_bcUpdateBtnActionPerformed

    private void bcDeleteBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bcDeleteBtnActionPerformed
        try {
            
            int row = bcTable.getSelectedRow();
           
            String ssn = (String) bcTable.getModel().getValueAt(row, 0);
            String sql = "DELETE FROM `employee` WHERE `Social_Security_Number` = ?";

            pst = (com.mysql.jdbc.PreparedStatement) conn.prepareStatement(sql);
            pst.setString(1,ssn );
           
            int r = pst.executeUpdate();
            updateBasePlusCommissionEmployeeTable();
            JOptionPane.showMessageDialog(null,"Done");
            bcssnTxt.setText("");
            bcfnTxt.setText("");
            bclnTxt.setText("");
            bccrTxt.setText("");
            bcbsTxt.setText("");
            
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }catch (ArrayIndexOutOfBoundsException ex){
            JOptionPane.showMessageDialog(null,"Select a row first","",WARNING_MESSAGE);
        }        



    }//GEN-LAST:event_bcDeleteBtnActionPerformed

    private void bcTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_bcTableMouseClicked
        int row = bcTable.getSelectedRow();
        
        bcssn = (String) bcTable.getModel().getValueAt(row, 0);
        
        bcssnTxt.setText((String) bcTable.getModel().getValueAt(row, 0));
        bcfnTxt.setText((String) bcTable.getModel().getValueAt(row, 1));
        bclnTxt.setText((String) bcTable.getModel().getValueAt(row, 2));
        bcbsTxt.setText(String.format("%.2f", bcTable.getModel().getValueAt(row, 3)));
        bccrTxt.setText(String.format("%.2f", bcTable.getModel().getValueAt(row, 4)));

    }//GEN-LAST:event_bcTableMouseClicked

    private void jButton29ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton29ActionPerformed
        try {
            if(rs3.next())
            {
                pcssnTxt.setText(rs3.getString("Social_Security_Number"));
                pcfnTxt.setText(rs3.getString("First_Name"));
                pclnTxt.setText(rs3.getString("Last_Name"));
                pccrTxt.setText(rs3.getString("Commission_Rate"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the last record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }



    }//GEN-LAST:event_jButton29ActionPerformed

    private void jButton28ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton28ActionPerformed
        try {
            if(rs3.previous())
            {
                pcssnTxt.setText(rs3.getString("Social_Security_Number"));
                pcfnTxt.setText(rs3.getString("First_Name"));
                pclnTxt.setText(rs3.getString("Last_Name"));
                pccrTxt.setText(rs3.getString("Commission_Rate"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the first record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton28ActionPerformed

    private void jButton25ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton25ActionPerformed
        try {
            if(rs3.last())
            {
                pcssnTxt.setText(rs3.getString("Social_Security_Number"));
                pcfnTxt.setText(rs3.getString("First_Name"));
                pclnTxt.setText(rs3.getString("Last_Name"));
                pccrTxt.setText(rs3.getString("Commission_Rate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton25ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            if(rs1.next())
            {
                psssnTxt.setText(rs1.getString("Social_Security_Number"));
                psfnTxt.setText(rs1.getString("First_Name"));
                pslnTxt.setText(rs1.getString("Last_Name"));
                pswsTxt.setText(rs1.getString("Weekly_Salary"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the last record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        try {
            if(rs1.previous())
            {
                psssnTxt.setText(rs1.getString("Social_Security_Number"));
                psfnTxt.setText(rs1.getString("First_Name"));
                pslnTxt.setText(rs1.getString("Last_Name"));
                pswsTxt.setText(rs1.getString("Weekly_Salary"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the first record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        try {
            if(rs1.last())
            {
                psssnTxt.setText(rs1.getString("Social_Security_Number"));
                psfnTxt.setText(rs1.getString("First_Name"));
                pslnTxt.setText(rs1.getString("Last_Name"));
                pswsTxt.setText(rs1.getString("Weekly_Salary"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton19ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton19ActionPerformed
                    
        try {
            String sql1 = "SELECT * FROM `salary` WHERE `Social_Security_Number` = ? AND `Week` = ? AND YEAR(Date) = ?;";
            pst = conn.prepareStatement(sql1);
            pst.setString(1, psssnTxt.getText());
            pst.setString(2, String.format("%d",week ));
            pst.setString(3, String.format("%d",year ));
            
            ResultSet rs = pst.executeQuery();
            
            if(rs.next())
            {
                JOptionPane.showMessageDialog(null,"Salary already paid");
            }
            else
            {
                PdfWriter myWriter = null;
                try {
                    
                    String ssn = psssnTxt.getText();
                    String fn = psfnTxt.getText();
                    String ln = pslnTxt.getText();
                    Double ws =  Double.parseDouble(pswsTxt.getText());

                    SalariedEmployee pse = new SalariedEmployee(fn,ln,ssn,ws);
                    e[0] = pse;

                    Document mydoc = new Document();

                    myWriter = PdfWriter.getInstance(mydoc,new FileOutputStream("report.pdf"));
                    mydoc.open();
                    mydoc.add(new Paragraph("CMMD-Enterprices",FontFactory.getFont(FontFactory.TIMES_BOLD,20,Font.BOLD)));
                    mydoc.add(new Paragraph("Pay Slip",FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD)));
                    mydoc.add(new Paragraph(String.format("%s", Calendar.getInstance().getTime())));
                    mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
                    
                    mydoc.add(new Paragraph(String.format("%s", e[0].toString())));
                    mydoc.add(new Paragraph(String.format("earned: $%,.2f\n\n",e[0].earnings())));
                    mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
                    

                    

                    mydoc.close();
                    if (Desktop.isDesktopSupported()) {
                        try {
                            File myFile = new File("C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\report.pdf");
                            Desktop.getDesktop().open(myFile);
                        } catch (FileNotFoundException ex) {
                            JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                        }
                    }
                    String date = year+"-"+month+"-"+day;
                    String sql = "INSERT INTO `salary`(`Social_Security_Number`, `Date`, `Week`,`Earnings`) VALUES (?,?,?,?)";

                    pst = conn.prepareStatement(sql);
                    pst.setString(1, psssnTxt.getText());
                    pst.setString(2, date);
                    pst.setString(3, String.format("%d",week ));
                    pst.setString(4, String.format("%f",e[0].earnings() ));

                    pst.execute();

                    updatePayWETable();

                    myWriter.close();
                } catch (FileNotFoundException ex) {
                    JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                    //Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(null, "Weekly salary must be >= 0.0");
                }catch (Exception ex) {
                    Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
            
              
    }//GEN-LAST:event_jButton19ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        jPanel3.removeAll();
        jPanel3.repaint();
        jPanel3.revalidate();
            
        jPanel3.add(jPanel27);
        jPanel3.repaint();
        jPanel3.revalidate();
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
            this.dispose();
            LoginScreen ls = new LoginScreen();
            ls.setVisible(true);

    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        System.exit(0);
    }//GEN-LAST:event_jButton9ActionPerformed

    private void payHourlyEmployeeSalaryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_payHourlyEmployeeSalaryActionPerformed
        if(phwTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Enter the hours worked first");  
        }
        else
        {
            try {
                String sql1 = "SELECT * FROM `salary` WHERE `Social_Security_Number` = ? AND `Week` = ? AND YEAR(Date) = ?;";
                pst = conn.prepareStatement(sql1);
                pst.setString(1, phssnTxt.getText());
                pst.setString(2, String.format("%d",week ));
                pst.setString(3, String.format("%d",year ));

                ResultSet rs = pst.executeQuery();

                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null,"Salary already paid");
                }
                else
                {
                    PdfWriter myWriter = null;
                    try {
                        String ssn = phssnTxt.getText();
                        String fn = phfnTxt.getText();
                        String ln = phlnTxt.getText();
                        Double hwage =  Double.parseDouble(phhwTxt.getText());
                        Double hw =  Double.parseDouble(phwTxt.getText());

                        HourlyEmployee phe = new HourlyEmployee(fn,ln,ssn,hwage,hw);
                        e[1] = phe;

                        Document mydoc = new Document();

                        myWriter = PdfWriter.getInstance(mydoc,new FileOutputStream("report.pdf"));
                        mydoc.open();
                        mydoc.add(new Paragraph("CMMD-Enterprices",FontFactory.getFont(FontFactory.TIMES_BOLD,20,Font.BOLD)));
                        mydoc.add(new Paragraph("Pay Slip",FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD)));
                        mydoc.add(new Paragraph(String.format("%s", Calendar.getInstance().getTime())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
                    
                        mydoc.add(new Paragraph(String.format("%s", e[1].toString())));
                        mydoc.add(new Paragraph(String.format("earned: $%,.2f\n\n",e[1].earnings())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));

                        

                        mydoc.close();
                        if (Desktop.isDesktopSupported()) {
                            try {
                                File myFile = new File("C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\report.pdf");
                                Desktop.getDesktop().open(myFile);
                            } catch (FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                            }
                        }
                        String date = year+"-"+month+"-"+day;
                        String sql = "INSERT INTO `salary`(`Social_Security_Number`, `Date`, `Week`, `Hours_Worked`,`Earnings`) VALUES (?,?,?,?,?)";

                        pst = conn.prepareStatement(sql);
                        pst.setString(1, phssnTxt.getText());
                        pst.setString(2, date);
                        pst.setString(3, String.format("%d",week ));
                        pst.setString(4, phwTxt.getText());
                        pst.setString(5, String.format("%f",e[1].earnings() ));

                        pst.execute();

                        myWriter.close();
                        updatePayHETable();
                        phwTxt.setText("");

                    }  catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                        //Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Hours worked must be >= 0.0 and <= 168.0");
                    }catch (Exception ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }   

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
    }//GEN-LAST:event_payHourlyEmployeeSalaryActionPerformed

    private void jButton34ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton34ActionPerformed
        try {
            if(rs4.next())
            {
                pbcssnTxt.setText(rs4.getString("Social_Security_Number"));
                pbcfnTxt.setText(rs4.getString("First_Name"));
                pbclnTxt.setText(rs4.getString("Last_Name"));
                pbcbsTxt.setText(rs4.getString("Base_Salary"));
                pbccrTxt.setText(rs4.getString("Commission_Rate"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the last record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }



    }//GEN-LAST:event_jButton34ActionPerformed

    private void jButton24ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton24ActionPerformed
        try {
            if(rs2.next())
            {
                phssnTxt.setText(rs2.getString("Social_Security_Number"));
                phfnTxt.setText(rs2.getString("First_Name"));
                phlnTxt.setText(rs2.getString("Last_Name"));
                phhwTxt.setText(rs2.getString("Hourly_Wage"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the last record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton24ActionPerformed

    private void jButton23ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton23ActionPerformed
        try {
            if(rs2.previous())
            {
                phssnTxt.setText(rs2.getString("Social_Security_Number"));
                phfnTxt.setText(rs2.getString("First_Name"));
                phlnTxt.setText(rs2.getString("Last_Name"));
                phhwTxt.setText(rs2.getString("Hourly_Wage"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the first record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }


    }//GEN-LAST:event_jButton23ActionPerformed

    private void jButton20ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton20ActionPerformed
        try {
            if(rs2.last())
            {
                phssnTxt.setText(rs2.getString("Social_Security_Number"));
                phfnTxt.setText(rs2.getString("First_Name"));
                phlnTxt.setText(rs2.getString("Last_Name"));
                phhwTxt.setText(rs2.getString("Hourly_Wage"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton20ActionPerformed

    private void jButton30ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton30ActionPerformed
        try {
            if(rs4.last())
            {
                pbcssnTxt.setText(rs4.getString("Social_Security_Number"));
                pbcfnTxt.setText(rs4.getString("First_Name"));
                pbclnTxt.setText(rs4.getString("Last_Name"));
                pbcbsTxt.setText(rs4.getString("Base_Salary"));
                pbccrTxt.setText(rs4.getString("Commission_Rate"));
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton30ActionPerformed

    private void jButton33ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton33ActionPerformed
        try {
            if(rs4.previous())
            {
                pbcssnTxt.setText(rs4.getString("Social_Security_Number"));
                pbcfnTxt.setText(rs4.getString("First_Name"));
                pbclnTxt.setText(rs4.getString("Last_Name"));
                pbcbsTxt.setText(rs4.getString("Base_Salary"));
                pbccrTxt.setText(rs4.getString("Commission_Rate"));
            }
            else
            {
                JOptionPane.showMessageDialog(null,"This is the first record");
            }
        } catch (SQLException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton33ActionPerformed

    private void jButton27ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton27ActionPerformed
        if(pcgsTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Enter the Gross Sales first");  
        }
        else
        {
            try {
                String sql1 = "SELECT * FROM `salary` WHERE `Social_Security_Number` = ? AND `Week` = ? AND YEAR(Date) = ?;";
                pst = conn.prepareStatement(sql1);
                pst.setString(1, pcssnTxt.getText());
                pst.setString(2, String.format("%d",week ));
                pst.setString(3, String.format("%d",year ));

                ResultSet rs = pst.executeQuery();

                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null,"Salary already paid");
                }
                else
                {
                    PdfWriter myWriter = null;
                    try {
                        String ssn = pcssnTxt.getText();
                        String fn = pcfnTxt.getText();
                        String ln = pclnTxt.getText();
                        Double cr =  Double.parseDouble(pccrTxt.getText());
                        Double gs =  Double.parseDouble(pcgsTxt.getText());

                        CommissionEmployee com = new CommissionEmployee(fn,ln,ssn,gs,cr);
                        e[2] = com;

                        Document mydoc = new Document();

                        myWriter = PdfWriter.getInstance(mydoc,new FileOutputStream("report.pdf"));
                        mydoc.open();
                        mydoc.add(new Paragraph("CMMD-Enterprices",FontFactory.getFont(FontFactory.TIMES_BOLD,20,Font.BOLD)));
                        mydoc.add(new Paragraph("Pay Slip",FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD)));
                        mydoc.add(new Paragraph(String.format("%s", Calendar.getInstance().getTime())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
                    
                        mydoc.add(new Paragraph(String.format("%s", e[2].toString())));
                        mydoc.add(new Paragraph(String.format("earned: $%,.2f\n\n",e[2].earnings())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));

                        

                        mydoc.close();
                        if (Desktop.isDesktopSupported()) {
                            try {
                                File myFile = new File("C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\report.pdf");
                                Desktop.getDesktop().open(myFile);
                            } catch (FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                            }
                        }
                        String date = year+"-"+month+"-"+day;
                        String sql = "INSERT INTO `salary`(`Social_Security_Number`, `Date`, `Week`,`Gross_Sales`,`Earnings`) VALUES (?,?,?,?,?)";

                        pst = conn.prepareStatement(sql);
                        pst.setString(1, pcssnTxt.getText());
                        pst.setString(2, date);
                        pst.setString(3, String.format("%d",week ));
                        pst.setString(4, pcgsTxt.getText());
                        pst.setString(5, String.format("%f",e[2].earnings() ));

                        pst.execute();

                        myWriter.close();
                        updatePayCETable();
                        pcgsTxt.setText("");

                    }  catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                        //Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Gross sales must be >= 0.0");
                    }catch (Exception ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }   

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }//GEN-LAST:event_jButton27ActionPerformed

    private void jButton32ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton32ActionPerformed
        if(pbcgsTxt.getText().isEmpty())
        {
            JOptionPane.showMessageDialog(null,"Enter the Gross Sales first");  
        }
        else
        {
            try {
                String sql1 = "SELECT * FROM `salary` WHERE `Social_Security_Number` = ? AND `Week` = ? AND YEAR(Date) = ?;";
                pst = conn.prepareStatement(sql1);
                pst.setString(1, pbcssnTxt.getText());
                pst.setString(2, String.format("%d",week ));
                pst.setString(3, String.format("%d",year ));

                ResultSet rs = pst.executeQuery();

                if(rs.next())
                {
                    JOptionPane.showMessageDialog(null,"Salary already paid");
                }
                else
                {
                    PdfWriter myWriter = null;
                    try {
                        String ssn = pbcssnTxt.getText();
                        String fn = pbcfnTxt.getText();
                        String ln = pbclnTxt.getText();
                        Double bs = Double.parseDouble(pbcbsTxt.getText());
                        Double cr =  Double.parseDouble(pbccrTxt.getText());
                        Double gs =  Double.parseDouble(pbcgsTxt.getText());

                        BasePlusCommissionEmployee bpcom = new BasePlusCommissionEmployee(fn,ln,ssn,gs,cr,bs);
                        
                        e[3] = bpcom;

                        Document mydoc = new Document();

                        myWriter = PdfWriter.getInstance(mydoc,new FileOutputStream("report.pdf"));
                        mydoc.open();
                        mydoc.add(new Paragraph("CMMD-Enterprices",FontFactory.getFont(FontFactory.TIMES_BOLD,20,Font.BOLD)));
                        mydoc.add(new Paragraph("Pay Slip",FontFactory.getFont(FontFactory.TIMES_BOLD,15,Font.BOLD)));
                        mydoc.add(new Paragraph(String.format("%s", Calendar.getInstance().getTime())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));
                    
                        mydoc.add(new Paragraph(String.format("%s", e[3].toString())));
                        bpcom.setBaseSalary(1.1*bpcom.getBaseSalary());
                        mydoc.add(new Paragraph(String.format("new base salary with 10%% increase is: $%,.2f\n", bpcom.getBaseSalary())));
                        mydoc.add(new Paragraph(String.format("earned: $%,.2f\n\n",e[3].earnings())));
                        mydoc.add(new Paragraph("-----------------------------------------------------------------------------------------------------------"));

                        

                        mydoc.close();
                        if (Desktop.isDesktopSupported()) {
                            try {
                                File myFile = new File("C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\report.pdf");
                                Desktop.getDesktop().open(myFile);
                            } catch (FileNotFoundException ex) {
                                JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                            }
                        }
                        String date = year+"-"+month+"-"+day;
                        String sql = "INSERT INTO `salary`(`Social_Security_Number`, `Date`, `Week`,`Gross_Sales`,`Earnings`) VALUES (?,?,?,?,?)";

                        pst = conn.prepareStatement(sql);
                        pst.setString(1, pbcssnTxt.getText());
                        pst.setString(2, date);
                        pst.setString(3, String.format("%d",week ));
                        pst.setString(4, pbcgsTxt.getText());
                        pst.setString(5, String.format("%f",e[3].earnings() ));

                        pst.execute();

                        myWriter.close();
                        updatePayBPCETable();
                        pbcgsTxt.setText("");

                    }  catch (FileNotFoundException ex) {
                        JOptionPane.showMessageDialog(null,"Close the previous pay slip to generate a new");
                        //Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IllegalArgumentException ex) {
                        JOptionPane.showMessageDialog(null, "Gross sales must be >= 0.0");
                    }catch (Exception ex) {
                        Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
                    }   

                }
            } catch (SQLException ex) {
                Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }

    }//GEN-LAST:event_jButton32ActionPerformed

    private void SalariedEmployeeReportBtnActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_SalariedEmployeeReportBtnActionPerformed
        try {
         
            String reportSource="C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\src\\com\\company\\reports\\SalariedEmployeeWeekly.jrxml";
            
            
            Map<String, Object> params= new HashMap<>(); 
            JasperReport jasperReport= JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params,conn);
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_SalariedEmployeeReportBtnActionPerformed

    private void jButton17ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton17ActionPerformed
        try {
         
            String reportSource="C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\src\\com\\company\\reports\\HourlyEmployeeWeekly.jrxml";
            
            
            Map<String, Object> params= new HashMap<>(); 
            JasperReport jasperReport= JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params,conn);
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton17ActionPerformed

    private void jButton18ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton18ActionPerformed
        try {
         
            String reportSource="C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\src\\com\\company\\reports\\CommissionEmployeeWeekly.jrxml";
            
            
            Map<String, Object> params= new HashMap<>(); 
            JasperReport jasperReport= JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params,conn);
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton18ActionPerformed

    private void jButton22ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton22ActionPerformed
        try {
         
            String reportSource="C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\src\\com\\company\\reports\\BasePlusCommissionEmployeeWeekly.jrxml";
            
            
            Map<String, Object> params= new HashMap<>(); 
            JasperReport jasperReport= JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params,conn);
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton22ActionPerformed

    private void jButton15ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton15ActionPerformed
        try {
         
            String reportSource="C:\\Users\\acer\\Documents\\NetBeansProjects\\JavaProject\\src\\com\\company\\reports\\AllEmployeesWeekly.jrxml";
            
            
            Map<String, Object> params= new HashMap<>(); 
            JasperReport jasperReport= JasperCompileManager.compileReport(reportSource);
            JasperPrint jasperPrint= JasperFillManager.fillReport(jasperReport,params,conn);
            JasperViewer.viewReport(jasperPrint, false);
            
        } catch (JRException ex) {
            Logger.getLogger(MainMenu.class.getName()).log(Level.SEVERE, null, ex);
        }

    }//GEN-LAST:event_jButton15ActionPerformed

    private void SalariedEmployeeReportBtnMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_SalariedEmployeeReportBtnMouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_SalariedEmployeeReportBtnMouseEntered

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MainMenu.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MainMenu().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton SalariedEmployeeReportBtn;
    private javax.swing.JButton bcAddBtn;
    private javax.swing.JButton bcDeleteBtn;
    private javax.swing.JTable bcTable;
    private javax.swing.JButton bcUpdateBtn;
    private javax.swing.JTextField bcbsTxt;
    private javax.swing.JTextField bccrTxt;
    private javax.swing.JTextField bcfnTxt;
    private javax.swing.JTextField bclnTxt;
    private javax.swing.JTextField bcssnTxt;
    private javax.swing.JButton cAddBtn;
    private javax.swing.JButton cDeleteBtn;
    private javax.swing.JTable cTable;
    private javax.swing.JButton cUpdateBtn;
    private javax.swing.JTextField ccrTxt;
    private javax.swing.JTextField cfnTxt;
    private javax.swing.JTextField clnTxt;
    private javax.swing.JTextField cssnTxt;
    private javax.swing.JButton hAddBtn;
    private javax.swing.JButton hDeleteBtn;
    private javax.swing.JTable hTable;
    private javax.swing.JButton hUpdateBtn;
    private javax.swing.JTextField hfnTxt;
    private javax.swing.JTextField hhwTxt;
    private javax.swing.JTextField hlnTxt;
    private javax.swing.JTextField hssnTxt;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton15;
    private javax.swing.JButton jButton17;
    private javax.swing.JButton jButton18;
    private javax.swing.JButton jButton19;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton20;
    private javax.swing.JButton jButton21;
    private javax.swing.JButton jButton22;
    private javax.swing.JButton jButton23;
    private javax.swing.JButton jButton24;
    private javax.swing.JButton jButton25;
    private javax.swing.JButton jButton26;
    private javax.swing.JButton jButton27;
    private javax.swing.JButton jButton28;
    private javax.swing.JButton jButton29;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton30;
    private javax.swing.JButton jButton31;
    private javax.swing.JButton jButton32;
    private javax.swing.JButton jButton33;
    private javax.swing.JButton jButton34;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel20;
    private javax.swing.JLabel jLabel21;
    private javax.swing.JLabel jLabel22;
    private javax.swing.JLabel jLabel23;
    private javax.swing.JLabel jLabel24;
    private javax.swing.JLabel jLabel25;
    private javax.swing.JLabel jLabel26;
    private javax.swing.JLabel jLabel27;
    private javax.swing.JLabel jLabel28;
    private javax.swing.JLabel jLabel29;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel30;
    private javax.swing.JLabel jLabel31;
    private javax.swing.JLabel jLabel32;
    private javax.swing.JLabel jLabel33;
    private javax.swing.JLabel jLabel34;
    private javax.swing.JLabel jLabel35;
    private javax.swing.JLabel jLabel36;
    private javax.swing.JLabel jLabel37;
    private javax.swing.JLabel jLabel38;
    private javax.swing.JLabel jLabel39;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel40;
    private javax.swing.JLabel jLabel41;
    private javax.swing.JLabel jLabel42;
    private javax.swing.JLabel jLabel43;
    private javax.swing.JLabel jLabel44;
    private javax.swing.JLabel jLabel45;
    private javax.swing.JLabel jLabel46;
    private javax.swing.JLabel jLabel47;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel10;
    private javax.swing.JPanel jPanel11;
    private javax.swing.JPanel jPanel12;
    private javax.swing.JPanel jPanel13;
    private javax.swing.JPanel jPanel14;
    private javax.swing.JPanel jPanel15;
    private javax.swing.JPanel jPanel16;
    private javax.swing.JPanel jPanel17;
    private javax.swing.JPanel jPanel18;
    private javax.swing.JPanel jPanel19;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel20;
    private javax.swing.JPanel jPanel21;
    private javax.swing.JPanel jPanel22;
    private javax.swing.JPanel jPanel23;
    private javax.swing.JPanel jPanel24;
    private javax.swing.JPanel jPanel25;
    private javax.swing.JPanel jPanel26;
    private javax.swing.JPanel jPanel27;
    private javax.swing.JPanel jPanel29;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JPanel jPanel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JScrollPane jScrollPane6;
    private javax.swing.JScrollPane jScrollPane7;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JButton payHourlyEmployeeSalary;
    private javax.swing.JTable pbcTable;
    private javax.swing.JTextField pbcbsTxt;
    private javax.swing.JTextField pbccrTxt;
    private javax.swing.JTextField pbcfnTxt;
    private javax.swing.JTextField pbcgsTxt;
    private javax.swing.JTextField pbclnTxt;
    private javax.swing.JTextField pbcssnTxt;
    private javax.swing.JTextField pccrTxt;
    private javax.swing.JTable pceTable;
    private javax.swing.JTextField pcfnTxt;
    private javax.swing.JTextField pcgsTxt;
    private javax.swing.JTextField pclnTxt;
    private javax.swing.JTextField pcssnTxt;
    private javax.swing.JTable pheTable;
    private javax.swing.JTextField phfnTxt;
    private javax.swing.JTextField phhwTxt;
    private javax.swing.JTextField phlnTxt;
    private javax.swing.JTextField phssnTxt;
    private javax.swing.JTextField phwTxt;
    private javax.swing.JTextField psfnTxt;
    private javax.swing.JTextField pslnTxt;
    private javax.swing.JTextField psssnTxt;
    private javax.swing.JTextField pswsTxt;
    private javax.swing.JTable pweTable;
    private javax.swing.JTable sTable;
    private javax.swing.JButton saddBtn;
    private javax.swing.JButton sdeleteBtn;
    private javax.swing.JTextField sfnTxt;
    private javax.swing.JTextField slnTxt;
    private javax.swing.JTextField sssnTxt;
    private javax.swing.JButton supdateBtn;
    private javax.swing.JTextField swsTxt;
    // End of variables declaration//GEN-END:variables
}
