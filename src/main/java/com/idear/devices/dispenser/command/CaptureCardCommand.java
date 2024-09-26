package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.DispenserStatus;
import com.idear.devices.dispenser.comm.SerialPortHandler;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Move a card from the stack cards zone or the read-write zone to capture box zone
 */
public class CaptureCardCommand extends AdvanceCommand{

    private AdvanceCheckStatusCommand advanceCheckStatusCommand;
    private final byte[] CAPTURE_CARD = {0x43, 0x50};
    public CaptureCardCommand(SerialPortHandler serialPortHandler ,
                              AdvanceCheckStatusCommand advanceCheckStatusCommand) {
        super(serialPortHandler);
        this.advanceCheckStatusCommand = advanceCheckStatusCommand;
        commandName = "Capture Card";
    }

    /**
     * Move a card to capture box zone
     * @throws DispenserException If the communication fails
     */
    public void exec() throws DispenserException {
        wrapAndExecCommand(CAPTURE_CARD);

        DispenserStatus dispenserStatus = new DispenserStatus();
        dispenserStatus.setCapturingCard(false);
        do{
            try {
                dispenserStatus = advanceCheckStatusCommand.exec();
            } catch (ErrorParsingDispenserStatus ignored) {

            }
        }while (!dispenserStatus.isCapturingCard());
    }
}
