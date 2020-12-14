package me.hsgamer.bettergui.vaultbridge;

import me.hsgamer.bettergui.api.requirement.TakableRequirement;
import me.hsgamer.bettergui.config.MessageConfig;
import me.hsgamer.bettergui.lib.core.bukkit.utils.MessageUtils;
import me.hsgamer.bettergui.lib.core.expression.ExpressionUtils;
import me.hsgamer.bettergui.lib.core.variable.VariableManager;
import me.hsgamer.bettergui.manager.PluginVariableManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class MoneyRequirement extends TakableRequirement<Double> {

    private final Map<UUID, Double> checked = new HashMap<>();

    public MoneyRequirement(String name) {
        super(name);
        PluginVariableManager.register(name, (original, uuid) -> {
            double money = getParsedValue(uuid);
            if (money > 0 && !VaultBridge.hasMoney(uuid, money)) {
                return String.valueOf(money);
            }
            return MessageConfig.HAVE_MET_REQUIREMENT_PLACEHOLDER.getValue();
        });
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
    protected void takeChecked(UUID uuid) {
        if (!VaultBridge.takeMoney(uuid, checked.remove(uuid))) {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> player.sendMessage(ChatColor.RED + "Error: the transaction couldn't be executed. Please inform the staff."));
        }
    }

    @Override
    public Double getParsedValue(UUID uuid) {
        String parsed = VariableManager.setVariables(String.valueOf(value).trim(), uuid);
        return Optional.ofNullable(ExpressionUtils.getResult(parsed)).map(BigDecimal::doubleValue).orElseGet(() -> {
            Optional.ofNullable(Bukkit.getPlayer(uuid)).ifPresent(player -> MessageUtils.sendMessage(player, MessageConfig.INVALID_NUMBER.getValue().replace("{input}", parsed)));
            return 0D;
        });
    }

    @Override
    public boolean check(UUID uuid) {
        double money = getParsedValue(uuid);
        if (money > 0 && !VaultBridge.hasMoney(uuid, money)) {
            return false;
        }
        checked.put(uuid, money);
        return true;
    }
}
