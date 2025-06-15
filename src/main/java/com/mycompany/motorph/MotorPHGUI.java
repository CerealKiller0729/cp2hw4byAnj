/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.mycompany.motorph;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;
import java.text.DecimalFormat;
import javax.swing.table.DefaultTableModel;

/**
 *
 * @author angeliquerivera
 */

public class MotorPHGUI {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#.##");
    private static JFrame mainFrame;
    private static final String EMPLOYEE_DATA_PATH = "src/main/resources/EmployeeData.csv";
    private static final String ATTENDANCE_DATA_PATH = "src/main/resources/AttendanceRecord.csv";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                // Load data
                EmployeeModelFromFile.loadEmployeesFromFile(EMPLOYEE_DATA_PATH);
                AttendanceRecord.loadAttendanceFromCSV(ATTENDANCE_DATA_PATH);
                
                createAndShowGUI();
            } catch (Exception e) {
                showError("Application failed to initialize: " + e.getMessage());
                System.exit(1);
            }
        });
    }

    private static void createAndShowGUI() {
        mainFrame = new JFrame("MotorPH Employee System");
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setSize(1000, 700);
        mainFrame.setLocationRelativeTo(null);
        mainFrame.add(createEmployeeDetailsPanel());
        mainFrame.setVisible(true);
    }

    private static JPanel createEmployeeDetailsPanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Header with exit button
        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.add(new JLabel("Employee Details", SwingConstants.CENTER), BorderLayout.CENTER);
        
        JButton exitButton = new JButton("Exit");
        exitButton.addActionListener(e -> System.exit(0));
        headerPanel.add(exitButton, BorderLayout.EAST);
        panel.add(headerPanel, BorderLayout.NORTH);

        // Tabbed interface
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.addTab("Individual Search", createIndividualEmployeePanel());
        tabbedPane.addTab("All Employees", createEmployeePanel());
        panel.add(tabbedPane, BorderLayout.CENTER);

        return panel;
    }

    private static JPanel createIndividualEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Search components
        JPanel searchPanel = new JPanel();
        JTextField searchField = new JTextField(15);
        JButton searchButton = new JButton("Search");
        
        searchPanel.add(new JLabel("Employee ID:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);

        // Results area
        JTextArea resultArea = new JTextArea(10, 50);
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        searchButton.addActionListener(e -> {
            try {
                String empId = searchField.getText().trim();
                if (empId.isEmpty()) {
                    throw new IllegalArgumentException("Please enter an Employee ID");
                }
                
                Employee employee = EmployeeModelFromFile.getEmployeeById(empId);
                if (employee != null) {
                    showEmployeeDetailsWithSalary(employee);
                } else {
                    resultArea.setText("Employee not found");
                }
            } catch (Exception ex) {
                showError(ex.getMessage());
            }
        });

        panel.add(searchPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static JPanel createEmployeePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Table setup
        String[] columns = {"Employee #", "Last Name", "First Name", "SSS #", "PhilHealth #", "TIN #", "Pag-IBIG #"};
        DefaultTableModel model = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };

        JTable table = new JTable(model);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);

        // Buttons panel
        JPanel buttonPanel = new JPanel();
        JButton refreshButton = new JButton("Refresh");
        JButton viewButton = new JButton("View Details");
        JButton newEmployeeButton = new JButton("New Employee");

        refreshButton.addActionListener(e -> refreshEmployeeTable(model));
        viewButton.addActionListener(e -> {
            int row = table.getSelectedRow();
            if (row >= 0) {
                String empId = model.getValueAt(row, 0).toString();
                Employee employee = EmployeeModelFromFile.getEmployeeById(empId);
                if (employee != null) {
                    showEmployeeDetailsWithSalary(employee);
                }
            } else {
                showError("Please select an employee first");
            }
        });

        newEmployeeButton.addActionListener(e -> showNewEmployeeForm(model));

        buttonPanel.add(refreshButton);
        buttonPanel.add(viewButton);
        buttonPanel.add(newEmployeeButton);

        refreshEmployeeTable(model);

        panel.add(buttonPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private static void showNewEmployeeForm(DefaultTableModel tableModel) {
        JFrame formFrame = new JFrame("Add New Employee");
        formFrame.setSize(600, 600);
        formFrame.setLocationRelativeTo(mainFrame);

        JPanel formPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Form fields
        JTextField empNumberField = new JTextField();
        JTextField lastNameField = new JTextField();
        JTextField firstNameField = new JTextField();
        JTextField birthDateField = new JTextField();
        JTextField addressField = new JTextField();
        JTextField phoneField = new JTextField();
        JTextField sssField = new JTextField();
        JTextField philhealthField = new JTextField();
        JTextField tinField = new JTextField();
        JTextField pagibigField = new JTextField();
        JTextField statusField = new JTextField("Regular");
        JTextField positionField = new JTextField();
        JTextField supervisorField = new JTextField();
        JTextField basicSalaryField = new JTextField();
        JTextField riceSubsidyField = new JTextField("0");
        JTextField phoneAllowanceField = new JTextField("0");
        JTextField clothingAllowanceField = new JTextField("0");
        JTextField grossSemiMonthlyField = new JTextField();
        JTextField hourlyRateField = new JTextField();
        JTextField shiftStartField = new JTextField("08:00");
        JCheckBox nightShiftCheckbox = new JCheckBox();

        // Add fields to form
        formPanel.add(new JLabel("Employee Number*:"));
        formPanel.add(empNumberField);
        formPanel.add(new JLabel("Last Name*:"));
        formPanel.add(lastNameField);
        formPanel.add(new JLabel("First Name*:"));
        formPanel.add(firstNameField);
        formPanel.add(new JLabel("Birth Date (YYYY-MM-DD):"));
        formPanel.add(birthDateField);
        formPanel.add(new JLabel("Address:"));
        formPanel.add(addressField);
        formPanel.add(new JLabel("Phone Number:"));
        formPanel.add(phoneField);
        formPanel.add(new JLabel("SSS Number:"));
        formPanel.add(sssField);
        formPanel.add(new JLabel("PhilHealth Number:"));
        formPanel.add(philhealthField);
        formPanel.add(new JLabel("TIN Number:"));
        formPanel.add(tinField);
        formPanel.add(new JLabel("Pag-IBIG Number:"));
        formPanel.add(pagibigField);
        formPanel.add(new JLabel("Status:"));
        formPanel.add(statusField);
        formPanel.add(new JLabel("Position:"));
        formPanel.add(positionField);
        formPanel.add(new JLabel("Supervisor:"));
        formPanel.add(supervisorField);
        formPanel.add(new JLabel("Basic Salary:"));
        formPanel.add(basicSalaryField);
        formPanel.add(new JLabel("Rice Subsidy:"));
        formPanel.add(riceSubsidyField);
        formPanel.add(new JLabel("Phone Allowance:"));
        formPanel.add(phoneAllowanceField);
        formPanel.add(new JLabel("Clothing Allowance:"));
        formPanel.add(clothingAllowanceField);
        formPanel.add(new JLabel("Gross Semi-Monthly:"));
        formPanel.add(grossSemiMonthlyField);
        formPanel.add(new JLabel("Hourly Rate*:"));
        formPanel.add(hourlyRateField);
        formPanel.add(new JLabel("Shift Start Time:"));
        formPanel.add(shiftStartField);
        formPanel.add(new JLabel("Night Shift:"));
        formPanel.add(nightShiftCheckbox);

        // Button panel
        JPanel buttonPanel = new JPanel();
        JButton saveButton = new JButton("Save");
        JButton cancelButton = new JButton("Cancel");

        saveButton.addActionListener(e -> {
            try {
                // Validate required fields
                if (empNumberField.getText().trim().isEmpty() ||
                    lastNameField.getText().trim().isEmpty() ||
                    firstNameField.getText().trim().isEmpty() ||
                    hourlyRateField.getText().trim().isEmpty()) {
                    throw new IllegalArgumentException("Fields marked with * are required");
                }

                // Create new employee
                Employee newEmployee = new Employee(
                    empNumberField.getText().trim(),
                    lastNameField.getText().trim(),
                    firstNameField.getText().trim(),
                    birthDateField.getText().trim(),
                    addressField.getText().trim(),
                    phoneField.getText().trim(),
                    sssField.getText().trim(),
                    philhealthField.getText().trim(),
                    tinField.getText().trim(),
                    pagibigField.getText().trim(),
                    statusField.getText().trim(),
                    positionField.getText().trim(),
                    supervisorField.getText().trim(),
                    basicSalaryField.getText().trim(),
                    riceSubsidyField.getText().trim(),
                    phoneAllowanceField.getText().trim(),
                    clothingAllowanceField.getText().trim(),
                    grossSemiMonthlyField.getText().trim(),
                    Double.parseDouble(hourlyRateField.getText().trim()),
                    shiftStartField.getText().trim(),
                    nightShiftCheckbox.isSelected()
                );

                // Add to model and save to file
                if (EmployeeModelFromFile.addEmployee(newEmployee)) {
                    // Refresh table
                    refreshEmployeeTable(tableModel);
                    
                    // Close form
                    formFrame.dispose();
                    
                    JOptionPane.showMessageDialog(formFrame, 
                        "Employee added successfully!", 
                        "Success", 
                        JOptionPane.INFORMATION_MESSAGE);
                } else {
                    showError("Failed to add employee. Employee ID may already exist.");
                }
            } catch (NumberFormatException ex) {
                showError("Please enter valid numbers for numeric fields");
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Error saving employee: " + ex.getMessage());
            }
        });

        cancelButton.addActionListener(e -> formFrame.dispose());

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);

        // Main panel layout
        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.add(new JScrollPane(formPanel), BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);

        formFrame.add(mainPanel);
        formFrame.setVisible(true);
    }

    private static void showEmployeeDetailsWithSalary(Employee employee) {
        JFrame detailsFrame = new JFrame("Employee Details - " + employee.getEmployeeNumber());
        detailsFrame.setSize(800, 650);
        detailsFrame.setLocationRelativeTo(mainFrame);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        // Employee details area
        JTextArea detailsArea = new JTextArea(employee.toString());
        detailsArea.setEditable(false);
        detailsArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
        JScrollPane detailsScroll = new JScrollPane(detailsArea);

        // Salary computation panel
        JPanel salaryPanel = new JPanel(new GridLayout(0, 2, 5, 5));
        salaryPanel.setBorder(BorderFactory.createTitledBorder("Salary Computation"));
        
        // Year selection
        salaryPanel.add(new JLabel("Select Year:"));
        JSpinner yearSpinner = new JSpinner(
            new SpinnerNumberModel(LocalDate.now().getYear(), 2000, LocalDate.now().getYear() + 1, 1));
        JSpinner.NumberEditor yearEditor = new JSpinner.NumberEditor(yearSpinner, "#");
        yearSpinner.setEditor(yearEditor);
        salaryPanel.add(yearSpinner);

        // Month selection
        salaryPanel.add(new JLabel("Select Month (1-12):"));
        JSpinner monthSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 12, 1));
        salaryPanel.add(monthSpinner);

        // Week selection (1-5)
        salaryPanel.add(new JLabel("Select Week (1-5):"));
        JSpinner weekSpinner = new JSpinner(new SpinnerNumberModel(1, 1, 5, 1));
        salaryPanel.add(weekSpinner);

        // Compute button
        JButton computeButton = new JButton("Compute Salary");
        salaryPanel.add(new JLabel()); // spacer
        salaryPanel.add(computeButton);

        // Results area
        JTextArea resultsArea = new JTextArea();
        resultsArea.setEditable(false);
        resultsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane resultsScroll = new JScrollPane(resultsArea);
        resultsScroll.setPreferredSize(new Dimension(700, 200));

        computeButton.addActionListener(e -> {
            try {
                // Validate inputs
                int year = (int) yearSpinner.getValue();
                int month = (int) monthSpinner.getValue();
                int week = (int) weekSpinner.getValue();
                
                // Additional validation
                if (week < 1 || week > 5) {
                    throw new IllegalArgumentException("Week must be between 1 and 5");
                }
                
                if (month < 1 || month > 12) {
                    throw new IllegalArgumentException("Month must be between 1 and 12");
                }
                
                // Validate date isn't in the future
                LocalDate selectedDate = LocalDate.of(year, month, 1);
                if (selectedDate.isAfter(LocalDate.now())) {
                    throw new IllegalArgumentException("Selected period cannot be in the future");
                }

                // Refresh attendance data
                AttendanceRecord.loadAttendanceFromCSV(ATTENDANCE_DATA_PATH);
                
                // Compute gross wage
                Grosswage grosswage = new Grosswage(
                    employee.getEmployeeNumber(),
                    employee.getFirstName(),
                    employee.getLastName(),
                    year,
                    month,
                    week,
                    employee.getShiftStartTime(),
                    employee.isNightShift()
                );
                double gross = grosswage.calculate();
                
                // Compute net wage
                String employeeName = employee.getLastName() + ", " + employee.getFirstName();
                Netwage netwage = new Netwage(
                    employee.getEmployeeNumber(),
                    employeeName,
                    gross,
                    grosswage.getHoursWorked(),
                    week,
                    grosswage,
                    month,
                    year
                );
                
                // Display results
                resultsArea.setText(formatSalaryResults(year, month, week, grosswage, netwage));
                
                // Make sure results area is visible
                resultsScroll.setVisible(true);
                
            } catch (NumberFormatException ex) {
                showError("Invalid number format: " + ex.getMessage());
            } catch (IllegalArgumentException ex) {
                showError(ex.getMessage());
            } catch (Exception ex) {
                showError("Error computing salary: " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        // Close button
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> detailsFrame.dispose());

        // Layout components
        JPanel resultsContainer = new JPanel(new BorderLayout());
        resultsContainer.setBorder(BorderFactory.createTitledBorder("Salary Results"));
        resultsContainer.add(resultsScroll, BorderLayout.CENTER);
        
        // Main content panel
        JPanel contentPanel = new JPanel(new BorderLayout(10, 10));
        contentPanel.add(detailsScroll, BorderLayout.CENTER);
        contentPanel.add(salaryPanel, BorderLayout.NORTH);
        contentPanel.add(resultsContainer, BorderLayout.SOUTH);
        
        // Button panel
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        buttonPanel.add(closeButton);
        
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        mainPanel.add(buttonPanel, BorderLayout.SOUTH);
        
        detailsFrame.add(mainPanel);
        detailsFrame.setVisible(true);
    }

    private static String formatSalaryResults(int year, int month, int week, Grosswage grosswage, Netwage netwage) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("=== SALARY COMPUTATION FOR WEEK %d OF %02d/%d ===\n", week, month, year));
        sb.append("\n=== WORK HOURS ===\n");
        sb.append(String.format("%-25s: %s hrs\n", "Regular Hours", decimalFormat.format(grosswage.getRegularHours())));
        sb.append(String.format("%-25s: %s hrs\n", "Overtime Hours", decimalFormat.format(grosswage.getOvertimeHours())));
        sb.append(String.format("%-25s: %s hrs\n", "Total Hours Worked", decimalFormat.format(grosswage.getHoursWorked())));
        
        sb.append("\n=== EARNINGS ===\n");
        sb.append(String.format("%-25s: PHP %s\n", "Regular Pay", decimalFormat.format(grosswage.getRegularPay())));
        sb.append(String.format("%-25s: PHP %s\n", "Overtime Pay", decimalFormat.format(grosswage.getOvertimePay())));
        sb.append(String.format("%-25s: PHP %s\n", "Gross Wage", decimalFormat.format(grosswage.calculate())));
        
        sb.append("\n=== DEDUCTIONS ===\n");
        sb.append(String.format("%-25s: PHP %s\n", "SSS Contribution", decimalFormat.format(netwage.getSSSDeduction())));
        sb.append(String.format("%-25s: PHP %s\n", "PhilHealth Contribution", decimalFormat.format(netwage.getPhilhealthDeduction())));
        sb.append(String.format("%-25s: PHP %s\n", "Pag-IBIG Contribution", decimalFormat.format(netwage.getPagIbigDeduction())));
        sb.append(String.format("%-25s: PHP %s\n", "Late/Tardy Deductions", decimalFormat.format(netwage.getLateDeduction())));
        sb.append(String.format("%-25s: PHP %s\n", "Withholding Tax", decimalFormat.format(netwage.getWithholdingTax())));
        sb.append(String.format("%-25s: PHP %s\n", "Total Deductions", decimalFormat.format(
            netwage.getTotalDeductions() + netwage.getWithholdingTax())));
        
        sb.append("\n=== NET SALARY ===\n");
        sb.append(String.format("%-25s: PHP %s\n", "Take Home Pay", 
            decimalFormat.format(grosswage.calculate() - netwage.getTotalDeductions() - netwage.getWithholdingTax())));
        
        return sb.toString();
    }

    private static void refreshEmployeeTable(DefaultTableModel model) {
        model.setRowCount(0);
        try {
            for (Employee emp : EmployeeModelFromFile.getEmployeeModelList()) {
                model.addRow(new Object[]{
                    emp.getEmployeeNumber(),
                    emp.getLastName(),
                    emp.getFirstName(),
                    emp.getSssNumber(),
                    emp.getPhilhealthNumber(),
                    emp.getTinNumber(),
                    emp.getPagIbigNumber()
                });
            }
        } catch (Exception e) {
            showError("Failed to refresh employee data: " + e.getMessage());
        }
    }

    private static void showError(String message) {
        JOptionPane.showMessageDialog(mainFrame, message, "Error", JOptionPane.ERROR_MESSAGE);
    }
}