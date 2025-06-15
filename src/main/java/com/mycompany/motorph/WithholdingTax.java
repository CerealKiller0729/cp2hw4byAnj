package com.mycompany.motorph;

import java.text.DecimalFormat;

public class WithholdingTax extends Calculation {
    private static final DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    private final double taxableIncome;
    private final boolean isWeekly;
    private double tax;

    /**
     * Constructor for WithholdingTax
     * @param taxableIncome The taxable income (either weekly or monthly)
     * @param isWeekly If true, converts weekly income to monthly for tax calculation
     */
    public WithholdingTax(double taxableIncome, boolean isWeekly) {
        if (taxableIncome < 0) {
            throw new IllegalArgumentException("Taxable income cannot be negative");
        }
        this.taxableIncome = taxableIncome;
        this.isWeekly = isWeekly;
    }

    @Override
    public double calculate() {
        // Convert to monthly equivalent if input is weekly
        double monthlyIncome = isWeekly ? taxableIncome * 4 : taxableIncome;
        
        // Philippine Tax Table 2024 (Monthly Brackets)
        if (monthlyIncome <= 20832) {
            tax = 0;
        } else if (monthlyIncome <= 33333) {
            tax = (monthlyIncome - 20832) * 0.20;
        } else if (monthlyIncome <= 66667) {
            tax = 2500 + (monthlyIncome - 33333) * 0.25;
        } else if (monthlyIncome <= 166667) {
            tax = 10833 + (monthlyIncome - 66667) * 0.30;
        } else if (monthlyIncome <= 666667) {
            tax = 40833.33 + (monthlyIncome - 166667) * 0.32;
        } else {
            tax = 200833.33 + (monthlyIncome - 666667) * 0.35;
        }

        // Convert back to weekly tax if input was weekly
        return isWeekly ? tax / 4 : tax;
    }

    /**
     * Gets the breakdown of tax calculation
     * @return Formatted string showing detailed computation
     */
    public String getCalculationBreakdown() {
        double monthlyIncome = isWeekly ? taxableIncome * 4 : taxableIncome;
        StringBuilder sb = new StringBuilder();
        
        sb.append(String.format("%-25s: %s%,.2f%n", 
            isWeekly ? "Weekly Taxable Income" : "Monthly Taxable Income", 
            "₱", taxableIncome));
            
        if (isWeekly) {
            sb.append(String.format("%-25s: %s%,.2f%n", 
                "Monthly Equivalent", "₱", monthlyIncome));
        }
        
        sb.append(String.format("%-25s: %s%,.2f%n", 
            "Withholding Tax", "₱", Double.parseDouble(decimalFormat.format(calculate()))));
        
        return sb.toString();
    }

    // Getters
    public double getTaxableIncome() {
        return taxableIncome;
    }

    public boolean isWeekly() {
        return isWeekly;
    }

    public double getTax() {
        return calculate();
    }
}