package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.comm.SerialPortHandlerException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

import static com.idear.devices.dispenser.DispenserException.DispenserError.*;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Advance Command is a command that needs a determinate structure and needs an Enq Command confirmation
 */
public abstract class AdvanceCommand extends DispenserCommand {

    Logger logger = LoggerFactory.getLogger(AdvanceCommand.class);

    private EnqCommand enqCommand;

    public AdvanceCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
        enqCommand = new EnqCommand(serialPortHandler);
    }

    /**
     * An Advance Command must have the next frame structure
     *  * [STX, ADDRESS_1, ADDRESS_2, COMMAND_1, COMMAND_2, PARAMETER (if exists), ETX, BCC]
     *  * STX: Begin of the frame
     *  * ADDRESS_1 and ADDRESS_2: Is the default dispenser address (0x31, 0x35)
     *  * COMMAND_1 and COMMAND_2: Is the command
     *  * PARAMETER: We could have parameters o no, for example Move Card Command has parameters like
     *  * move card to read-write position, move card to out but hold it and move the card to out
     *  * but without hold it
     *  * ETX: The end of the frame
     *  * BCC: Is the checksum of the all previous data in the frame
     *  *
     *  And all those type of commands must exec ENQ Command after of exec its command,
     *  this due to they must confirm the action and receive or no information
     * @param command Command to exec
     * @return Can return or no information
     * @throws DispenserException If the communication fails
     */
    protected byte[] wrapAndExecCommand(byte[] command) throws DispenserException {
        try {
            byte[] wrapData = new byte[5 + command.length];
            wrapData[0] = STX;
            wrapData[1] = DEFAULT_ADDRESS[0];
            wrapData[2] = DEFAULT_ADDRESS[1];
            System.arraycopy(command, 0, wrapData, 3, command.length);
            wrapData[wrapData.length - 2] = ETX;
            wrapData[wrapData.length - 1] =
                    calculateCheckSum(Arrays.copyOf(wrapData, wrapData.length - 1));
//            logger.debug("Syncotek Dispenser - Executing command:{} with data:{}",
//                    commandName, byteArrayToHexadecimal(wrapData));

            byte[] response = serialPortHandler.sendAndReceiveData(wrapData, WAIT_TIME);

            if (response[0] == NAK)
                throw new DispenserException(DISPENSER_COMMUNICATION_ERROR);

            //Confirm the execution, the dispenser do some action or return information
//            logger.debug("Syncotek Dispenser - Executing command: ENQ");
            response = enqCommand.exec();
            return response;

        } catch (SerialPortHandlerException e) {
            logger.error(e.getMessage(), e);
            throw new DispenserException(DISPENSER_COMMUNICATION_ERROR, e);
        }
    }

    /**
     * Calculate the checksum of a frame
     * @param dataToCalculate Frame to calculate
     * @return Checksum calculated
     */
    private byte calculateCheckSum(byte[] dataToCalculate) {
        byte crcVal = 0;
        for (byte data : dataToCalculate) {
            crcVal ^= data;
        }
        return crcVal;
    }

    public static String byteArrayToHexadecimal(byte[] data) {
        StringBuilder sb = new StringBuilder(data.length * 2);
        for (byte b : data)
            sb.append(String.format("%02x", b).toUpperCase());
        return sb.toString();
    }
}
