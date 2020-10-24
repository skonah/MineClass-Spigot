package net.babamod.mineclass.utils;

import org.bukkit.entity.Player;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Objects;

public class AppliedStatus implements Serializable {
  /** Instance unique pré-initialisée */
  private static AppliedStatus INSTANCE;

  private final HashMap<String, String> appliedStatus;

  /** Constructeur privé */
  private AppliedStatus() {
    appliedStatus = new HashMap<>();
  }

  /** Point d'accès pour l'instance unique du singleton */
  public static synchronized AppliedStatus getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new AppliedStatus();
    }

    return INSTANCE;
  }

  public synchronized void setStatus(Player player, String status) {
    appliedStatus.put(player.getName(), status);
  }

  public synchronized String getStatus(Player player) {
    return appliedStatus.getOrDefault(player.getName(), "none");
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    AppliedStatus that = (AppliedStatus) o;

    return Objects.equals(appliedStatus, that.appliedStatus);
  }

  @Override
  public int hashCode() {
    return appliedStatus.hashCode();
  }

  @Override
  public String toString() {
    return "AppliedStatus{" + "appliedStatus=" + appliedStatus + '}';
  }
}
