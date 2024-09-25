package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.comm.SerialPortHandler;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Contains the general values for a dispenser command
 */
public abstract class DispenserCommand {

    //Serial connexion
    protected SerialPortHandler serialPortHandler;
    //Milliseconds to wait when a command is executed
    protected final long WAIT_TIME = 100;
    //First byte of the frame
    protected final byte STX = 0x02;
    //Last byte of the frame
    protected final byte ETX = 0x03;
    //Byte received when a command was executed ok
    protected final byte ACK = 0x06;
    //Byte received when a command was not executed ok
    protected final byte NAK = 0x15;
    //Dispenser default address
    protected final byte[] DEFAULT_ADDRESS = {0x31, 0x35};


    public DispenserCommand(SerialPortHandler serialPortHandler) {
        this.serialPortHandler = serialPortHandler;
    }
}
