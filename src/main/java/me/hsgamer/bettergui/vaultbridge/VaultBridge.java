package me.hsgamer.bettergui.vaultbridge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;

public class VaultBridge {

  private static Economy economy;
  private static Permission permission;

  private VaultBridge() {

  }

  /*
  ECONOMY
   */
  public static boolean setupEconomy() {
    if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager()
        .getRegistration(Economy.class);
    if (rsp == null) {
      return false;
    }
    economy = rsp.getProvider();
    return economy != null;
  }

  public static boolean hasValidEconomy() {
    return economy != null;
  }

  public static double getMoney(Player player) {
    if (!hasValidEconomy()) {
      throw new IllegalStateException("Economy plugin was not found!");
    }
    return economy.getBalance(player, player.getWorld().getName());
  }

  public static boolean hasMoney(Player player, double minimum) {
    if (!hasValidEconomy()) {
      throw new IllegalStateException("Economy plugin was not found!");
    }
    if (minimum < 0.0) {
      throw new IllegalArgumentException("Invalid amount of money: " + minimum);
    }

    double balance = economy.getBalance(player, player.getWorld().getName());

    return balance >= minimum;
  }

  /**
   * @return true if the operation was successful.
   */
  public static boolean takeMoney(Player player, double amount) {
    if (!hasValidEconomy()) {
      throw new IllegalStateException("Economy plugin was not found!");
    }
    if (amount < 0.0) {
      throw new IllegalArgumentException("Invalid amount of money: " + amount);
    }

    EconomyResponse response = economy.withdrawPlayer(player, player.getWorld().getName(), amount);
    return response.transactionSuccess();
  }

  public static boolean giveMoney(Player player, double amount) {
    if (!hasValidEconomy()) {
      throw new IllegalStateException("Economy plugin was not found!");
    }
    if (amount < 0.0) {
      throw new IllegalArgumentException("Invalid amount of money: " + amount);
    }

    EconomyResponse response = economy.depositPlayer(player, player.getWorld().getName(), amount);

    return response.transactionSuccess();
  }

  public static String formatMoney(double amount) {
    if (hasValidEconomy()) {
      return economy.format(amount);
    } else {
      return Double.toString(amount);
    }
  }

  /*
  Permission
   */
  public static boolean setupPermission() {
    if (Bukkit.getPluginManager().getPlugin("Vault") == null) {
      return false;
    }
    RegisteredServiceProvider<Permission> rsp = Bukkit.getServicesManager()
        .getRegistration(Permission.class);
    if (rsp == null) {
      return false;
    }
    permission = rsp.getProvider();
    return permission != null;
  }

  public static boolean hasValidPermission() {
    return permission != null;
  }

  public static String getPrimaryGroup(Player player) {
    if (!hasValidPermission()) {
      throw new IllegalStateException("Permission plugin not found");
    } else {
      return permission.getPrimaryGroup(player);
    }
  }
}