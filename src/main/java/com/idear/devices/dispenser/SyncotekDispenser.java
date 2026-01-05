package com.idear.devices.dispenser;

import com.idear.devices.dispenser.comm.JSerialCommSerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;
import com.idear.devices.dispenser.command.*;
import com.idear.devices.dispenser.command.MoveCardCommand.Position;
import com.idear.devices.dispenser.command.SetDispenserModeCommand.DispenserMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;

import static com.idear.devices.dispenser.DispenserException.DispenserError.DISPENSER_COMMUNICATION_ERROR;

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

    private Logger logger = LoggerFactory.getLogger(SyncotekDispenser.class);
    private CommandExecutor commandExecutor;
    private SerialPortHandler serialPortHandler;
    protected final byte ACK = 0x06;
    protected final byte NAK = 0x15;
    private final ReentrantLock lock = new ReentrantLock();

    /**
     * When we need to start the device, just we will need specify the port name,
     * and we will be able to exec any command supported
     *
     * @param port Port name which be assigned to our device
     * @throws DispenserException If the connection process fails
     */
    public SyncotekDispenser(String port) throws DispenserException {
        try {
            serialPortHandler =
                    new JSerialCommSerialPortHandler(port, 9600, 8, 1, 0);
            commandExecutor = new CommandExecutor(serialPortHandler);
            logger.info("Starting dispenser on {} port successfully", port);

        } catch (SerialPortHandlerException e) {
            throw new DispenserException(DispenserException.DispenserError.DISPENSER_INITIAL_CONNECTION_ERROR, e);
        }
    }

    public void moveCard(Position position) throws DispenserException {
        try {
            lock.lock();
            MoveCardCommand moveCardCommand = new MoveCardCommand(position);
            execCommandWithEnqResponseLess(moveCardCommand);
            logger.info("Moving card to position {}", position);
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            lock.unlock();
        }
    }

    private byte[] execCommandWithEnq(WrappedCommand command) throws DispenserException {
        byte[] response = commandExecutor.execute(command);

        if (response[0] == NAK)
            throw new DispenserException(DISPENSER_COMMUNICATION_ERROR);

        EnqCommand enqCommand = new EnqCommand();
        return commandExecutor.execute(enqCommand);
    }

    private void execCommandWithEnqResponseLess(WrappedCommand command) throws DispenserException {
        byte[] response = commandExecutor.execute(command);

        if (response[0] == NAK)
            throw new DispenserException(DISPENSER_COMMUNICATION_ERROR);

        EnqCommand enqCommand = new EnqCommand();
        commandExecutor.executeResponseLess(enqCommand);
    }


    public void captureCard() throws DispenserException {
        try {
            lock.lock();
            CaptureCardCommand captureCardCommand = new CaptureCardCommand();
            execCommandWithEnqResponseLess(captureCardCommand);
            logger.info("Capturing card");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } finally {
            lock.unlock();
        }
    }

    public void setDispenserMode(DispenserMode mode) throws DispenserException {
        try {
            lock.lock();
            SetDispenserModeCommand setDispenserModeCommand = new SetDispenserModeCommand(mode);
            execCommandWithEnqResponseLess(setDispenserModeCommand);
            logger.info("Setting dispenser to mode {}", mode);
        } finally {
            lock.unlock();
        }
    }

    public DispenserMode getDispenserMode() throws DispenserException {
        try {
            lock.lock();
            GetDispenserModeCommand getDispenserModeCommand = new GetDispenserModeCommand();
            byte[] response = execCommandWithEnq(getDispenserModeCommand);
            DispenserMode mode = DispenserMode.find(response[5]);
            logger.info("Getting dispenser mode {}", mode);
            return mode;
        } finally {
            lock.unlock();
        }
    }

    public void reset() throws DispenserException {
        try {
            lock.lock();
            ResetCommand resetCommand = new ResetCommand();
            execCommandWithEnqResponseLess(resetCommand);
            logger.info("Resetting dispenser");
        } finally {
            lock.unlock();
        }
    }

    public DispenserStatus getStatus() throws DispenserException, ErrorParsingDispenserStatus {
        try {
            lock.lock();
            AdvanceCheckStatusCommand advanceCheckStatusCommand = new AdvanceCheckStatusCommand();
            byte[] response = execCommandWithEnq(advanceCheckStatusCommand);
            logger.debug("Getting dispenser status");
            DispenserStatus dispenserStatus = DispenserStatusParser.parse(response);
            logger.debug(dispenserStatus.toString());
            return dispenserStatus;
        } finally {
            lock.unlock();
        }
    }

    public void disconnect() {
        if(serialPortHandler != null)
            serialPortHandler.close();
    }
}
