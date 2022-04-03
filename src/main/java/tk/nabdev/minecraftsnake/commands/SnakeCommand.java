package tk.nabdev.minecraftsnake.commands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import tk.nabdev.minecraftsnake.MinecraftSnake;
import tk.nabdev.minecraftsnake.SnakeGame;


public class SnakeCommand implements CommandExecutor {
    SnakeGame game;
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if(sender instanceof Player p){
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute at " + p.getName() + " run fill ~ ~-1 ~ ~22 ~-1 ~22 dark_prismarine");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(), "execute at " + p.getName() + " run fill ~ ~3 ~ ~22 ~3 ~22 barrier");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"execute at " + p.getName() + " run fill ~ ~ ~ ~22 ~ ~ polished_blackstone_stairs[facing=south]");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"execute at " + p.getName() + " run fill ~ ~ ~ ~ ~ ~22 polished_blackstone_stairs[facing=east]");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"execute at " + p.getName() + " run fill ~22 ~ ~ ~22 ~ ~22 polished_blackstone_stairs[facing=west]");
            Bukkit.getServer().dispatchCommand(Bukkit.getServer().getConsoleSender(),"execute at " + p.getName() + " run fill ~ ~ ~22 ~22 ~ ~22 polished_blackstone_stairs");
            Location center = p.getLocation();
            center.setX(center.getX() + 11);
            center.setZ(center.getZ() + 11);
            center.setY(center.getY() + 4);
            p.teleport(center);
            game = new SnakeGame(center, p, this);
            game.placeControls();
            MinecraftSnake.getPluginInstance().listener.game = game;
            game.placeApple();

            game.start();
            return true;
        } else {
            sender.sendMessage("Only players can use this command.");
            return false;
        }
    }
    public void free(){
        game = null;
    }
}
