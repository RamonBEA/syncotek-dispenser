package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.DispenserStatus;
import com.idear.devices.dispenser.comm.SerialPortHandler;

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
        commandName = "Reset";
    }

    /**
     * Reset the dispenser
     * @throws DispenserException If the communication fails
     */
    public void exec() throws DispenserException {
        wrapAndExecCommand(RESET_MACHINE);

        DispenserStatus dispenserStatus = new DispenserStatus();
        dispenserStatus.setDispenseError(true);
        //Send the advance status command until dispenser error disappears
        do {
            try {
                dispenserStatus = advanceCheckStatusCommand.exec();
            } catch (ErrorParsingDispenserStatus e) {
                System.out.println("Error parsing error: " + e.getMessage());
            }
        }while (!dispenserStatus.isDispenseError());
    }
}
