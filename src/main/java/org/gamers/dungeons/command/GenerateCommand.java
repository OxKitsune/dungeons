package org.gamers.dungeons.command;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.gamers.dungeons.dungeon.DungeonGenerator;
import org.jetbrains.annotations.NotNull;

public class GenerateCommand implements CommandExecutor {
    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] args) {

        if(command.getName().equals("generate")){

            if(commandSender instanceof Player){

                Player player = (Player) commandSender;

                if(!player.isOp()){
                    player.sendMessage(ChatColor.RED + "Only OPs can run this command!");
                    return true;
                }

                // Paste the dungeon
                DungeonGenerator.getInstance().generateDungeon(player.getLocation());
                player.sendMessage(ChatColor.YELLOW + "Pasting the dungeon...");
            }
            else {
                commandSender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            }

            return true;
        }

        return false;
    }
}
