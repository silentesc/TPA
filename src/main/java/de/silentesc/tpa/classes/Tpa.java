package de.silentesc.tpa.classes;

import de.silentesc.tpa.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class Tpa {
    private static final List<Tpa> Tpas = new ArrayList<>();

    private final Player requestingPlayer;
    private final Player requestedPlayer;
    private final TpaMode mode;

    // Called when a tpa has been started
    public Tpa(Player requestingPlayer, Player requestedPlayer, TpaMode mode) {
        this.requestingPlayer = requestingPlayer;
        this.requestedPlayer = requestedPlayer;
        this.mode = mode;

        // Add tpa to list
        getTpas().add(this);

        // Expire tpa after time is up
        Bukkit.getScheduler().runTaskLater(
                Main.getInstance(), this::tpaExpired,
                Main.getInstance().getManager().getConfigUtils().getKeepAliveSeconds()
        );
    }

    // Check if there is a tpa between 2 players
    // Returns null if not exists
    @Nullable
    public static Tpa getTpa(final Player teleportingPlayer, final Player targetPlayer) {
        // Loop through tpas
        for (Tpa tpa : getTpas()) {
            // Get players and check TpaMode
            Player checkTeleportingPlayer = (tpa.mode == TpaMode.TPA) ? tpa.requestingPlayer : tpa.requestedPlayer;
            Player checkTargetPlayer = (tpa.mode == TpaMode.TPA) ? tpa.requestedPlayer : tpa.requestingPlayer;
            // Check if tpa exists
            if (teleportingPlayer.getUniqueId() == checkTeleportingPlayer.getUniqueId() && targetPlayer.getUniqueId() == checkTargetPlayer.getUniqueId()) {
                return tpa;
            }
        }
        return null;
    }

    // Called to teleport, teleporting ends a tpa
    public void performTeleport() {
        // This "deletes" the tpa
        getTpas().remove(this);

        // Assign variables and check TpaMode
        final int preTeleportSeconds = Main.getInstance().getManager().getConfigUtils().getPreTeleportSeconds();
        final Player teleportingPlayer = (mode == TpaMode.TPA) ? requestingPlayer : requestedPlayer;
        final Player targetPlayer = (mode == TpaMode.TPA) ? requestedPlayer : requestingPlayer;

        // Send messages
        Main.getInstance().getManager().getShortMessages().sendSuccessMessage(teleportingPlayer,
                String.format("You will be teleported to§e %s§7 in§a %s§7 seconds", targetPlayer.getDisplayName(), preTeleportSeconds));
        Main.getInstance().getManager().getShortMessages().sendSuccessMessage(targetPlayer,
                String.format("§e%s§7 will be teleported to you in§a %s§7 seconds", teleportingPlayer.getDisplayName(), preTeleportSeconds));

        // Wait until the preTeleportSeconds timer finished
        Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
            // Check if players are still online
            if (!teleportingPlayer.isOnline() || !targetPlayer.isOnline()) {
                System.out.println("At least 1 player is offline");
                getTpas().remove(this);
                return;
            }
            // Teleport player via LocationUtils to also play sound for all near players
            Main.getInstance().getManager().getLocationUtils().teleportPlayer(teleportingPlayer, targetPlayer.getLocation());
        }, preTeleportSeconds);
    }

    // Called when the tpa expires
    private void tpaExpired() {
        // Check if tpa still exists after the keep-alive time
        if (!getTpas().contains(this)) return;

        // "Delete" tpa
        getTpas().remove(this);

        // Send messages
        Main.getInstance().getManager().getShortMessages().sendFailMessage(requestingPlayer,
                String.format("Your request to§e %s§7 has been§c expired", requestedPlayer));
        Main.getInstance().getManager().getShortMessages().sendFailMessage(requestedPlayer,
                String.format("§e%s§7's request has been§c expired", requestingPlayer));

    }

    // Getter
    public static List<Tpa> getTpas() {
        return Tpas;
    }
}
