package com.empcraft.holoplots;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import com.empcraft.holoplots.Metrics;

import java.util.logging.Level;

public class Main extends JavaPlugin {
    public static Main THIS;
    public static IHoloUtil HOLO = null;
    private static final int BSTATS_ID = 6402;

    @Override
    public void onEnable() {
        Main.THIS = this;
        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            getLogger().log(Level.SEVERE, "ProtocolLib required. Disabling HoloPlots.");
            getLogger().log(Level.SEVERE, "https://www.spigotmc.org/resources/protocollib.1997/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("HolographicDisplays") == null) {
            getLogger().log(Level.SEVERE, "HolographicDisplays required. Disabling HoloPlots.");
            getLogger().log(Level.SEVERE, "https://dev.bukkit.org/projects/holographic-displays/files/2859237");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") == null) {
            getLogger().log(Level.SEVERE, "You can't run HoloPlots without PlotSquared. Disabling HoloPlots.");
            getLogger().log(Level.SEVERE, "https://www.spigotmc.org/resources/plotsquared.1177/");
            Bukkit.getPluginManager().disablePlugin(this);
            return;
        }
        new PacketListener();
        if (Bukkit.getPluginManager().getPlugin("PlotSquared") != null) {
            HOLO = new PSHoloUtil();
        }
        // Enable metrics
        new Metrics(this, BSTATS_ID);
    }
}
