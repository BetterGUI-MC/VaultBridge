package me.hsgamer.bettergui.vaultbridge;

import java.util.logging.Level;
import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public boolean onLoad() {
    VaultBridge.setup();
    if (VaultBridge.hasValidEconomy() || VaultBridge.hasValidPermission()) {
      return true;
    } else {
      getPlugin().getLogger().info("Vault Not Found. Disabling");
      return false;
    }
  }

  @Override
  public void onEnable() {
    if (VaultBridge.hasValidEconomy()) {
      getPlugin().getLogger()
          .log(Level.INFO, "Added Economy support from Vault ({0})", VaultBridge.getEconomyName());
      VariableManager.register("money",
          (executor, identifier) -> VaultBridge.formatMoney(VaultBridge.getMoney(executor)));
      CommandBuilder.register("give-money:", GiveMoneyCommand.class);
      getPlugin().getMessageConfig().getConfig()
          .addDefault("no-money", "&cYou don't have enough money to do this");
      getPlugin().getMessageConfig().saveConfig();
      RequirementBuilder.register("money", MoneyRequirement.class);
    }
    if (VaultBridge.hasValidPermission()) {
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
