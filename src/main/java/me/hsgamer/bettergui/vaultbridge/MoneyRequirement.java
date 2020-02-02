package me.hsgamer.bettergui.vaultbridge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.manager.VariableManager;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.IconRequirement;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.ExpressionUtils;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MoneyRequirement extends IconRequirement<Double> {

  private Map<UUID, List<Double>> checked = new HashMap<>();

  public MoneyRequirement(Icon icon) {
    super(icon, true);
  }

  @Override
  public List<Double> getParsedValue(Player player) {
    List<Double> list = new ArrayList<>();
    values.forEach(string -> {
      String parsed =
          VariableManager.hasVariables(string) ? VariableManager.setVariables(string, player)
              : string;
      if (ExpressionUtils.isValidExpression(parsed)) {
        list.add(ExpressionUtils.getResult(parsed).doubleValue());
      } else {
        try {
          list.add(Double.parseDouble(parsed));
        } catch (NumberFormatException e) {
          String error =
              ChatColor.RED + "Error parsing value!" + parsed + " is not a valid number";
          player.sendMessage(error);
        }
      }
    });
    return list;
  }

  @Override
  public boolean check(Player player) {
    List<Double> values = getParsedValue(player);
    if (values.isEmpty()) {
      return false;
    }
    for (double value : values) {
      if (value > 0) {
        if (!VaultBridge.hasValidEconomy()) {
          player.sendMessage(ChatColor.RED
              + "This command has a price, but Vault with a compatible economy plugin was not found. For security, the command has been blocked. Please inform the staff.");
          return false;
        }

        if (!VaultBridge.hasMoney(player, value)) {
          if (failMessage != null) {
            if (!failMessage.isEmpty()) {
              player.sendMessage(failMessage
                  .replace("{money}", VaultBridge.formatMoney(value)));
            }
          } else {
            String message = BetterGUI.getInstance().getMessageConfig()
                .get(String.class, "no-money", "&cYou don't have enough money to do this");
            if (!message.isEmpty()) {
              CommonUtils.sendMessage(player, message.replace("{money}", VaultBridge.formatMoney(value)));
            }
          }
          return false;
        }
      }
    }
    checked.put(player.getUniqueId(), values);
    return true;
  }

  @Override
  public void take(Player player) {
    checked.remove(player.getUniqueId()).forEach(value -> {
      if (!VaultBridge.takeMoney(player, value)) {
        player.sendMessage(ChatColor.RED
            + "Error: the transaction couldn't be executed. Please inform the staff.");
      }
    });
  }
}
