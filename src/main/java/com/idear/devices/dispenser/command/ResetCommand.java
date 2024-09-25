package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.command.AdvanceCheckStatusCommand.DispenserStatus;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command to reset the dispenser
 */
public class ResetCommand extends AdvanceCommand{
    private AdvanceCheckStatusCommand advanceCheckStatusCommand;
    private final byte[] RESET_MACHINE = {0x52, 0x53};
    public ResetCommand(SerialPortHandler serialPortHandler,
                        AdvanceCheckStatusCommand advanceCheckStatusCommand) {
        super(serialPortHandler);
        this.advanceCheckStatusCommand = advanceCheckStatusCommand;
    }

    /**
     * Reset the dispenser
     * @throws DispenserException If the communication fails
     */
    public void exec() throws DispenserException {
        wrapAndExecCommand(RESET_MACHINE);

        DispenserStatus dispenserStatus;

        //Send the advance status command until dispenser error disappears
        do {
            dispenserStatus = advanceCheckStatusCommand.exec();
        }while (!dispenserStatus.isDispenseError());
    }
}
