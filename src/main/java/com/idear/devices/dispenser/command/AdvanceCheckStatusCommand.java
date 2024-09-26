package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.DispenserStatus;
import com.idear.devices.dispenser.DispenserStatusParser;
import com.idear.devices.dispenser.comm.SerialPortHandler;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Return the actual state of the dispenser,
 * and some cases we need sent it constantly to review that the previous command was executed correctly
 */
public class AdvanceCheckStatusCommand extends AdvanceCommand {

    private final byte[] ADVANCE_CHECK_STATUS = {0x41, 0x50};

    public AdvanceCheckStatusCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
        commandName = "Advance Check Status";
    }

    /**
     * Return the dispenser status
     *
     * @return Dispenser status
     * @throws DispenserException If the communication fails
     */
    public DispenserStatus exec() throws DispenserException, ErrorParsingDispenserStatus {
        byte[] response = wrapAndExecCommand(ADVANCE_CHECK_STATUS);
        return DispenserStatusParser.parse(response);
    }
}
