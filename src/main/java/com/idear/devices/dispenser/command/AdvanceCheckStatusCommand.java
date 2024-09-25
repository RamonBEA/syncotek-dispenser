package com.idear.devices.dispenser.command;

import com.idear.devices.dispenser.DispenserException;
import com.idear.devices.dispenser.comm.SerialPortHandler;


/**
 * @author rperez (ramon.perez@sistemabea.mx)
 * Return the actual state of the dispenser,
 * and some cases we need sent it constantly to review that the previous command was executed correctly
 */
public class AdvanceCheckStatusCommand extends AdvanceCommand {

    private final byte[] ADVANCE_CHECK_STATUS = {0x41, 0x50};


    public AdvanceCheckStatusCommand(SerialPortHandler serialPortHandler) {
        super(serialPortHandler);
    }

    /**
     * Return the dispenser status
     * @return Dispenser status
     * @throws DispenserException If the communication fails
     */
    public DispenserStatus exec() throws DispenserException {
        byte[] response = wrapAndExecCommand(ADVANCE_CHECK_STATUS);
        return new DispenserStatus(response);
    }

    /**
     * Map a byte array to different dispenser status, and each status can its own method to known it
     */
    public class DispenserStatus {
        private boolean captureCardBoxFull;
        private boolean dispensingCard;
        private boolean capturingCard;
        private boolean dispenseError;
        private boolean captureError;
        private boolean cardOverlapped;
        private boolean cardJammed;
        private boolean cardPreEmpty;
        private boolean cardStackerEmpty;
        private boolean sensorThreeActive;
        private boolean sensorTwoActive;
        private boolean sensorOneActive;

        private DispenserStatus() {
        }

        public DispenserStatus(byte[] status) throws DispenserException {
            //status must have length equals to 11
            if (status.length != 11)
                throw new DispenserException(DispenserException.DispenserError.DISPENSER_COMMUNICATION_ERROR);
            //Response structure like byte array
            //[STX, DIR, DIR, COMMAND, COMMAND, STATUS, STATUS, STATUS, STATUS, ETX, CHECKSUM]
            captureCardBoxFull = (status[5] & 0x01) == 0x01;
            dispensingCard = (status[6] & 0x08) == 0x08;
            capturingCard = (status[6] & 0x04) == 0x04;
            dispenseError = (status[6] & 0x02) == 0x02;
            captureError = (status[6] & 0x01) == 0x01;
            cardOverlapped = (status[7] & 0x04) == 0x04;
            cardJammed = (status[7] & 0x02) == 0x02;
            cardPreEmpty = (status[7] & 0x01) == 0x01;
            cardStackerEmpty = (status[8] & 0x08) == 0x08;
            sensorThreeActive = (status[8] & 0x04) == 0x04;
            sensorTwoActive = (status[8] & 0x02) == 0x02;
            sensorOneActive = (status[8] & 0x01) == 0x01;
        }

        public boolean isCaptureCardBoxFull() {
            return captureCardBoxFull;
        }

        public boolean isDispensingCard() {
            return dispensingCard;
        }

        public boolean isCapturingCard() {
            return capturingCard;
        }

        public boolean isDispenseError() {
            return dispenseError;
        }

        public boolean isCaptureError() {
            return captureError;
        }

        public boolean isCardOverlapped() {
            return cardOverlapped;
        }

        public boolean isCardJammed() {
            return cardJammed;
        }

        public boolean isCardPreEmpty() {
            return cardPreEmpty;
        }

        public boolean isCardStackerEmpty() {
            return cardStackerEmpty;
        }

        public boolean isSensorThreeActive() {
            return sensorThreeActive;
        }

        public boolean isSensorTwoActive() {
            return sensorTwoActive;
        }

        public boolean isSensorOneActive() {
            return sensorOneActive;
        }

        @Override
        public String toString() {
            return "DispenserStatus{" +
                    "captureCardBoxFull=" + captureCardBoxFull +
                    ", dispensingCard=" + dispensingCard +
                    ", capturingCard=" + capturingCard +
                    ", dispenseError=" + dispenseError +
                    ", captureError=" + captureError +
                    ", cardOverlapped=" + cardOverlapped +
                    ", cardJammed=" + cardJammed +
                    ", cardPreEmpty=" + cardPreEmpty +
                    ", cardStackerEmpty=" + cardStackerEmpty +
                    ", sensorThreeActive=" + sensorThreeActive +
                    ", sensorTwoActive=" + sensorTwoActive +
                    ", sensorOneActive=" + sensorOneActive +
                    '}';
        }
    }
}
