package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtString;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.util.RndSet;

import java.util.*;

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

    private static final RndSet<Vec3d> orbLocationset = new RndSet<>(Arrays.asList(orbLocations));

    public static RndSet<Vec3d> getOrbLocations() {
        return new RndSet<>(orbLocationset);
    }


    private static final Vec3d[] powerupLocations = {
            new Vec3d(200.91, 88.00, 27.98),
            new Vec3d(206.29, 80.00, -1.91),
            new Vec3d(174.5, 100.5, 37.00),
            new Vec3d(245.5, 92, -5.5),
            new Vec3d(239.50, 111, -36.5),
            new Vec3d(209.5, 95, -34.5),
    };

    private static final RndSet<Vec3d> powerupLocationset = new RndSet<>(Arrays.asList(powerupLocations));

    public static RndSet<Vec3d> getPowerupLocations() {
        return powerupLocationset;
    }


    public static NbtCompound BOW, PICKAXE, BLOCK, HELMET, CHESTPLATE, LEGGINGS, BOOTS, AGILITY, BRIDGE, GRAVITY, EMP, ARROW_BOW, GUNK, GRAVITY_DISPLAY;
    static {
        try {
            BOW = StringNbtReader.parse("{id:'bow',Count:1b,tag:{Enchantments:[{id:'minecraft:punch',lvl:1s},{id:'minecraft:infinity',lvl:1s},{id:'minecraft:knockback',lvl:1s}],Unbreakable:1b}}");
            PICKAXE = StringNbtReader.parse("{id:'iron_pickaxe',Count:1b,tag:{CanDestroy:['#suso:primatica_breakable'],HideFlags:8,Unbreakable:1b,Enchantments:[{id:'minecraft:efficiency',lvl:10s}]}}");
            BLOCK =  StringNbtReader.parse("{id:'gray_holoblock',Count:1b,tag:{infinite:1b,CanPlaceOn:['#minecraft:all'],HideFlags:16}}");
            HELMET = StringNbtReader.parse("{id:'leather_helmet',Count:1b,tag:{display:{color:0},Unbreakable:1b,HideFlags:64}}");
            CHESTPLATE = StringNbtReader.parse("{id:'leather_chestplate',Count:1b,tag:{display:{color:0},Unbreakable:1b,HideFlags:64}}");
            LEGGINGS = StringNbtReader.parse("{id:'leather_leggings',Count:1b,tag:{display:{color:0},Unbreakable:1b,HideFlags:64}}");
            BOOTS = StringNbtReader.parse("{id:'leather_boots',Count:1b,tag:{display:{color:0},Unbreakable:1b,HideFlags:64}}");

            AGILITY = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:1,display:{Name:'{\"translate\":\"event.suso.agility\"}'}}}");
            BRIDGE = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:2,display:{Name:'{\"translate\":\"event.suso.bridge\"}'}}}");
            GRAVITY = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:3,display:{Name:'{\"translate\":\"event.suso.gravity\"}'}}}");
            EMP = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:4,display:{Name:'{\"translate\":\"event.suso.emp\"}'},CanPlaceOn:['#minecraft:all'],HideFlags:16}}");
            ARROW_BOW = StringNbtReader.parse("{id:'bow',Count:3b,tag:{Enchantments:[{id:'minecraft:punch',lvl:1s},{id:'minecraft:infinity',lvl:1s},{id:'minecraft:knockback',lvl:1s}],CustomModelData:1,Unbreakable:1b}}");
            GUNK = StringNbtReader.parse("{id:'feather',Count:1b,tag:{CustomModelData:5,display:{Name:'{\"translate\":\"event.suso.gunk\"}'}}}");

            GRAVITY_DISPLAY = StringNbtReader.parse("{id:'leather_chestplate',Count:1b,tag:{CustomModelData:1,display:{color:0}}}");
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            System.exit(0xDEADBEEF);
        }
    }


    private static final Map<Integer, String> colorCorrespondence = new HashMap<>();
    static {
        colorCorrespondence.put(7, "gray");
        colorCorrespondence.put(15, "white");
        colorCorrespondence.put(13, "pink");
        colorCorrespondence.put(5, "purple");
        colorCorrespondence.put(9, "blue");
        colorCorrespondence.put(3, "cyan");
        colorCorrespondence.put(11, "light_blue");
        colorCorrespondence.put(2, "green");
        colorCorrespondence.put(10, "lime");
        colorCorrespondence.put(14, "yellow");
        colorCorrespondence.put(6, "orange");
        colorCorrespondence.put(4, "red");
    }

    public static String getCorrespondingGunk(int colorIndex) {
        return "suso:" + colorCorrespondence.get(colorIndex) + "_gunk";
    }

    public static String getCorrespondingBlock(int colorIndex) {
        return "suso:" + colorCorrespondence.get(colorIndex) + "_holoblock";
    }

    public static String getCorrespondingEmp(int colorIndex) {
        return "suso:" + colorCorrespondence.get(colorIndex) + "_emp";
    }
}
