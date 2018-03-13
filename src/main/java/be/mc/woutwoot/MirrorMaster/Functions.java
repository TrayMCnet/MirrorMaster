package be.mc.woutwoot.MirrorMaster;

import com.intellectualcrafters.plot.config.C;
import com.intellectualcrafters.plot.config.Settings;
import com.intellectualcrafters.plot.flag.Flags;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotPlayer;
import com.intellectualcrafters.plot.util.Permissions;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class Functions {

    static int LookDirection() {
        float yaw = UsersManager.user.player.getLocation().getYaw();

        while (yaw <= 0.0F)
            yaw += 360.0F;
        if ((yaw < 45.0F) || (yaw > 315.0F))
            return 0;
        if ((yaw > 45.0F) && (yaw < 135.0F))
            return 1;
        if ((yaw > 135.0F) && (yaw < 225.0F))
            return 2;
        if ((yaw > 225.0F) && (yaw < 315.0F))
            return 3;
        return 4;
    }

    static boolean Up() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.UP;
    }

    static boolean Down() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.DOWN;
    }

    static boolean East() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.EAST;
    }

    static boolean West() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.WEST;
    }

    static boolean North() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.NORTH;
    }

    static boolean South() {
        return Variables.touchingBlock.getFace(Variables.currentBlock) == BlockFace.SOUTH;
    }

    static void PlaceBlock(int xDif, int yDif, int zDif) {
        Player pl = UsersManager.user.player;
        Location l = new Location(pl.getWorld(), UsersManager.user.mirrorPoint.getX() + xDif, yDif, UsersManager.user.mirrorPoint.getZ() + zDif);
        if (!p2CheckPlace(l, pl)) return;
        UsersManager.user.player.getWorld().getBlockAt(l).setType(Variables.materialCopy);
        UsersManager.user.player.getWorld().getBlockAt(l).setData(Variables.dataCopy);
    }

    static void PlaceBlock(int xDif, int yDif, int zDif, byte data) {
        Player pl = UsersManager.user.player;
        Location l = new Location(pl.getWorld(), UsersManager.user.mirrorPoint.getX() + xDif, yDif, UsersManager.user.mirrorPoint.getZ() + zDif);
        if (!p2CheckPlace(l, pl)) return;
        UsersManager.user.player.getWorld().getBlockAt(l).setType(Variables.materialCopy);
        UsersManager.user.player.getWorld().getBlockAt(l).setData(data);
    }

    static void RemoveBlock(int xDif, int yDif, int zDif) {
        Player pl = UsersManager.user.player;
        Location l = new Location(pl.getWorld(), UsersManager.user.mirrorPoint.getX() + xDif, yDif, UsersManager.user.mirrorPoint.getZ() + zDif);
        if (!p2CheckRemove(l, pl)) return;
        UsersManager.user.player.getWorld().getBlockAt(l).setType(Material.AIR);
    }

    static void RemoveBlock(int xDif, int yDif, int zDif, boolean normal) {
        Player pl = UsersManager.user.player;
        Location l = new Location(pl.getWorld(), UsersManager.user.mirrorPoint.getX() + xDif, yDif, UsersManager.user.mirrorPoint.getZ() + zDif);
        if (p2CheckRemove(l, pl)) return;
        UsersManager.user.player.getWorld().getBlockAt(l).breakNaturally();
    }

    private static boolean p2CheckRemove(Location l, Player pl) {
        if (MirrorMaster.P2()) {
            Plot p = MirrorMaster.api.getPlot(l);
            PlotPlayer pp = PlotPlayer.wrap(pl);
            if (p != null) {
                if(!p.hasOwner())
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_DESTROY_UNOWNED);
                if(Settings.Done.RESTRICT_BUILDING && p.getFlags().containsKey(Flags.DONE))
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_DESTROY_OTHER);
                if(!p.isAdded(pp.getUUID()))
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_DESTROY_OTHER);
            } else {
                return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_DESTROY_ROAD);
            }
        }
        return true;
    }

    private static boolean p2CheckPlace(Location l, Player pl) {
        if (MirrorMaster.P2()) {
            Plot p = MirrorMaster.api.getPlot(l);
            PlotPlayer pp = PlotPlayer.wrap(pl);
            if (p != null) {
                if(!p.hasOwner())
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_BUILD_UNOWNED);
                if(Settings.Done.RESTRICT_BUILDING && p.getFlags().containsKey(Flags.DONE))
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_BUILD_OTHER);
                if(!p.isAdded(pp.getUUID()))
                    return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_BUILD_OTHER);
            } else {
                return Permissions.hasPermission(pp, C.PERMISSION_ADMIN_BUILD_ROAD);
            }
        }
        return true;
    }

    static boolean CheckBlockMaterialLists(ArrayList<Material> list) {
        for (Material material : list) {
            if (material == Variables.currentBlock.getType())
                return true;
        }
        return false;
    }
}