package com.idear.devices.dispenser.command;


/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Command used for know what is the dispenser mode
 */
public class GetDispenserModeCommand extends WrappedCommand{

    public GetDispenserModeCommand() {
        data = new byte[]{0x53, 0x49};
        name = "Get Dispenser Mode";
    }
}
