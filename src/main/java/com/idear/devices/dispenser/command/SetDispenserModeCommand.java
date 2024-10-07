package com.idear.devices.dispenser.command;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Configure the dispenser mode, there are three diferent modes: DISABLE, CAPTURE_CARD, READ_WRITE_CARD
 */
public class SetDispenserModeCommand extends WrappedCommand{

    public SetDispenserModeCommand(DispenserMode dispenserMode) {
        data = new byte[]{0x49, 0x4E, dispenserMode.value};
        name = "Set Dispenser Mode";
    }

    /**
     * It is possible chose between three modes
     * - DISABLE : When we try to introduce a card, the dispenser will not activate its motor
     *   to move the card into.
     * - CAPTURE_CARD: When we try to introduce a card, the dispenser will move the card until the capture zone.
     * - READ_WRITE_CARD: When we try to introduce a card, the dispenser will move the card until read-write zone.
     */
    public enum DispenserMode{
        DISABLE((byte) 0x30),
        CAPTURE_CARD((byte) 0x31),
        READ_WRITE_CARD((byte) 0x32),
        UNKNOWN((byte) 0x00);

        private byte value;

        DispenserMode(byte value) {
            this.value = value;
        }

        public static DispenserMode find(byte valueToSearch){
            for (DispenserMode dispenserMode: DispenserMode.values()) {
                if(dispenserMode.value == valueToSearch)
                    return dispenserMode;
            }
            return UNKNOWN;
        }
    }
}
