package tk.nabdev.minecraftsnake;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Directional;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import tk.nabdev.minecraftsnake.commands.SnakeCommand;

import java.util.ArrayList;
import java.util.Random;

public class SnakeGame implements Listener {
    private final Player player;
    private final Location center;
    public int direction = 1;
    public Location f;
    public Location b;
    public Location l;
    public Location r;
    private boolean started = false;
    private ArrayList<SnakeBlock> snake = new ArrayList<SnakeBlock>();
    private ArrayList<SnakeBlock> snakeCpy = new ArrayList<SnakeBlock>();
    private int controls;
    private int movement;
    private final SnakeCommand command;

    public SnakeGame(Location center, Player player, SnakeCommand command){
        this.command = command;
        this.player = player;
        this.center = center.clone();
        this.center.setY(this.center.getY() -4);
    }

    public void start(){
        started = true;
        center.getBlock().setType(Material.ORANGE_CONCRETE);
        snake.add(new SnakeBlock(center.clone(), 1));
        snakeCpy.add(new SnakeBlock(center.clone(), 1));
        controls = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftSnake.getPluginInstance(), new Runnable() {
            public void run() {
                placeControls();
            }
        }, 20, 1);
        movement = Bukkit.getScheduler().scheduleSyncRepeatingTask(MinecraftSnake.getPluginInstance(), new Runnable() {
            public void run() {
                move();
            }
        }, 20, 12);
    }
    private void death(Location position){
        position.createExplosion(50, true, true);
        Bukkit.getScheduler().cancelTask(controls);
        Bukkit.getScheduler().cancelTask(movement);
        command.free();
    }
    private void move(){
        boolean eaten = false;
        for (int i = 0; i < snake.size(); i++) {
            int dir;
            if(i == 0){
                dir = direction;
            } else {
                dir = snake.get(i - 1).lastDir;
            }
            SnakeBlock block = snake.get(i);
            Block past = block.position.getBlock();
            past.setType(Material.GREEN_CONCRETE);
            past.setType(Material.AIR);
            switch(dir){
                case 1:
                    block.position.setX(block.position.getX() + 1);
                    break;
                case 2:
                    block.position.setZ(block.position.getZ() + 1);
                    break;
                case 3:
                    block.position.setZ(block.position.getZ() - 1);
                    break;
                case 4:
                    block.position.setX(block.position.getX() - 1);
                    break;
            }
            Block futureBlock = block.position.getBlock();
            snakeCpy.set(i, new SnakeBlock(block.position.clone(), dir));
            if(futureBlock.getType() == Material.RED_CONCRETE && i == 0){
                eaten = true;
                placeApple();
            } else if(futureBlock.getType() != Material.AIR && i == 0){
                death(block.position);
            }
            futureBlock.setType(i == 0 ? Material.ORANGE_CONCRETE : Material.GREEN_CONCRETE);

        }
        snake.removeAll(snake);
        for (int i = 0; i < snakeCpy.size(); i++) {
            snake.add(snakeCpy.get(i));
            if(eaten && i == snakeCpy.size() - 1){
                SnakeBlock newBody = new SnakeBlock(snakeCpy.get(i).position.clone(), snakeCpy.get(i).lastDir);
                switch(newBody.lastDir){
                    case 1:
                        newBody.position.setX(newBody.position.getX() - 1);
                        break;
                    case 2:
                        newBody.position.setZ(newBody.position.getZ() - 1);
                        break;
                    case 3:
                        newBody.position.setZ(newBody.position.getZ() + 1);
                        break;
                    case 4:
                        newBody.position.setX(newBody.position.getX() + 1);
                        break;
                }
                snake.add(newBody);
                snakeCpy.add(newBody);
                newBody.position.getBlock().setType(Material.GREEN_CONCRETE);
                break;
            }
        }
    }

    public void placeApple(){
        Location apple = center.clone();
        apple.setX(apple.getX() + rand(-10, 11));
        apple.setZ(apple.getZ() + rand(-10, 11));
        if(apple.getZ() == center.getZ() && apple.getX() == center.getX() && !started){
            placeApple();
        } else {
            apple.getBlock().setType(Material.RED_CONCRETE);
        }
    }
    private int rand(int min, int max) {
        Random random = new Random();
        return random.nextInt(max - min) + min;
    }

    public void placeControls(){
        World world = Bukkit.getWorld("world");
        if(f != null && r != null) {
            if(f.getBlock().getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
                world.getBlockAt(f).setType(Material.BARRIER);
            }
            if(b.getBlock().getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
                world.getBlockAt(b).setType(Material.BARRIER);
            }
            if(l.getBlock().getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
                world.getBlockAt(l).setType(Material.BARRIER);
            }
            if(r.getBlock().getType() == Material.MAGENTA_GLAZED_TERRACOTTA) {
                world.getBlockAt(r).setType(Material.BARRIER);
            }
        }
        f = player.getLocation().clone();
        f.setX(player.getLocation().getX() + 2);
        f.setY(player.getLocation().getY() - 1);
        b = player.getLocation().clone();
        b.setX(player.getLocation().getX() - 2);
        b.setY(player.getLocation().getY() - 1);
        l = player.getLocation().clone();
        l.setZ(player.getLocation().getZ() - 2);
        l.setY(player.getLocation().getY() - 1);
        r = player.getLocation().clone();
        r.setZ(player.getLocation().getZ() + 2);
        r.setY(player.getLocation().getY() - 1);

        Block fB = f.getBlock();
        if(fB.getType() == Material.BARRIER) {
            fB.setType(Material.MAGENTA_GLAZED_TERRACOTTA);
            BlockData fBD = fB.getBlockData();
            ((Directional) fBD).setFacing(BlockFace.WEST);
            fB.setBlockData(fBD);
        }
        Block bB = b.getBlock();
        if(bB.getType() == Material.BARRIER) {
            bB.setType(Material.MAGENTA_GLAZED_TERRACOTTA);
            BlockData bBD = bB.getBlockData();
            ((Directional) bBD).setFacing(BlockFace.EAST);
            bB.setBlockData(bBD);
        }
        Block lB = l.getBlock();
        if(lB.getType() == Material.BARRIER) {
            lB.setType(Material.MAGENTA_GLAZED_TERRACOTTA);
            BlockData lBD = lB.getBlockData();
            ((Directional) lBD).setFacing(BlockFace.SOUTH);
            lB.setBlockData(lBD);
        }
        Block rB = r.getBlock();
        if(rB.getType() == Material.BARRIER) {
            rB.setType(Material.MAGENTA_GLAZED_TERRACOTTA);
            BlockData rBD = rB.getBlockData();
            ((Directional) rBD).setFacing(BlockFace.NORTH);
            rB.setBlockData(rBD);
        }
    }
}
