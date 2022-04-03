package tk.nabdev.minecraftsnake;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;

public class ClickListener implements Listener {
    public SnakeGame game = null;
    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if(game == null || event == null) return;
        if(event.getHand().name() != "HAND") return;
        Block block = event.getClickedBlock();
        if(block == null) return;
        Location clicked = block.getLocation();
        if(clicked.getBlockX() == game.f.getBlockX() && clicked.getBlockZ() == game.f.getBlockZ() && game.direction != 4){
            game.direction = 1;
        } else if(clicked.getBlockX() == game.b.getBlockX() && clicked.getBlockZ() == game.b.getBlockZ() && game.direction != 1){
            game.direction = 4;
        } else if(clicked.getBlockX() == game.l.getBlockX() && clicked.getBlockZ() == game.l.getBlockZ() && game.direction != 2){
            game.direction = 3;
        } else if(clicked.getBlockX() == game.r.getBlockX() && clicked.getBlockZ() == game.r.getBlockZ() && game.direction != 3) {
            game.direction = 2;
        }
    }
}
