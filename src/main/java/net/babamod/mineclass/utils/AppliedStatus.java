package net.babamod.mineclass.utils;

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

  public synchronized void setStatus(String playerName, String status) {
    appliedStatus.put(playerName, status);
  }

  public synchronized String getStatus(String playerName) {
    return appliedStatus.getOrDefault(playerName, "none");
  }

  public synchronized boolean hasAClass(String playerName) {
    return appliedStatus.getOrDefault(playerName, null) != null;
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
    return "AppliedStatus{" +
            "appliedStatus=" + appliedStatus +
            '}';
  }
}
