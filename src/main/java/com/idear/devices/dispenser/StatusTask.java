package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.DispenserStatus;
import com.idear.devices.dispenser.command.ErrorParsingDispenserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class StatusTask extends Thread {

    private final Logger logger = LoggerFactory.getLogger(StatusTask.class);
    private final SyncotekDispenser syncotekDispenser;
    private final AtomicReference<DispenserStatus> dispenserStatus;
    private final AtomicBoolean activate;
    private final int intervalToStatusRequest;

    public StatusTask(SyncotekDispenser syncotekDispenser, int intervalToStatusRequest) {
        this.syncotekDispenser = syncotekDispenser;
        this.setName("StatusTask");
        dispenserStatus = new AtomicReference<>();
        this.activate = new AtomicBoolean(false);
        this.intervalToStatusRequest = intervalToStatusRequest;
    }

    public DispenserStatus getDispenserStatus() {
        return dispenserStatus.get();
    }

    @Override
    public void run() {
        logger.info("Dispenser StatusTask started, interval to {} ms", intervalToStatusRequest);
        while (true) {
            while (activate.get()) {
                try {
                    dispenserStatus.set(syncotekDispenser.getStatus());
                    TimeUnit.MILLISECONDS.sleep(intervalToStatusRequest);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error("Error getting dispenser status [{}]", e.getMessage());
                }
            }
        }
    }

    public synchronized boolean isActivate() {
        return activate.get();
    }

    public synchronized void setActivate(boolean activate) {
        this.activate.set(activate);
    }
}
