package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.idear.devices.dispenser.DispenserException.DispenserError.DISPENSER_COMMUNICATION_ERROR;

public class CommandExecutor {

    private Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    private SerialPortHandler serialPortHandler;

    public CommandExecutor(SerialPortHandler serialPortHandler) {
        this.serialPortHandler = serialPortHandler;
    }

    public byte[] execute(Command command) throws DispenserException {

        try {
            byte[] data = command.buildCommand();
            logger.debug("Executing command: {}, data: {}", command.name, data);
            return serialPortHandler.sendAndReceiveData(
                    data, 100);
        } catch (SerialPortHandlerException e) {
            throw new DispenserException(DISPENSER_COMMUNICATION_ERROR);
        }
    }
}
