package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.ErrorParsingDispenserStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class StatusTask extends Thread {

    private final Logger logger = LoggerFactory.getLogger(StatusTask.class);
    private final SyncotekDispenser syncotekDispenser;
    private final AtomicBoolean terminate;
    private final AtomicBoolean activate;
    private final int intervalToStatusRequest;
    private final DispenserStatusObserver dispenserStatusObserver;

    public StatusTask(SyncotekDispenser syncotekDispenser, int intervalToStatusRequest, DispenserStatusObserver dispenserStatusObserver) {
        this.syncotekDispenser = syncotekDispenser;
        this.setName("StatusTask");
        this.terminate = new AtomicBoolean(true);
        this.activate = new AtomicBoolean(false);
        this.intervalToStatusRequest = intervalToStatusRequest;
        this.dispenserStatusObserver = dispenserStatusObserver;
    }

    @Override
    public void run() {
        logger.info("Dispenser StatusTask started, interval to {} ms", intervalToStatusRequest);
        while (terminate.get()) {
            while (activate.get()) {
                try {
                    dispenserStatusObserver.onDispenserStatusChanged(syncotekDispenser.getStatus());
                    TimeUnit.MILLISECONDS.sleep(intervalToStatusRequest);
                } catch (DispenserException | ErrorParsingDispenserStatus | InterruptedException e) {
                    logger.error("Error getting dispenser status [{}]", e.getMessage());
                }
            }
        }
    }

    public synchronized void terminate() {
        terminate.set(false);
    }

    public synchronized boolean isActivate() {
        return activate.get();
    }

    public synchronized void setActivate(boolean activate) {
        this.activate.set(activate);
    }
}
