package net.babamod.mineclass.commands;

import net.babamod.mineclass.classes.*;
import net.babamod.mineclass.utils.AppliedStatus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClass implements CommandExecutor {
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if (args.length == 0) {
      sender.sendMessage("You need to use this command with one of the suggested arguments.");
      return false;
    }
    if (sender instanceof Player) {
      Player player = (Player) sender;
      if (args[0].equals("dwarf")) {
        AppliedStatus.getInstance().setElf(player.getName(), false);
        AppliedStatus.getInstance().setFireDwarf(player.getName(), false);
        AppliedStatus.getInstance().setNaga(player.getName(), false);
        AppliedStatus.getInstance().setDwarf(player.getName(), true);
        ClassWrapper.clearAllClassEffects(player);
        DwarfClass.reapplyEffects(player);
        DwarfClass.giveClassItem(player);
        return true;
      }
      if (args[0].equals("elf")) {
        AppliedStatus.getInstance().setDwarf(player.getName(), false);
        AppliedStatus.getInstance().setFireDwarf(player.getName(), false);
        AppliedStatus.getInstance().setNaga(player.getName(), false);
        AppliedStatus.getInstance().setElf(player.getName(), true);
        ClassWrapper.clearAllClassEffects(player);
        ElfClass.reapplyEffects(player);
        ElfClass.giveClassItem(player);
        return true;
      }
      if (args[0].equals("fire_dwarf")) {
        AppliedStatus.getInstance().setDwarf(player.getName(), false);
        AppliedStatus.getInstance().setElf(player.getName(), false);
        AppliedStatus.getInstance().setNaga(player.getName(), false);
        AppliedStatus.getInstance().setFireDwarf(player.getName(), true);
        ClassWrapper.clearAllClassEffects(player);
        FireDwarfClass.reapplyEffects(player);
        FireDwarfClass.giveClassItem(player);
        return true;
      }
      if (args[0].equals("naga")) {
        AppliedStatus.getInstance().setDwarf(player.getName(), false);
        AppliedStatus.getInstance().setElf(player.getName(), false);
        AppliedStatus.getInstance().setFireDwarf(player.getName(), false);
        AppliedStatus.getInstance().setNaga(player.getName(), true);
        ClassWrapper.clearAllClassEffects(player);
        NagaClass.reapplyEffects(player);
        NagaClass.giveClassItem(player);
        return true;
      }
      if (args[0].equals("clear")) {
        AppliedStatus.getInstance().setDwarf(player.getName(), false);
        AppliedStatus.getInstance().setElf(player.getName(), false);
        AppliedStatus.getInstance().setFireDwarf(player.getName(), false);
        AppliedStatus.getInstance().setNaga(player.getName(), false);
        ClassWrapper.clearAllClassEffects(player);
        ClassWrapper.removePlayerClassItem(player);
        return true;
      }
      if (args[0].equals("whoami")) {
        if (AppliedStatus.getInstance().isDwarf(player.getName())) {
          player.sendMessage("You are a dwarf.");
        }
        if (AppliedStatus.getInstance().isElf(player.getName())) {
          player.sendMessage("You are an elf.");
        }
        if (AppliedStatus.getInstance().isFireDwarf(player.getName())) {
          player.sendMessage("You are a fire dwarf.");
        }
        if (AppliedStatus.getInstance().isNaga(player.getName())) {
          player.sendMessage("You are a naga.");
        }
        return true;
      }
    }
    return false;
  }
}
