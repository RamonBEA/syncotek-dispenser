package com.idear.devices.dispenser;

import com.idear.devices.dispenser.comm.JSerialCommSerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;
import com.idear.devices.dispenser.command.*;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Dispenser joins all commands supported,
 * Some commands can be dependent of other commands like Advance Check Status Command,
 * This command is necessary to complete the execution of others commands like:
 * - Move Card Command
 * - Capture Card Command
 * - Reset Command
 **/
public class Dispenser {

    public MoveCardCommand moveCardCommand;
    public CaptureCardCommand captureCardCommand;
    public AdvanceCheckStatusCommand advanceCheckStatusCommand;
    public GetDispenserModeCommand getDispenserModeCommand;
    public SetDispenserModeCommand setDispenserModeCommand;
    public ResetCommand resetCommand;

    /**
     * When we need to start the device, just we will need specify the port name,
     * and we will be able to exec any command supported
     * @param port Port name which be assigned to our device
     * @throws DispenserException If the connection process fails
     */
    public Dispenser(String port) throws DispenserException {
        try {
            SerialPortHandler serialPortHandler =
                    new JSerialCommSerialPortHandler(port, 9600, 8, 1, 0);

            advanceCheckStatusCommand = new AdvanceCheckStatusCommand(serialPortHandler);
            moveCardCommand = new MoveCardCommand(serialPortHandler, advanceCheckStatusCommand);
            captureCardCommand = new CaptureCardCommand(serialPortHandler, advanceCheckStatusCommand);
            resetCommand = new ResetCommand(serialPortHandler, advanceCheckStatusCommand);
            getDispenserModeCommand = new GetDispenserModeCommand(serialPortHandler);
            setDispenserModeCommand = new SetDispenserModeCommand(serialPortHandler);

        } catch (SerialPortHandlerException e) {
            throw new DispenserException(DispenserException.DispenserError.DISPENSER_INITIAL_CONNECTION_ERROR, e);
        }
    }
}
