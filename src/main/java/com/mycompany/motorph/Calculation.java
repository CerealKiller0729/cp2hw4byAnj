/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.motorph;

import java.text.DecimalFormat;
/**
 *
 * @author angeliquerivera
 */

public abstract class Calculation {

    // Decimal formatter for consistent number formatting
    protected static final DecimalFormat decimalFormat = new DecimalFormat("#.##");

    /**
     * Abstract method to perform the calculation.
     * @return The result of the calculation as a double.
     */
    protected abstract double calculate();

    /**
     * Formats a numeric value using the decimal formatter.
     * @param value The value to format.
     * @return The formatted value as a String.
     */
    protected String format(double value) {
        return decimalFormat.format(value);
    }
}