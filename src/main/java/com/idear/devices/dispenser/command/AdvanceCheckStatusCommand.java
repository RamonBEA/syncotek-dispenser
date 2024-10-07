package com.idear.devices.dispenser.command;

/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Return the actual state of the dispenser,
 * and some cases we need sent it constantly to review that the previous command was executed correctly
 */
public class AdvanceCheckStatusCommand extends WrappedCommand {

    public AdvanceCheckStatusCommand() {
        data = new byte[]{0x41, 0x50};
        name = "Advance Check Status";
    }
}
