package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.builder.ActionBuilder;
import me.hsgamer.bettergui.util.SchedulerUtil;
import me.hsgamer.hscore.action.common.Action;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import me.hsgamer.hscore.task.element.TaskProcess;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class GiveMoneyAction implements Action {
    private final String value;

    protected GiveMoneyAction(ActionBuilder.Input input) {
        this.value = input.getValue();
    }

    @Override
    public void apply(UUID uuid, TaskProcess process, StringReplacer stringReplacer) {
        String parsed = stringReplacer.replaceOrOriginal(value, uuid);
        Optional<Double> optionalMoney = Validate.getNumber(parsed).map(BigDecimal::doubleValue);
        if (!optionalMoney.isPresent()) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Invalid money amount: " + parsed));
            process.next();
            return;
        }
        double moneyToGive = optionalMoney.get();
        if (moneyToGive > 0) {
            SchedulerUtil.global().run(() -> {
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
