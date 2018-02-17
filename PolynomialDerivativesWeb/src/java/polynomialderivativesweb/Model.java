package polynomialderivativesweb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


import java.util.Vector;

/**
 * Representation of an application logic.
 *
 * @author Arkadiusz Pilarski
 * @version 3.0
 */
public class Model {

    /**
     * Stores a degree of a polynomial.
     */
    private int degree;

    /**
     * Stores an order of a derivative.
     */
    private int order;

    /**
     * Sores polynomial coefficients values.
     */
    private double[] coefficients;

    /**
     * Stores polynomial calculated coefficients values.
     */
    private double[] calculatedCoefficients;

    /**
     * Stores a boolean value in order to keep information if the calculations
     * have been executed or not.
     */
    private boolean calculated;

    /**
     * Polynomial degree getter.
     */
    public int getDegree() {
        return degree;
    }

    /**
     * Derivative order getter.
     */
    public int getOrder() {
        return order;
    }

    /**
     * Entered coefficients getter.
     */
    public double[] getCoefficients() {
        return coefficients;
    }

    /**
     * Calculated coefficients getter.
     */
    public double[] getCalculatedCoefficients() {
        return calculatedCoefficients;
    }

    /**
     * Calculated value getter.
     */
    public boolean getCalculated() {
        return calculated;
    }

    /**
     * Calculated value setter.
     *
     * @param calculated boolean value that keeps information if the
     * calculations have been executed or not.
     */
    public void setCalculated(boolean calculated) {
        this.calculated = calculated;
    }

    /**
     * Calculates the coefficients of polynomial derivative of certain order
     * entered by user. New calculated values are stored in the temporary Vector
     * collection. One loop of outer "for" imitates the calculation of
     * derivative. Derivative order determines how many loops of outer "for" are
     * going to be execute. At the end of every outer "for" loop, values from
     * temporary Vector collection are moved to the sourceVector which is an
     * example of safe-type objects collection. After all loops, source table
     * stores the new coefficients values.
     *
     * SuppressWarnings indicates that the "unchecked" compiler warnings should
     * be suppressed in the annotated method.
     *
     * @param sourceTable at the beginning stores the coefficients values
     * entered by user. At the end stores the new calculated coefficients
     * values.
     * @param degree degree of an entered polynomial.
     * @param order order of an entered derivative.
     * @throws CalculationException informs that coefficients were not
     * calculated correctly or that there is a mismach between degree and
     * sourceTable values which should be the same.
     */
    @SuppressWarnings("unchecked")
    public void calculateDerivative(double[] sourceTable, double[] destinationTable, int degree, int order) throws CalculationException {
        if (sourceTable.length != degree + 1) {
            throw new CalculationException();
        }

        // Temporary Vector object that is going to be used for storing calculated values.
        Vector tempTable = new Vector();
        tempTable.setSize(degree + 1);

        // Examplary safe-type objects collection used in calculations.
        Vector<Double> sourceVector = new Vector<>();

        // Moving (by adding) elements form sourceTable to examplary sourceVector.
        for (Double element : sourceTable) {
            sourceVector.add(element);
        }

        // Implementation of a lambda expression.
        LambdaExpression.DoubleMath multiplication = (a, b) -> a * b;

        // A helpful variable imitating index of a table.
        int index;

        // Derivative calculation
        for (int j = 0; j < order; j++) {

            // Calculates new coefficients values and puts them into the temporary table.
            for (int i = 1; i <= degree; i++) {
                tempTable.set(i - 1, multiplication.operation(sourceVector.get(i), i));
            }
            // The derivative of the constant is always zero.
            if (j == 0) {
                tempTable.set(degree, 0.0);
            }

            // Saving current coefficients values by storing them in sourceVector collection.
            index = 0;
            for (Object element : tempTable) {
                sourceVector.set(index++, (double) element);
            }
        }

        // Exporting final coefficients values to the destinationTable.
        index = 0;
        for (Double element : sourceVector) {
            destinationTable[index++] = element;
        }
    }

    /**
     * Throws an exception if derivative order is higher than polynomial degree.
     *
     * @param degree degree of an entered polynomial.
     * @param order order of an entered derivative.
     * @throws DegreeToOrderRelationException an exception when derivative order
     * is higher than polynomial degree.
     * @return returns true if the given variables are suitable for the program
     * : degree value greater than order value.
     */
    public boolean checkDegreeOrderRelation(int degree, int order) throws DegreeToOrderRelationException {
        if (degree < order) {
            throw new DegreeToOrderRelationException();
        }
        return true;
    }

    /**
     * Method that checks if the parameters are correct.
     *
     * @param sourceTable a table with entered coefficients values.
     * @param degree entered polynomial degree.
     * @param order entered derivative order.
     */
    public void checkParameters(double[] sourceTable, int degree, int order) throws NumberFormatException {
        if (degree <= 0 || order < 1 || sourceTable.length != degree + 1) {
            throw new NumberFormatException();
        }
    }

    /**
     * Method that sets all parameters that are already checked and suitable for
     * the program.
     *
     * @param deg a polynomial degree.
     * @param ord a derivative order.
     * @param sourceTable a table with coefficients values.
     * @param destTable a table in which the calculated coefficients values are
     * going to be stored.
     */
    public void setParameters(int deg, int ord, double[] sourceTable, double[] destTable) {
        this.coefficients = sourceTable;
        this.degree = deg;
        this.order = ord;
        this.calculatedCoefficients = destTable;
    }

}
