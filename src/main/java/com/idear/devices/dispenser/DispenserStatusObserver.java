package com.idear.devices.dispenser;

import com.idear.devices.dispenser.command.DispenserStatus;

public interface DispenserStatusObserver {
    void onDispenserStatusChanged(DispenserStatus status);
}
