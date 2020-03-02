package me.hsgamer.bettergui.vaultbridge;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import me.hsgamer.bettergui.BetterGUI;
import me.hsgamer.bettergui.config.impl.MessageConfig.DefaultMessage;
import me.hsgamer.bettergui.object.Icon;
import me.hsgamer.bettergui.object.IconRequirement;
import me.hsgamer.bettergui.object.IconVariable;
import me.hsgamer.bettergui.util.CommonUtils;
import me.hsgamer.bettergui.util.ExpressionUtils;
import me.hsgamer.bettergui.util.Validate;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class MoneyRequirement extends IconRequirement<Object, Double> implements IconVariable {

  private Map<UUID, Double> checked = new HashMap<>();

  public MoneyRequirement(Icon icon) {
    super(icon, true);
  }

  @Override
  public Double getParsedValue(Player player) {
    String raw = String.valueOf(value).trim();
    String parsed = icon.hasVariables(raw) ? icon.setVariables(raw, player) : raw;
    if (ExpressionUtils.isValidExpression(parsed)) {
      return ExpressionUtils.getResult(parsed).doubleValue();
    } else {
      Optional<BigDecimal> number = Validate.getNumber(parsed);
      if (number.isPresent()) {
        return number.get().doubleValue();
      } else {
        CommonUtils.sendMessage(player, BetterGUI.getInstance().getMessageConfig().get(
            DefaultMessage.INVALID_NUMBER).replace("{input}", parsed));
        return 0D;
      }
    }
  }

  @Override
  public boolean check(Player player) {
    double money = getParsedValue(player);
    if (money > 0 && !VaultBridge.hasMoney(player, money)) {
      sendFailCommand(player);
      return false;
    }
    checked.put(player.getUniqueId(), money);
    return true;
  }

  @Override
  public void take(Player player) {
    if (!VaultBridge.takeMoney(player, checked.remove(player.getUniqueId()))) {
      player.sendMessage(ChatColor.RED
          + "Error: the transaction couldn't be executed. Please inform the staff.");
    }
  }

  @Override
  public String getIdentifier() {
    return "required_money";
  }

  @Override
  public Icon getIcon() {
    return icon;
  }

  @Override
  public String getReplacement(Player player, String s) {
    double money = getParsedValue(player);
    if (money > 0 && !VaultBridge.hasMoney(player, money)) {
      return String.valueOf(money);
    }
    return BetterGUI.getInstance().getMessageConfig()
        .get(DefaultMessage.HAVE_MET_REQUIREMENT_PLACEHOLDER);
  }
}
