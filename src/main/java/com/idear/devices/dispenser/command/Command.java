package com.idear.devices.dispenser.command;

public abstract class Command {

    protected byte[] data;
    protected String name;
    protected byte[] DEFAULT_ADDRESS = {0x31, 0x35};
    abstract byte[] buildCommand();
}
