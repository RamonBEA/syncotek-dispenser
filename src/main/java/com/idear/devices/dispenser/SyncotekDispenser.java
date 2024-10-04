package com.idear.devices.dispenser;

import com.idear.devices.dispenser.comm.JSerialCommSerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;
import com.idear.devices.dispenser.command.*;
import com.idear.devices.dispenser.command.MoveCardCommand.Position;
import com.idear.devices.dispenser.command.SetDispenserModeCommand.DispenserMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Dispenser joins all commands supported,
 * Some commands can be dependent of other commands like Advance Check Status Command,
 * This command is necessary to complete the execution of others commands like:
 * - Move Card Command
 * - Capture Card Command
 * - Reset Command
 **/
public class SyncotekDispenser {

    private MoveCardCommand moveCardCommand;
    private CaptureCardCommand captureCardCommand;
    private AdvanceCheckStatusCommand advanceCheckStatusCommand;
    private GetDispenserModeCommand getDispenserModeCommand;
    private SetDispenserModeCommand setDispenserModeCommand;
    private ResetCommand resetCommand;
    private ReentrantLock reentrantLock = new ReentrantLock();
    private Logger logger = LoggerFactory.getLogger(SyncotekDispenser.class);

    /**
     * When we need to start the device, just we will need specify the port name,
     * and we will be able to exec any command supported
     *
     * @param port Port name which be assigned to our device
     * @throws DispenserException If the connection process fails
     */
    public SyncotekDispenser(String port) throws DispenserException {
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

    public void moveCard(Position position) throws DispenserException {
        try {
            reentrantLock.lock();
            moveCardCommand.exec(position);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void captureCard() throws DispenserException {
        try {
            reentrantLock.lock();
            captureCardCommand.exec();
        } finally {
            reentrantLock.unlock();
        }
    }

    public DispenserStatus getStatus() throws DispenserException, ErrorParsingDispenserStatus {
        try {
            reentrantLock.lock();
            return advanceCheckStatusCommand.exec();
        } finally {
            reentrantLock.unlock();
        }
    }

    public DispenserMode getDispenserMode() throws DispenserException {
        try {
            reentrantLock.lock();
            return getDispenserModeCommand.exec();
        } finally {
            reentrantLock.unlock();
        }
    }

    public void setSetDispenserMode(DispenserMode mode) throws DispenserException {
        try {
            reentrantLock.lock();
            setDispenserModeCommand.exec(mode);
        } finally {
            reentrantLock.unlock();
        }
    }

    public void reset() throws DispenserException {
        try {
            reentrantLock.lock();
            resetCommand.exec();
        }finally {
            reentrantLock.unlock();
        }
    }
}
