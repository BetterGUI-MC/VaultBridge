package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.addon.BetterGUIAddon;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;

import java.util.logging.Level;

public final class Main extends BetterGUIAddon {

    @Override
    public void onEnable() {
        VaultBridge.setup();
        if (VaultBridge.hasValidEconomy()) {
            getPlugin().getLogger().log(Level.INFO, "Added Economy support from Vault ({0})", VaultBridge.getEconomyName());
            VariableManager.register("money", (original, uuid) -> String.valueOf(VaultBridge.getMoney(uuid)));
            VariableManager.register("money_formatted", (original, uuid) -> VaultBridge.formatMoney(VaultBridge.getMoney(uuid)));
            ActionBuilder.INSTANCE.register(GiveMoneyAction::new, "give-money");
            RequirementBuilder.INSTANCE.register(MoneyRequirement::new, "money");
        }
        if (VaultBridge.hasValidPermission()) {
            getPlugin().getLogger().log(Level.INFO, "Added Group support from Vault ({0})", VaultBridge.getPermissionName());
            VariableManager.register("group", (original, uuid) -> VaultBridge.getPrimaryGroup(uuid));
            RequirementBuilder.INSTANCE.register(GroupRequirement::new, "group");
        }
    }
}
