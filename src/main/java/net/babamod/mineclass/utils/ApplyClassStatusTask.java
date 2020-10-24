package net.babamod.mineclass.utils;

import net.babamod.mineclass.classes.MineClassFactory;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class ApplyClassStatusTask extends BukkitRunnable {

  private final Player player;

  public ApplyClassStatusTask(Player player) {
    this.player = player;
  }

  @Override
  public void run() {
    MineClassFactory.getInstance()
        .getRightClass(player)
        .ifPresent(mineClass -> mineClass.reapplyEffects(player));
  }
}
