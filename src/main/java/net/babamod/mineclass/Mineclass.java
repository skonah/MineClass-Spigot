package net.babamod.mineclass;

import net.babamod.mineclass.commands.CommandClass;
import net.babamod.mineclass.utils.AppliedStatus;
import net.babamod.mineclass.utils.MineClassListeners;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;

public final class Mineclass extends JavaPlugin implements Listener {

  @Override
  public void onEnable() {
    AppliedStatus.getInstance();
    new MineClassListeners(this);
    PluginCommand pluginCommand = this.getCommand("class");
    if (pluginCommand != null) {
      pluginCommand.setTabCompleter(
          (sender, command, alias, args) ->
              Arrays.asList("dwarf", "elf", "fire_dwarf", "naga", "clear", "whoami"));
      pluginCommand.setExecutor(new CommandClass());
    }
  }

  @Override
  public void onDisable() {
    // Plugin shutdown logic
  }
}
