package com.empcraft.holoplots;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.gmail.filoghost.holographicdisplays.api.VisibilityManager;
import com.intellectualcrafters.plot.PS;
import com.intellectualcrafters.plot.config.C;
import com.intellectualcrafters.plot.generator.GridPlotWorld;
import com.intellectualcrafters.plot.generator.SquarePlotWorld;
import com.intellectualcrafters.plot.object.Location;
import com.intellectualcrafters.plot.object.Plot;
import com.intellectualcrafters.plot.object.PlotId;
import com.intellectualcrafters.plot.object.PlotWorld;
import com.intellectualcrafters.plot.util.MainUtil;
import com.intellectualcrafters.plot.util.UUIDHandler;

public class PSHoloUtil implements IHoloUtil {

    public static HashMap<Plot, Hologram> holograms = new HashMap<Plot, Hologram>();
    
    public void updatePlayer(Player player, ChunkWrapper chunk) {
        String world = chunk.world;
        if (!PS.get().isPlotWorld(world)) {
            return;
        }
        
        PlotWorld plotworld = PS.get().getPlotWorld(world);
        if (!(plotworld instanceof GridPlotWorld)) {
            return;
        }
        GridPlotWorld gpw = (GridPlotWorld) plotworld;

        int bx = (chunk.x << 4) - 1;
        int bz = (chunk.y << 4) - 1;
        
        int tx = bx + 16;
        int tz = bz + 16;
        
        PlotId id = getId(gpw, bx - 16, bz - 16);
        Plot plot = MainUtil.getPlot(world, id);
        
        Location signLoc = PS.get().getPlotManager(world).getSignLoc(gpw, plot);
        
        int x = signLoc.getX();
        int z = signLoc.getZ();
        
        org.bukkit.Location loc;
        if (x > bx && x <= tx && z > bz && z <= tz) {
            loc = new org.bukkit.Location(player.getWorld(), x + 0.5, signLoc.getY() + 3, z + 0.5);
            Hologram hologram = holograms.get(plot);
            if (hologram == null) {
                hologram = HologramsAPI.createHologram(Main.THIS, loc);
                holograms.put(plot, hologram);
            }
            hologram.clearLines();
            hologram.appendTextLine(translate(plot, C.OWNER_SIGN_LINE_1.s()));
            hologram.appendTextLine(translate(plot, C.OWNER_SIGN_LINE_2.s()));
            hologram.appendTextLine(translate(plot, C.OWNER_SIGN_LINE_3.s()));
            hologram.appendTextLine(translate(plot, C.OWNER_SIGN_LINE_4.s()));
            VisibilityManager visiblityManager = hologram.getVisibilityManager();
            visiblityManager.showTo(player);
        }
    }
    
    private String translate(Plot plot, String string)  {
        String id = plot.id.toString();
        String name;
        if (plot.owner == null) {
            name = "unowned";
        }
        else {
            name = UUIDHandler.getName(plot.owner);
        }
        if (name == null) {
            name = "unknown";
        }
        return ChatColor.translateAlternateColorCodes('&', string.replaceAll("%id%", id).replaceAll("%plr%", name).replace("Claimed", plot.owner == null ? "" : "Claimed"));
    }
    
    private PlotId getId(GridPlotWorld plotworld, int x, int z) {
        final SquarePlotWorld dpw = ((SquarePlotWorld) plotworld);
        final int size = plotworld.SIZE;
        int idx;
        int idz;
        if (x < 0) {
            idx = (x/size);
            x = size + (x % size);
        }
        else {
            idx = (x/size) + 1;
            x = (x % size);
        }
        if (z < 0) {
            idz = (z/size);
            z = size + (z % size);
        }
        else {
            idz = (z/size) + 1;
            z = (z % size);
        }
        return new PlotId(idx + 1, idz + 1);
    }
    
}