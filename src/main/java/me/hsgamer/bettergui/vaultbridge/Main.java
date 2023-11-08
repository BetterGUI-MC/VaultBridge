package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.addon.GetPlugin;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.expansion.common.Expansion;
import me.hsgamer.hscore.variable.VariableBundle;

import java.util.logging.Level;

public final class Main implements Expansion, GetPlugin {
    private final VariableBundle variableBundle = new VariableBundle();

    @Override
    public void onEnable() {
        VaultBridge.setup();
        if (VaultBridge.hasValidEconomy()) {
            getPlugin().getLogger().log(Level.INFO, "Added Economy support from Vault ({0})", VaultBridge.getEconomyName());
            variableBundle.register("money", StringReplacer.of((original, uuid) -> String.valueOf(VaultBridge.getMoney(uuid))));
            variableBundle.register("money_formatted", StringReplacer.of((original, uuid) -> VaultBridge.formatMoney(VaultBridge.getMoney(uuid))));
            ActionBuilder.INSTANCE.register(GiveMoneyAction::new, "give-money");
            RequirementBuilder.INSTANCE.register(MoneyRequirement::new, "money");
        }
        if (VaultBridge.hasValidPermission()) {
            getPlugin().getLogger().log(Level.INFO, "Added Group support from Vault ({0})", VaultBridge.getPermissionName());
            variableBundle.register("group", StringReplacer.of((original, uuid) -> VaultBridge.getPrimaryGroup(uuid)));
            RequirementBuilder.INSTANCE.register(GroupRequirement::new, "group");
        }
    }

    @Override
    public void onDisable() {
        variableBundle.unregisterAll();
    }
}
