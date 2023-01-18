package org.apache.commons.io.file.spi.controller.impl;

import org.apache.commons.io.file.spi.controller.AttackManager;
import org.apache.commons.io.file.spi.utils.NumberUtils;

public class StopController {

    private final AttackManager attackManager;
    public StopController(final AttackManager attackManager) {
        this.attackManager = attackManager;
    }

    public void stop(final String id) {
        if(NumberUtils.isInteger(id)) this.attackManager.attacks.remove(Integer.parseInt(id));
        else this.attackManager.attacks.keySet().forEach(this.attackManager.attacks::remove);
    }
}
