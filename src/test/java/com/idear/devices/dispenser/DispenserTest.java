package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.DispenserStatus;
import com.idear.devices.dispenser.command.ErrorParsingDispenserStatus;
import com.idear.devices.dispenser.command.MoveCardCommand;
import com.idear.devices.dispenser.command.SetDispenserModeCommand;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


class DispenserTest implements DispenserStatusObserver{

    private final Logger logger = LoggerFactory.getLogger(DispenserTest.class);

    @Test
    void moveProcessGetStatusProcessGetModeProcessAtTheSameTime() throws DispenserException, InterruptedException {

        SyncotekDispenser dispenser = new SyncotekDispenser("COM11");

        Thread moveCardTask = new Thread(() -> {
            int times = 0;
            while (true) {
                try {
                    logger.info("Try number {}", times);
                    dispenser.moveCard(MoveCardCommand.Position.FRONT_HOLDING_CARD);
                    dispenser.moveCard(MoveCardCommand.Position.READ_WRITE_SCAN);
                    TimeUnit.SECONDS.sleep(5);
                    times++;
                } catch (DispenserException | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Move Card Task");
        moveCardTask.start();

        Thread getStatusTask = new Thread(() -> {
            int times = 0;
            while (true) {
                try {
                    logger.info("Try number {}", times);
                    DispenserStatus status = dispenser.getStatus();
                    logger.info("Status: {}", status);
                    TimeUnit.SECONDS.sleep(2);
                    times++;
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Get Status Task");
        getStatusTask.start();

        Thread getDispenserModeTask = new Thread(() -> {
            int times = 0;
            while (true) {
                try {
                    logger.info("Try number {}", times);
                    SetDispenserModeCommand.DispenserMode mode = dispenser.getDispenserMode();
                    TimeUnit.SECONDS.sleep(5);
                    times++;
                } catch (DispenserException | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Dispenser Mode Task");
        getDispenserModeTask.start();

        TimeUnit.DAYS.sleep(1);
    }

    @Test
    void getStatusMultipleProcess() throws DispenserException, InterruptedException {
        SyncotekDispenser dispenser = new SyncotekDispenser("COM11");
        Thread getStatusTask1 = new Thread(() -> {
            while (true) {
                try {
                    DispenserStatus status = dispenser.getStatus();
                    logger.info("Status: {}", status);
                    TimeUnit.SECONDS.sleep(1);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Get Status Task 1");
        getStatusTask1.start();

        Thread getStatusTask2 = new Thread(() -> {
            while (true) {
                try {
                    DispenserStatus status = dispenser.getStatus();
                    logger.info("Status: {}", status);
                    TimeUnit.SECONDS.sleep(1);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Get Status Task 2");
        getStatusTask2.start();

        Thread getStatusTask3 = new Thread(() -> {
            while (true) {
                try {
                    DispenserStatus status = dispenser.getStatus();
                    logger.info("Status: {}", status);
                    TimeUnit.SECONDS.sleep(1);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error(e.getMessage(), e);
                }
            }
        }, "Get Status Task 3");
        getStatusTask3.start();

        TimeUnit.DAYS.sleep(1);
    }

    @Test
    void dispenserConnection() {
        try {
            SyncotekDispenser dispenser = new SyncotekDispenser("COM11");
            dispenser.setDispenserMode(SetDispenserModeCommand.DispenserMode.CAPTURE_CARD);
        } catch (DispenserException  e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Test
    void statusTask() {
        try {
            SyncotekDispenser dispenser = new SyncotekDispenser("COM11");
            StatusTask statusTask = new StatusTask(dispenser, 500, this);
            statusTask.start();
            TimeUnit.SECONDS.sleep(5);
            statusTask.setActivate(true);
            TimeUnit.DAYS.sleep(1);
        } catch (DispenserException | InterruptedException e) {
            logger.error(e.getMessage(), e);
        }
    }

    @Override
    public void onDispenserStatusChanged(DispenserStatus status) {
        logger.info("Status: {}", status);
    }
}