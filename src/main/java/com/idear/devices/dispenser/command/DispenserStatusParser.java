package com.idear.devices.dispenser.command;

public class DispenserStatusParser {
    private DispenserStatusParser() {}

    public static DispenserStatus parse(byte[] status) throws ErrorParsingDispenserStatus {
        //status must have length equals to 11
        if (status.length != 11)
            throw new ErrorParsingDispenserStatus("Status must be 11 bytes");

        DispenserStatus dispenserStatus = new DispenserStatus();
        //Response structure like byte array
        //[STX, DIR, DIR, COMMAND, COMMAND, STATUS, STATUS, STATUS, STATUS, ETX, CHECKSUM]
        dispenserStatus.setCaptureCardBoxFull((status[5] & 0x01) == 0x01);
        dispenserStatus.setDispensingCard((status[6] & 0x08) == 0x08);
        dispenserStatus.setCapturingCard((status[6] & 0x04) == 0x04);
        dispenserStatus.setDispenseError((status[6] & 0x02) == 0x02);
        dispenserStatus.setCaptureError((status[6] & 0x01) == 0x01);
        dispenserStatus.setCardOverlapped((status[7] & 0x04) == 0x04);
        dispenserStatus.setCardJammed((status[7] & 0x02) == 0x02);
        dispenserStatus.setCardPreEmpty((status[7] & 0x01) == 0x01);
        dispenserStatus.setCardStackerEmpty((status[8] & 0x08) == 0x08);
        dispenserStatus.setSensorThreeActive((status[8] & 0x04) == 0x04);
        dispenserStatus.setSensorTwoActive((status[8] & 0x02) == 0x02);
        dispenserStatus.setSensorOneActive((status[8] & 0x01) == 0x01);
        return dispenserStatus;
    }
}
