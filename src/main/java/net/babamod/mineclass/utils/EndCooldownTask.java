package net.babamod.mineclass.utils;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class EndCooldownTask extends BukkitRunnable {

  private final Player player;

  public EndCooldownTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    PlayerLaunchedEnderPearl.getInstance().resetCooldown(player);
  }
}
