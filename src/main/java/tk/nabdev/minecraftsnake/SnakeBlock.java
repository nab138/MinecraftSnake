package tk.nabdev.minecraftsnake;

import org.bukkit.Location;

public class SnakeBlock {
    public int lastDir;
    public Location position;

    public SnakeBlock(Location pos, int dir){
        lastDir = dir;
        position = pos;
    }

    public int getLastDir(){
        return lastDir;
    }

    public Location getPosition() {
        return position;
    }
}
