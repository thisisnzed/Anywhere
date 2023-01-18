package org.anywhere.agent.controller.impl;

import org.anywhere.agent.controller.AttackManager;
import org.anywhere.agent.utils.NumberUtils;

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
