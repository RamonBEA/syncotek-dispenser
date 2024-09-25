package com.idear.devices.dispenser.comm;

public class SerialPortHandlerException extends Exception {
    public SerialPortHandlerException(SerialPortError serialPortError) {
        super(serialPortError.name());
    }

    public SerialPortHandlerException(SerialPortError serialPortError, Throwable cause) {
        super(serialPortError.name(), cause);
    }

    /**
     * Possible errors when the serial communication fails
     */
    enum SerialPortError{
        ERROR_TRYING_START_SERIAL_PORT,
        ERROR_WAITING_FOR_DATA_RESPONSE,
        ERROR_TRYING_SEND_DATA
    }
}


