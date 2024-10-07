package com.idear.devices.dispenser.command;


/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command used for confirm that we receive a response ACK/NAK from the previous command sent,
 * when this command is received, the previous command will perform its action or will return data in the Enq response
 */
public class EnqCommand extends Command {

    public EnqCommand() {
        data = new byte[] {0x05};
        name = "ENQ";
    }

    @Override
    byte[] buildCommand() {
        return new byte[]{data[0], DEFAULT_ADDRESS[0], DEFAULT_ADDRESS[1]};
    }
}
