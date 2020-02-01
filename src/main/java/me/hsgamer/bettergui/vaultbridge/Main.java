package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.builder.CommandBuilder;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.addon.Addon;

public final class Main extends Addon {

  @Override
  public void onEnable() {
    if (VaultBridge.setupEconomy()) {
      getPlugin().getLogger().info("Added Economy support from Vault");
      VariableManager.register("money",
          (executor, identifier) -> VaultBridge.formatMoney(VaultBridge.getMoney(executor)));
      CommandBuilder.register("give-money:", GiveMoneyCommand.class);
    }
    if (VaultBridge.setupPermission()) {
      getPlugin().getLogger().info("Added Group support from Vault");
      VariableManager
          .register("group", (executor, identifier) -> VaultBridge.getPrimaryGroup(executor));
    }
  }

  @Override
  public void onDisable() {
    // Disable logic
  }
}
