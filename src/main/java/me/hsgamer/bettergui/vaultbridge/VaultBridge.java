package me.hsgamer.bettergui.vaultbridge;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

import java.util.UUID;

public class VaultBridge {

    private static Economy economy;
    private static Permission permission;

    private VaultBridge() {

    }

    public static void setup() {
        RegisteredServiceProvider<Economy> rspE = Bukkit.getServicesManager()
                .getRegistration(Economy.class);
        if (rspE != null) {
            economy = rspE.getProvider();
        }

        RegisteredServiceProvider<Permission> rspP = Bukkit.getServicesManager()
                .getRegistration(Permission.class);
        if (rspP != null) {
            permission = rspP.getProvider();
        }
    }

  /*
  ECONOMY
   */

    public static String getEconomyName() {
        return economy.getName();
    }

    public static boolean hasValidEconomy() {
        return economy != null;
    }

    public static double getMoney(UUID uuid) {
        if (!hasValidEconomy()) {
            throw new IllegalStateException("Economy plugin was not found!");
        }
        return economy.getBalance(Bukkit.getOfflinePlayer(uuid));
    }

    public static boolean hasMoney(UUID uuid, double minimum) {
        if (!hasValidEconomy()) {
            throw new IllegalStateException("Economy plugin was not found!");
        }
        if (minimum < 0.0) {
            throw new IllegalArgumentException("Invalid amount of money: " + minimum);
        }

        double balance = economy.getBalance(Bukkit.getOfflinePlayer(uuid));

        return balance >= minimum;
    }

    /**
     * @return true if the operation was successful.
     */
    public static boolean takeMoney(UUID uuid, double amount) {
        if (!hasValidEconomy()) {
            throw new IllegalStateException("Economy plugin was not found!");
        }
        if (amount < 0.0) {
            throw new IllegalArgumentException("Invalid amount of money: " + amount);
        }

        EconomyResponse response = economy.withdrawPlayer(Bukkit.getOfflinePlayer(uuid), amount);
        return response.transactionSuccess();
    }

    public static boolean giveMoney(UUID uuid, double amount) {
        if (!hasValidEconomy()) {
            throw new IllegalStateException("Economy plugin was not found!");
        }
        if (amount < 0.0) {
            throw new IllegalArgumentException("Invalid amount of money: " + amount);
        }

        EconomyResponse response = economy.depositPlayer(Bukkit.getOfflinePlayer(uuid), amount);

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
    public static String getPermissionName() {
        return permission.getName();
    }

    public static boolean hasValidPermission() {
        return permission != null;
    }

    public static String getPrimaryGroup(UUID uuid) {
        if (!hasValidPermission() || !permission.hasGroupSupport()) {
            throw new IllegalStateException("Group permission plugin not found");
        } else {
            return permission.getPrimaryGroup(null, Bukkit.getOfflinePlayer(uuid));
        }
    }
}