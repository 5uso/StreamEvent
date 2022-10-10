package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.util.RndSet;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class PrimaticaInfo {
    public enum Powerups {
        AGILITY, BRIDGE, GRAVITY, EMP, ARROW, GUNK
    }

    private static final Vec3d[] orbLocations = {
            new Vec3d(226.00, 125.00, 18.50),
            new Vec3d(209.5, 109.00, -5.5),
            new Vec3d(174.5, 100.5, 37.00),
            new Vec3d(245.5, 92, -5.5),
            new Vec3d(239.50, 111, -36.5),
            new Vec3d(209.5, 95, -34.5),
    };
    private static final Vec3d[] powerupLocations = {
            new Vec3d(200.91, 88.00, 27.98),
            new Vec3d(206.29, 80.00, -1.91),
            new Vec3d(174.5, 100.5, 37.00),
            new Vec3d(245.5, 92, -5.5),
            new Vec3d(239.50, 111, -36.5),
            new Vec3d(209.5, 95, -34.5),
    };

    private static final RndSet<Vec3d> orbLocationset = new RndSet<>(Arrays.asList(orbLocations));
    private static final RndSet<Vec3d> powerupLocationset = new RndSet<>(Arrays.asList(powerupLocations));

    public static RndSet<Vec3d> getOrbLocations() {
        return new RndSet<>(orbLocationset);
    }

    public static RndSet<Vec3d> getPowerupLocations() {
        return powerupLocationset;
    }

    public static NbtCompound SWORD, BOW, PICKAXE, BLOCK, HELMET, CHESTPLATE, LEGGINGS, BOOTS, AGILITY, BRIDGE, GRAVITY, EMP;
    static {
        try {
            SWORD = StringNbtReader.parse("{id:'iron_sword',Count:1b,tag:{Unbreakable:1b}}");
            BOW = StringNbtReader.parse("{id:'bow',Count:1b,tag:{Enchantments:[{id:'minecraft:punch',lvl:1s},{id:'minecraft:infinity',lvl:1s}],Unbreakable:1b}}");
            PICKAXE = StringNbtReader.parse("{id:'iron_pickaxe',Count:1b,tag:{CanDestroy:['#minecraft:concrete'],Unbreakable:1b,Enchantments:[{id:'minecraft:efficiency',lvl:10s}]}}");
            BLOCK =  StringNbtReader.parse("{id:'light_blue_concrete',Count:2b,tag:{CanPlaceOn:['#minecraft:all'],HideFlags:16}}");
            HELMET = StringNbtReader.parse("{id:'leather_helmet',Count:1b,tag:{Unbreakable:1b}}");
            CHESTPLATE = StringNbtReader.parse("{id:'leather_chestplate',Count:1b,tag:{Unbreakable:1b}}");
            LEGGINGS = StringNbtReader.parse("{id:'leather_leggings',Count:1b,tag:{Unbreakable:1b}}");
            BOOTS = StringNbtReader.parse("{id:'leather_boots',Count:1b,tag:{Unbreakable:1b}}");

            AGILITY = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:1}}");
            BRIDGE = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:2}}");
            GRAVITY = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:3}}");
            EMP = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:4,CanPlaceOn:['#minecraft:all'],HideFlags:16}}");
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            System.exit(0xDEADBEEF);
        }
    }
}
