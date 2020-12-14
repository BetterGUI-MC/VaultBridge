package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.lib.core.common.Validate;
import me.hsgamer.bettergui.lib.core.expression.ExpressionUtils;
import me.hsgamer.bettergui.lib.taskchain.TaskChain;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class GiveMoneyAction extends BaseAction {

    public GiveMoneyAction(String string) {
        super(string);
    }

    @Override
    public void addToTaskChain(UUID uuid, TaskChain<?> taskChain) {
        double moneyToGive = 0;
        String parsed = getReplacedString(uuid);
        if (Validate.isValidPositiveNumber(parsed)) {
            moneyToGive = Double.parseDouble(parsed);
        } else if (ExpressionUtils.isValidExpression(parsed)) {
            moneyToGive = Objects.requireNonNull(ExpressionUtils.getResult(parsed)).doubleValue();
        } else {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Invalid money amount: " + parsed));
        }

        if (moneyToGive > 0) {
            double finalMoneyToGive = moneyToGive;
            taskChain.sync(() -> {
                if (!VaultBridge.giveMoney(uuid, finalMoneyToGive)) {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
                }
            });
        }
    }
}
