package com.doctordark.util.bossbar;

import net.minecraft.server.v1_7_R4.EntityEnderDragon;
import net.minecraft.server.v1_7_R4.EntityLiving;
import net.minecraft.server.v1_7_R4.World;
import org.bukkit.Location;

public class DragonBar extends BossBar {

    public DragonBar(Location location, String title) {
        super(location, title);
    }

    public DragonBar(Location location, String title, int percent) {
        super(location, title, percent);
    }

    @Override
    int getMaxHealth() {
        return 200;
    }

    @Override
    EntityLiving constructBossEntity(World world) {
        return new EntityEnderDragon(world);
    }
}
