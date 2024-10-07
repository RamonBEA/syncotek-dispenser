package com.idear.devices.dispenser.command;

import java.util.Arrays;

public class WrappedCommand extends Command {

    private static byte STX = 0x02;
    private static byte ETX = 0x03;

    @Override
    byte[] buildCommand() {
        byte[] wrapData = new byte[5 + data.length];
        wrapData[0] = STX;
        wrapData[1] = DEFAULT_ADDRESS[0];
        wrapData[2] = DEFAULT_ADDRESS[1];
        System.arraycopy(data, 0, wrapData, 3, data.length);
        wrapData[wrapData.length - 2] = ETX;
        wrapData[wrapData.length - 1] =
                calculateCheckSum(Arrays.copyOf(wrapData, wrapData.length - 1));
        return wrapData;
    }

    private byte calculateCheckSum(byte[] dataToCalculate) {
        byte crcVal = 0;
        for (byte data : dataToCalculate) {
            crcVal ^= data;
        }
        return crcVal;
    }
}
