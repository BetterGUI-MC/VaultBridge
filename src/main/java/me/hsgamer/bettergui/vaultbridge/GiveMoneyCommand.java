package me.hsgamer.bettergui.vaultbridge;

import java.util.Objects;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import me.hsgamer.bettergui.object.Command;
import me.hsgamer.bettergui.util.ExpressionUtils;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class GiveMoneyCommand extends Command {

  private String errorMessage;

  public GiveMoneyCommand(String string) {
    super(string);
  }

  @Override
  public void addToTaskChain(Player player, TaskChain<?> taskChain) {
    double moneyToGive = 0;
    String parsed = getParsedCommand(player);
    if (Validate.isValidPositiveInteger(parsed)) {
      moneyToGive = Double.parseDouble(parsed);
    } else if (ExpressionUtils.isValidExpression(parsed)) {
      moneyToGive = Objects.requireNonNull(ExpressionUtils.getResult(parsed)).doubleValue();
    } else {
      errorMessage = ChatColor.RED + "Invalid money amount: " + parsed;
    }

    if (errorMessage != null) {
      player.sendMessage(errorMessage);
      return;
    }
    if (!VaultBridge.hasValidEconomy()) {
      player.sendMessage(ChatColor.RED
          + "Vault with a compatible economy plugin not found. Please inform the staff.");
      return;
    }

    if (moneyToGive > 0) {
      double finalMoneyToGive = moneyToGive;
      taskChain.sync(() -> {
        if (!VaultBridge.giveMoney(player, finalMoneyToGive)) {
          player.sendMessage(ChatColor.RED
              + "Error: the transaction couldn't be executed. Please inform the staff.");
        }
      });
    }
  }
}
