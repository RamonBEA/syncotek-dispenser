package com.idear.devices.dispenser.comm;

public interface SerialPortHandler {

    void sendData(byte[] command, long timeOut) throws SerialPortHandlerException;

    byte[] sendAndReceiveData(byte[] command, long timeOut) throws SerialPortHandlerException;

    void close();
}
