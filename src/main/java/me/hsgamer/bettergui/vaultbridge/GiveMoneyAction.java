package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.action.BaseAction;
import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class GiveMoneyAction extends BaseAction {
    protected GiveMoneyAction(ActionBuilder.Input input) {
        super(input);
    }

    @Override
    public void accept(UUID uuid, TaskProcess process) {
        String parsed = getReplacedString(uuid);
        Optional<Double> optionalMoney = Validate.getNumber(parsed).map(BigDecimal::doubleValue);
        if (!optionalMoney.isPresent()) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Invalid money amount: " + parsed));
            process.next();
            return;
        }
        double moneyToGive = optionalMoney.get();
        if (moneyToGive > 0) {
            Scheduler.current().sync().runTask(() -> {
                if (!VaultBridge.giveMoney(uuid, moneyToGive)) {
                    Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
                }
                process.next();
            });
        } else {
            process.next();
        }
    }
}
