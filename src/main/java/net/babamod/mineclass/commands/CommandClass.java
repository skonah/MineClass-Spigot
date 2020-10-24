package net.babamod.mineclass.commands;

import net.babamod.mineclass.classes.MineClassFactory;
import net.babamod.mineclass.utils.AppliedStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage(
          "You need to use this command with one of the suggested arguments (press space then tab to see suggested arguments).");
      return false;
    }
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (MineClassFactory.getInstance().getAvailableClassCodes().contains(args[0])) {
        AppliedStatus.getInstance().setStatus(player, args[0]);
        MineClassFactory.clearAllClassEffects(player);
        MineClassFactory.getInstance().reapplyEffectsByCode(args[0], player);
        MineClassFactory.getInstance().giveItemsForClassByCode(args[0], player);
        MineClassFactory.getInstance().dropForbiddenItemsForClassByCode(args[0], player);
        return true;
      }
      if (args[0].equals("clear")) {
        AppliedStatus.getInstance().setStatus(player, null);
        MineClassFactory.clearAllClassEffects(player);
        return true;
      }
      if (args[0].equals("whoami")) {
        String classCode = AppliedStatus.getInstance().getStatus(player);
        if (classCode != null && !classCode.equals("none")) {
          player.sendMessage(String.format("You are a %s.", classCode));
        } else {
          player.sendMessage("You are a simple steve.");
        }
        return true;
      }
    }
    return false;
  }
}
