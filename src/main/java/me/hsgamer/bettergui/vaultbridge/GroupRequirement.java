package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;

import java.util.UUID;

public class GroupRequirement extends BaseRequirement<String> {
    public GroupRequirement(String name) {
        super(name);
    }

    @Override
    public String getParsedValue(UUID uuid) {
        return VariableManager.setVariables(String.valueOf(value).trim(), uuid);
    }

    @Override
    public boolean check(UUID uuid) {
        return VaultBridge.getGroups(uuid).contains(getParsedValue(uuid));
    }

    @Override
    public void take(UUID uuid) {
        // EMPTY
    }
}
