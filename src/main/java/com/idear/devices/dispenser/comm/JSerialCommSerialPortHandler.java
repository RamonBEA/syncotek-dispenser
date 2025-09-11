package com.idear.devices.dispenser.comm;

import com.fazecast.jSerialComm.SerialPort;

import java.util.concurrent.TimeUnit;

import static com.idear.devices.dispenser.comm.SerialPortHandlerException.SerialPortError.*;

/**
 * @author rperez
 * @version 1.0
 */
public class JSerialCommSerialPortHandler implements SerialPortHandler {

    private SerialPort serialPort;

    /**
     * Starts a connexion with a serial port
     *
     * @param portName Port name for instance "/dev/ttyS0"
     * @param baudRate Communication velocity
     * @param dataBits Number of bits transmitted
     * @param stopBits Stop bits
     * @param parity   Parity
     * @throws SerialPortHandlerException It is thrown if the connexion was not successful
     */
    public JSerialCommSerialPortHandler(String portName, int baudRate, int dataBits,
                                        int stopBits, int parity) throws SerialPortHandlerException {

        serialPort = SerialPort.getCommPort(portName);
        serialPort.setComPortParameters(baudRate, dataBits, stopBits, parity);
        serialPort.setFlowControl(SerialPort.FLOW_CONTROL_DISABLED);

        if (!serialPort.openPort()) {
            throw new SerialPortHandlerException(ERROR_TRYING_START_SERIAL_PORT);
        }
    }

    /**
     * Send data to serial port without waiting response
     *
     * @param data    Data to be to send
     * @param timeOut Time to wait to send
     * @throws SerialPortHandlerException It is thrown if is interrupted
     */
    @Override
    public void sendData(byte[] data, long timeOut) throws SerialPortHandlerException {
        serialPort.writeBytes(data, data.length);
        try {
            TimeUnit.MILLISECONDS.sleep(timeOut);
        } catch (InterruptedException e) {
            throw new SerialPortHandlerException(ERROR_TRYING_SEND_DATA);
        }
    }

    /**
     * Send data to serial port and return a response
     *
     * @param data    Data to be to send
     * @param timeOut Time to wait to send
     * @return Response
     * @throws SerialPortHandlerException It is thrown if is interrupted or does not receive a response
     */
    @Override
    public byte[] sendAndReceiveData(byte[] data, long timeOut) throws SerialPortHandlerException {
        serialPort.writeBytes(data, data.length);
        byte[] buffer = new byte[3];
        try {
            TimeUnit.MILLISECONDS.sleep(timeOut);
            int size = serialPort.bytesAvailable();

            if (size <= 0)
                throw new SerialPortHandlerException(ERROR_TRYING_SEND_DATA);

            buffer = new byte[size];
            serialPort.readBytes(buffer, buffer.length);
        } catch (InterruptedException e) {
            throw new SerialPortHandlerException(ERROR_TRYING_SEND_DATA);
        }
        return buffer;
    }

    /**
     * Close the serial port communication
     */
    @Override
    public void close() {
        serialPort.closePort();
    }
}
