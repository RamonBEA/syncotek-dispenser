package com.idear.devices.dispenser;
/**
 * @author rperez (ramon.perez@sistemabea.mx)
 **/
public class DispenserException extends Exception{
    public DispenserException(DispenserError dispenserError) {
        super(dispenserError.name());
    }

    public DispenserException(DispenserError dispenserError, Throwable cause) {
        super(dispenserError.name(), cause);
    }

    /**
     * Possible errors
     */
    public enum DispenserError{
        DISPENSER_COMMUNICATION_ERROR,
        DISPENSER_INITIAL_CONNECTION_ERROR
    }
}
