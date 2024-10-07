package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.DispenserStatus;
import com.idear.devices.dispenser.command.ErrorParsingDispenserStatus;
import com.idear.devices.dispenser.command.MoveCardCommand;
import com.idear.devices.dispenser.command.SetDispenserModeCommand;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;


class DispenserTest {

    @Test
    void moveCard() throws DispenserException, InterruptedException {

        SyncotekDispenser dispenser = new SyncotekDispenser("COM5");

        Thread moveCardTask = new Thread(() -> {
            while (true) {
                try {
                    dispenser.moveCard(MoveCardCommand.Position.FRONT_HOLDING_CARD);
                    System.out.println("Moving card to front holding card");
                    System.out.println("-----------------------------------");
                    dispenser.moveCard(MoveCardCommand.Position.READ_WRITE_SCAN);
                    System.out.println("Moving card to read write scan");
                    System.out.println("-----------------------------------");
                } catch (DispenserException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        moveCardTask.start();

        Thread getStatusTask = new Thread(() -> {
            while (true) {
                try {
                    DispenserStatus status = dispenser.getStatus();
                    System.out.println("Get Dispenser status");
                    System.out.println(status);
                    System.out.println("-----------------------------------");
                    TimeUnit.SECONDS.sleep(2);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        getStatusTask.start();

        Thread getDispenserModeTask = new Thread(() -> {
            while (true) {
                try {
                    SetDispenserModeCommand.DispenserMode mode = dispenser.getDispenserMode();
                    System.out.println("Get Dispenser mode");
                    System.out.println(mode);
                    System.out.println("-----------------------------------");
                    TimeUnit.SECONDS.sleep(5);
                } catch (DispenserException | InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        });
        getDispenserModeTask.start();

        TimeUnit.DAYS.sleep(1);
    }
}