package com.idear.devices.dispenser.command;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command to reset the dispenser
 */
public class ResetCommand extends WrappedCommand {

    public ResetCommand() {
        data = new byte[]{0x52, 0x53};
        name = "Reset";
    }
}
