package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.command.SetDispenserModeCommand.DispenserMode;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command used for know what is the dispenser mode
 */
public class GetDispenserModeCommand extends AdvanceCommand{

    private final byte[] GET_DISPENSER_MODE = {0x53, 0x49};
    public GetDispenserModeCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
        commandName = "Get Dispenser Mode";
    }

    /**
     * Get the actual dispenser mode
     * @return The actual dispenser mode, with possible values like DISABLE, CAPTURE_CARD and READ_WRITE_CARD
     * @throws DispenserException If the communication fails
     */
    public DispenserMode exec() throws DispenserException {
        byte [] response = wrapAndExecCommand(GET_DISPENSER_MODE);
        return DispenserMode.getDispenserMode(response[5]);
    }
}
