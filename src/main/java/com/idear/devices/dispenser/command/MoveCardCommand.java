package com.idear.devices.dispenser.command;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command to move a card of three diferent ways
 */
public class MoveCardCommand extends WrappedCommand {

    public MoveCardCommand(Position position) {
        data = new byte[]{0x46, 0x43, position.value};
        name = "Move Card";
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
