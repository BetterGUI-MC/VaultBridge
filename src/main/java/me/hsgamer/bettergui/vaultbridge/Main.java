package me.hsgamer.bettergui.vaultbridge;

import java.util.logging.Level;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public void onEnable() {
    VaultBridge.setup();
    if (VaultBridge.hasValidEconomy()) {
      getPlugin().getLogger()
          .log(Level.INFO, "Added Economy support from Vault ({0})", VaultBridge.getEconomyName());
      VariableManager.register("money",
          (executor, identifier) -> String.valueOf(VaultBridge.getMoney(executor)));
      VariableManager.register("money_formatted",
          (executor, identifier) -> VaultBridge.formatMoney(VaultBridge.getMoney(executor)));
      CommandBuilder.register(GiveMoneyCommand::new, "give-money:");
      RequirementBuilder.register(MoneyRequirement::new, "money");
    }
    if (VaultBridge.hasValidPermission()) {
      getPlugin().getLogger()
          .log(Level.INFO, "Added Group support from Vault ({0})", VaultBridge.getPermissionName());
      VariableManager
          .register("group", (executor, identifier) -> VaultBridge.getPrimaryGroup(executor));
    }
  }
}
