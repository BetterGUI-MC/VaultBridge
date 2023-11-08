package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.builder.RequirementBuilder;
import me.hsgamer.bettergui.util.StringReplacerApplier;
import me.hsgamer.hscore.bukkit.scheduler.Scheduler;
import me.hsgamer.hscore.bukkit.utils.MessageUtils;
import me.hsgamer.hscore.common.StringReplacer;
import me.hsgamer.hscore.common.Validate;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public class MoneyRequirement extends TakableRequirement<Double> {
    protected MoneyRequirement(RequirementBuilder.Input input) {
        super(input);
        getMenu().getVariableManager().register(getName(), StringReplacer.of((original, uuid) -> {
            double money = getFinalValue(uuid);
            if (money > 0 && !VaultBridge.hasMoney(uuid, money)) {
                return String.valueOf(money);
            }
            return BetterGUI.getInstance().getMessageConfig().getHaveMetRequirementPlaceholder();
        }));
    }

    @Override
    protected boolean getDefaultTake() {
        return true;
    }

    @Override
    protected Object getDefaultValue() {
        return "0";
    }

    @Override
    protected Double convert(Object o, UUID uuid) {
        String parsed = StringReplacerApplier.replace(String.valueOf(o).trim(), uuid, this);
        return Validate.getNumber(parsed).map(BigDecimal::doubleValue).orElseGet(() -> {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, BetterGUI.getInstance().getMessageConfig().getInvalidNumber(parsed)));
            return 0D;
        });
    }

    @Override
    protected Result checkConverted(UUID uuid, Double value) {
        if (value > 0 && !VaultBridge.hasMoney(uuid, value)) {
            return Result.fail();
        }
        return successConditional((uuid1, process) -> Scheduler.current().sync().runTask(() -> {
            if (!VaultBridge.takeMoney(uuid1, value)) {
                Optional.ofNullable(Bukkit.getPlayer(uuid1)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
            }
            process.next();
        }));
    }
}
