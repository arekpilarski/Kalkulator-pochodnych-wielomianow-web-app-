package polynomialderivativesweb;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


/**
 * An exception that is thrown when the entered derivative order is higher
 * than the polynomial degree entered before.
 * 
 * @author Arkadiusz Pilarski
 * @version 2.0
 */
public class DegreeToOrderRelationException extends Exception{
    
    /**
    * Calls the Exception constructor.
    */
    public DegreeToOrderRelationException() { 
        super();
    }
    
    
    
}
