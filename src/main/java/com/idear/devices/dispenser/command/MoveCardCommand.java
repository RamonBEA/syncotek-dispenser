package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;
import com.idear.devices.dispenser.command.AdvanceCheckStatusCommand.DispenserStatus;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command to move a card of three diferent ways
 */
public class MoveCardCommand extends AdvanceCommand {

    private final byte[] MOVE_CARD = {0x46, 0x43};
    private AdvanceCheckStatusCommand advanceCheckStatusCommand;

    public MoveCardCommand(SerialPortHandler serialPortHandler,
                           AdvanceCheckStatusCommand advanceCheckStatusCommand) {
        super(serialPortHandler);
        this.advanceCheckStatusCommand = advanceCheckStatusCommand;
    }

    /**
     * Move the card to the specified position
     *
     * @param position Position where the card will be moved
     * @throws DispenserException If the serial connexion fails
     */
    public void exec(Position position) throws DispenserException {
        byte[] command = new byte[]{MOVE_CARD[0], MOVE_CARD[1], position.value};
        wrapAndExecCommand(command);
        DispenserStatus dispenserStatus;

        //We should send the advance command status
        // until we detect that the related sensors are actives or no
        boolean stop =true;
        do {
            dispenserStatus = advanceCheckStatusCommand.exec();

            switch (position) {
                case FRONT_HOLDING_CARD:
                    stop = dispenserStatus.isSensorOneActive() &&
                            dispenserStatus.isSensorTwoActive() &&
                            !dispenserStatus.isSensorThreeActive();
                    break;
                case FRONT_WITHOUT_HOLDING_CARD:
                    stop = !dispenserStatus.isSensorOneActive() &&
                            !dispenserStatus.isSensorTwoActive() &&
                            !dispenserStatus.isSensorThreeActive();
                    break;
                case READ_WRITE_SCAN:
                    stop = !dispenserStatus.isSensorOneActive() &&
                            dispenserStatus.isSensorTwoActive() &&
                            dispenserStatus.isSensorThreeActive();
                    break;
            }
        } while (!stop);
    }

    /**
     * Dispenser position values supported
     * - FRONT_WITHOUT_HOLDING_CARD: Eject the card from dispenser without hold
     * - FRONT_HOLDING_CARD: Eject the card from dispenser but holding on the edge
     * - READ_WRITE_SCAN: Move the card to read-write area
     */
    public enum Position {
        FRONT_WITHOUT_HOLDING_CARD((byte) 0x30),
        FRONT_HOLDING_CARD((byte) 0x34),
        READ_WRITE_SCAN((byte) 0x36);

        private byte value;

        Position(byte value) {
            this.value = value;
        }
    }
}
