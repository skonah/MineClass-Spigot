package net.babamod.mineclass.utils;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;

public class PlayerLaunchedEnderPearl {
  /** Instance unique pré-initialisée */
  private static PlayerLaunchedEnderPearl INSTANCE;

  private final HashMap<String, Boolean> playerLaunchedEnderPearl;

  /** Constructeur privé */
  private PlayerLaunchedEnderPearl() {
    playerLaunchedEnderPearl = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized PlayerLaunchedEnderPearl getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new PlayerLaunchedEnderPearl();
    }

    return INSTANCE;
  }

  public void setCooldown(Player player, Plugin plugin) {
    playerLaunchedEnderPearl.put(player.getName(), true);
    new EndCooldownTask(player).runTaskLater(plugin, 20);
  }

  public void resetCooldown(Player player) {
    playerLaunchedEnderPearl.put(player.getName(), false);
  }

  public boolean getCooldown(Player player) {
    return playerLaunchedEnderPearl.getOrDefault(player.getName(), false);
  }
}
