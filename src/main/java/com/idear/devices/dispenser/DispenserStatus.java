package com.idear.devices.dispenser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

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

    public void setCaptureCardBoxFull(boolean captureCardBoxFull) {
        this.captureCardBoxFull = captureCardBoxFull;
    }

    public void setDispensingCard(boolean dispensingCard) {
        this.dispensingCard = dispensingCard;
    }

    public void setCapturingCard(boolean capturingCard) {
        this.capturingCard = capturingCard;
    }

    public void setDispenseError(boolean dispenseError) {
        this.dispenseError = dispenseError;
    }

    public void setCaptureError(boolean captureError) {
        this.captureError = captureError;
    }

    public void setCardOverlapped(boolean cardOverlapped) {
        this.cardOverlapped = cardOverlapped;
    }

    public void setCardJammed(boolean cardJammed) {
        this.cardJammed = cardJammed;
    }

    public void setCardPreEmpty(boolean cardPreEmpty) {
        this.cardPreEmpty = cardPreEmpty;
    }

    public void setCardStackerEmpty(boolean cardStackerEmpty) {
        this.cardStackerEmpty = cardStackerEmpty;
    }

    public void setSensorThreeActive(boolean sensorThreeActive) {
        this.sensorThreeActive = sensorThreeActive;
    }

    public void setSensorTwoActive(boolean sensorTwoActive) {
        this.sensorTwoActive = sensorTwoActive;
    }

    public void setSensorOneActive(boolean sensorOneActive) {
        this.sensorOneActive = sensorOneActive;
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
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        } catch ( JsonProcessingException ignore) {
            // TODO log
            return null;
        }
    }
}
