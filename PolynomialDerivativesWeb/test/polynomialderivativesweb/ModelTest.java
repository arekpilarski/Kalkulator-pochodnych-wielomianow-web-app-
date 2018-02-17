/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package polynomialderivativesweb;

import org.junit.*;
import static org.junit.Assert.*;

/**
 * Test case of Model methods. Tests both public methods contained by Model
 * class vith various parameters.
 *
 * @author Arkadiusz Pilarski
 * @version 3.0
 */
public class ModelTest {

    Model model = new Model();

    @Test
    public void testCalculations() {

        double[] table = new double[]{1.0, 2.0, 3.0};
        double[] table2 = new double[3];
        double[] expected = new double[]{2.0, 6.0, 0.0};
        int degree = 2, order = 1;

        try {
            model.calculateDerivative(table, table2, degree, order);
            assertArrayEquals("Comparing tables", table2, expected, 0);

            // Data out of field.
            model.calculateDerivative(expected, table2, degree + 1, order);
            fail("Should have thrown an CalculationException due to mismatch between degree value and table length.");
        } catch (CalculationException exc) {

        }
    }

    @Test
    public void testNullParameter() {

        try {
            model.calculateDerivative(null, null, 3, 2);
            fail("Should have thrown a NullPointerException due to no table passed.");

        } catch (CalculationException exc) {

        } catch (NullPointerException exc) {

        }
    }

    @Test
    public void testCheckDegreeOrderRelation() {
        int degree = 3;
        int order = 2;
        try {
            assertTrue(model.checkDegreeOrderRelation(degree, order));
            assertTrue(model.checkDegreeOrderRelation(1, 1));

            // Data out of field.
            model.checkDegreeOrderRelation(degree, 2 * order);
            fail("Should an exception occured due to degree to order relation");
        } catch (DegreeToOrderRelationException exc) {

        }
    }

    @Test
    public void testCheckParameters() {
        double[] table = new double[]{1, 2, 3, 4};
        int degreeIncorrect = 2;
        int degreeCorrect = 3;
        int order = 2;
        try {
            // Correct data.
            model.checkParameters(table, degreeCorrect, order);

            // Data out of field.
            model.checkParameters(table, degreeIncorrect, order);
            fail("Should have thrown an exception due to unsuitable degree value");
        } catch (NumberFormatException e) {

        }
        
        degreeIncorrect = -1;
        try {
            model.checkParameters(table, degreeIncorrect, order);
            fail("Should have thrown a NumberFormatException due to negative degree value.");
        } catch (NumberFormatException e) {
            
        }
        
        order = -1;
        try {
            model.checkParameters(table, degreeCorrect, order);
            fail("Should have thrown a NumberFormatException due to negative order value.");
        } catch (NumberFormatException e) {
            
        }
        
        degreeCorrect = 1;
        order = 1;
        table = new double [] {1,2};
        
        try {
            model.checkParameters(table, degreeCorrect, order);
        } catch (Exception e) {
            fail("Boundary values has been entered. No exception should be thrown.");
        }
        
    }

}
