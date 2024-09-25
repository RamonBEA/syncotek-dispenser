package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command used for confirm that we receive a response ACK/NAK from the previous command sent,
 * when this command is received, the previous command will perform its action or will return data in the Enq response
 */
public class EnqCommand extends DispenserCommand {

    private final byte ENQ = 0x05;

    public EnqCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
    }

    /**
     * Confirm that we receive a response of the previous command
     * @return Can return or no data, this depends on the previous command executed
     * @throws DispenserException If the communication fails
     */
    public byte[] exec() throws DispenserException {
        try {
            byte[] command = new byte[]{ENQ, DEFAULT_ADDRESS[0], DEFAULT_ADDRESS[1]};
            return serialPortHandler.sendAndReceiveData(command, WAIT_TIME);
        } catch (SerialPortHandlerException e) {
            throw new DispenserException(DispenserException.DispenserError.DISPENSER_COMMUNICATION_ERROR, e);
        }
    }
}
