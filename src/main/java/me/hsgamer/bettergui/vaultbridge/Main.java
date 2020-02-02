package me.hsgamer.bettergui.vaultbridge;

import java.util.logging.Level;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public void onEnable() {
    if (VaultBridge.setupEconomy()) {
      getPlugin().getLogger()
          .log(Level.INFO, "Added Economy support from Vault ({0})", VaultBridge.getEconomyName());
      VariableManager.register("money",
          (executor, identifier) -> VaultBridge.formatMoney(VaultBridge.getMoney(executor)));
      CommandBuilder.register("give-money:", GiveMoneyCommand.class);
      getPlugin().getMessageConfig().getConfig()
          .addDefault("no-money", "You don't have enough money to do this");
      getPlugin().getMessageConfig().saveConfig();
      RequirementBuilder.register("money", MoneyRequirement.class);
    }
    if (VaultBridge.setupPermission()) {
      getPlugin().getLogger()
          .log(Level.INFO, "Added Group support from Vault ({0})", VaultBridge.getPermissionName());
      VariableManager
          .register("group", (executor, identifier) -> VaultBridge.getPrimaryGroup(executor));
    }
  }

  @Override
  public void onDisable() {
    // Disable logic
  }
}
