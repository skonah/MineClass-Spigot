package net.babamod.mineclass.utils;

import net.babamod.mineclass.classes.ClassWrapper;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class ApplyClassStatusTask extends BukkitRunnable {

    private final JavaPlugin plugin;
    private final Player player;

    public ApplyClassStatusTask(JavaPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    @Override
    public void run() {
        ClassWrapper.reapplyRightClassEffects(player, false);
    }

}