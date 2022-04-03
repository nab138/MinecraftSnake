package tk.nabdev.minecraftsnake;

import org.bukkit.plugin.java.JavaPlugin;
import tk.nabdev.minecraftsnake.commands.SnakeCommand;

public final class MinecraftSnake extends JavaPlugin {

    public static MinecraftSnake PluginInstance;
    public ClickListener listener;
    @Override
    public void onEnable() {
        PluginInstance = this;
        // Plugin startup logic
        getCommand("snake").setExecutor(new SnakeCommand());
        listener = new ClickListener();
        getServer().getPluginManager().registerEvents(listener, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static MinecraftSnake getPluginInstance() {
        return PluginInstance;
    }
}
