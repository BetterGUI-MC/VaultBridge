package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.requirement.BaseRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;

import java.util.UUID;

public class GroupRequirement extends BaseRequirement<String> {
    protected GroupRequirement(RequirementBuilder.Input input) {
        super(input);
    }

    @Override
    protected String convert(Object o, UUID uuid) {
        return StringReplacerApplier.replace(String.valueOf(o).trim(), uuid, this);
    }

    @Override
    protected Result checkConverted(UUID uuid, String s) {
        if (VaultBridge.getGroups(uuid).contains(s)) {
            return Result.success();
        } else {
            return Result.fail();
        }
    }
}
