package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Configure the dispenser mode, there are three diferent modes: DISABLE, CAPTURE_CARD, READ_WRITE_CARD
 */
public class SetDispenserModeCommand extends AdvanceCommand{

    private final byte[] DISPENSER_MODE = {0x49, 0x4E};
    public SetDispenserModeCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
    }

    /**
     * Set the dispenser mode chosen
     * @param dispenserMode Dispenser mode to set
     * @throws DispenserException If the communication fails
     */
    public void exec(DispenserMode dispenserMode) throws DispenserException {
        byte[] command = new byte[]{DISPENSER_MODE[0], DISPENSER_MODE[1], dispenserMode.value};
        wrapAndExecCommand(command);
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

        public static DispenserMode getDispenserMode(byte valueToSearch){
            for (DispenserMode dispenserMode: DispenserMode.values()) {
                if(dispenserMode.value == valueToSearch)
                    return dispenserMode;
            }
            return UNKNOWN;
        }
    }
}
