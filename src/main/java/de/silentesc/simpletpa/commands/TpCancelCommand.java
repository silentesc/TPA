package de.silentesc.simpletpa.commands;

import de.silentesc.simpletpa.Main;
import de.silentesc.simpletpa.Manager;
import de.silentesc.simpletpa.classes.Tpa;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TpCancelCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        // Manager
        Manager manager = Main.getInstance().getManager();

        // Return if sender is not a player
        if (!(commandSender instanceof Player)) {
            manager.getShortMessages().notAPlayer(commandSender);
            return true;
        }

        Player player = (Player) commandSender;
        String[] usages = {"/tpcancel [player]"};

        // Return if args length is not 1
        if (strings.length != 1) {
            manager.getShortMessages().wrongUsage(player, usages);
            return true;
        }

        // Get target and return if not exists
        Player target = Bukkit.getPlayerExact(strings[0]);
        if (target == null) {
            manager.getShortMessages().playerDoesNotExist(player);
            return true;
        }

        // Check if tpa exists
        Tpa tpa = Tpa.getTpa(player, target);
        if (tpa == null) {
            manager.getShortMessages().tpaDoesNotExist(player);
            return true;
        }

        // Remove tpa
        Tpa.getTpas().remove(tpa);

        // Send message
        manager.getShortMessages().sendSuccessMessage(player, String.format("You§c canceled§7 your pending tpa to§e%s", target.getDisplayName()));
        manager.getShortMessages().sendSuccessMessage(target, String.format("§e%s§c canceled§7 their pending tpa", player.getDisplayName()));

        return true;
    }
}
