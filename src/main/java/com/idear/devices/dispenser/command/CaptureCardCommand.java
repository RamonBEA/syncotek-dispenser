package com.idear.devices.dispenser.command;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Move a card from the stack cards zone or the read-write zone to capture box zone
 */
public class CaptureCardCommand extends WrappedCommand{

    public CaptureCardCommand() {
        data = new byte[]{0x43, 0x50};
        name = "Capture Card";
    }
}
