package suso.event_manage.state_handlers.primatica;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.StringNbtReader;
import net.minecraft.util.math.Vec3d;
import suso.event_manage.EvtBaseConstants;
import suso.event_manage.util.RndSet;

import java.util.Arrays;

public class PrimaticaInfo {
    public enum Powerups {
        AGILITY, BRIDGE, GRAVITY, EMP, ARROW, GUNK
    }


    private static final Vec3d[] orbLocations = {
            new Vec3d(-57,132,12),
            new Vec3d(-37,137,20),
            new Vec3d(-57,143,22),
            new Vec3d(-46,120,54),
            new Vec3d(-43,129,-19),
            new Vec3d(-61,121,-34),
            new Vec3d(-57,114,-13),
            new Vec3d(-34,124,12),
            new Vec3d(-51,144,-1),
            new Vec3d(-49,110,-45),
            new Vec3d(-43,104,-55),
            new Vec3d(-70,90,-21),
            new Vec3d(-46,106,-21),
            new Vec3d(-37,105,-9),
            new Vec3d(-41,109,1),
            new Vec3d(-42,108,21),
            new Vec3d(-68,89,19),
            new Vec3d(-38,105,37),
            new Vec3d(-51,108,38),
            new Vec3d(-53,95,53),
            new Vec3d(-40,78,21),
            new Vec3d(-22,128,-40),
            new Vec3d(-25,160,-50),
            new Vec3d(-28,142,-56),
            new Vec3d(-19,110,-70),
            new Vec3d(-18,99,-58),
            new Vec3d(-10,93,-67),
            new Vec3d(1,92,-53),
            new Vec3d(9,102,-52),
            new Vec3d(-24,130,0),
            new Vec3d(-25,119,43),
            new Vec3d(-23,102,52),
            new Vec3d(-33,104,64),
            new Vec3d(-33,85,56),
            new Vec3d(-23,91,70),
            new Vec3d(0,94,74),
            new Vec3d(-2,111,61),
            new Vec3d(-6,109,40),
            new Vec3d(-24,95,17),
            new Vec3d(-20,124,20),
            new Vec3d(-16,121,-24),
            new Vec3d(-3,119,-37),
            new Vec3d(16,119,-62),
            new Vec3d(16,130,-52),
            new Vec3d(-16,134,-24),
            new Vec3d(-20,91,0),
            new Vec3d(33,117,-56),
            new Vec3d(23,109,-40),
            new Vec3d(-5,111,-24),
            new Vec3d(6,122,-20),
            new Vec3d(41,88,-56),
            new Vec3d(34,91,-38),
            new Vec3d(16,84,-56),
            new Vec3d(29,134,64),
            new Vec3d(20,155,52),
            new Vec3d(-3,123,52),
            new Vec3d(22,87,69),
            new Vec3d(16,120,34),
            new Vec3d(5,111,31),
            new Vec3d(49,113,48),
            new Vec3d(59,93,44),
            new Vec3d(28,103,53),
            new Vec3d(29,137,37),
            new Vec3d(40,118,24),
            new Vec3d(57,134,16),
            new Vec3d(67,102,21),
            new Vec3d(57,101,21),
            new Vec3d(60,106,6),
            new Vec3d(57,125,-20),
            new Vec3d(59,99,-19),
            new Vec3d(54,147,-41),
            new Vec3d(40,119,-26),
            new Vec3d(46,113,-37),
            new Vec3d(54,93,-33),
            new Vec3d(18,94,-65),
            new Vec3d(18,111,-21),
            new Vec3d(57,146,-5),
            new Vec3d(39,144,-5),
            new Vec3d(29,125,-15),
            new Vec3d(17,139,-20),
            new Vec3d(36,115,1),
            new Vec3d(38,101,-9),
            new Vec3d(39,92,-21),
            new Vec3d(49,91,34),
            new Vec3d(22,101,8),
            new Vec3d(0,69,-30),
            new Vec3d(0,70,0),
            new Vec3d(0,69,30),
            new Vec3d(-19,74,26),
            new Vec3d(19,68,7),
            new Vec3d(-19,68,-19),
            new Vec3d(9,70,-15),
            new Vec3d(-43,85,39),
            new Vec3d(-75,106,0),
            new Vec3d(-57,157,5),
            new Vec3d(0,101,20),
            new Vec3d(-4,108,-15),
            new Vec3d(17,106,-6),
            new Vec3d(7,97,-9),
            new Vec3d(14,112,11),
            new Vec3d(-11,112,0),
            new Vec3d(-8,119,8),
            new Vec3d(8,119,-8),
            new Vec3d(0,119,0),
            new Vec3d(7,131,24),
            new Vec3d(-5,138,20),
            new Vec3d(-14,140,-11),
            new Vec3d(14,140,11),
            new Vec3d(0,140,0),
    };

    private static final Vec3d[] finalOrbLocations = {
            new Vec3d(0,140,0),
            new Vec3d(14,140,11),
            new Vec3d(-14,140,-11),
            new Vec3d(0,70,0),
            new Vec3d(29,137,37),
            new Vec3d(16,150,-52),
            new Vec3d(-43,129,-19),
            new Vec3d(-40,137,20),
            new Vec3d(14,117,11),
            new Vec3d(-11,112,0),
    };

    private static final RndSet<Vec3d> orbLocationSet = new RndSet<>(Arrays.asList(orbLocations));
    private static final RndSet<Vec3d> finalOrbLocationSet = new RndSet<>(Arrays.asList(finalOrbLocations));

    public static RndSet<Vec3d> getOrbLocations() {
        return new RndSet<>(orbLocationSet);
    }

    public static RndSet<Vec3d> getFinalOrbLocations() {
        return new RndSet<>(finalOrbLocationSet);
    }


    public static NbtCompound BOW, PICKAXE, BLOCK, HELMET, CHESTPLATE, LEGGINGS, BOOTS, AGILITY, BRIDGE, GRAVITY, EMP, ARROW_BOW, GUNK, GRAVITY_DISPLAY, GUNK_DISPLAY;
    static {
        try {
            BOW = StringNbtReader.parse("{id:'bow',count:1,components:{'custom_model_data':7,'enchantments':{levels:{'knockback':1,'infinity':1,'punch':1}},'unbreakable':{}}}");
            PICKAXE = StringNbtReader.parse("{id:'iron_pickaxe',count:1,components:{'unbreakable':{},'can_break':{predicates:[{blocks:'#suso:primatica_breakable'}],show_in_tooltip:false},'enchantments':{levels:{'minecraft:efficiency':10}}}}");
            BLOCK =  StringNbtReader.parse("{id:'suso:gray_holoblock',count:1,components:{'custom_data':{infinite:1b},'can_place_on':{predicates:[{blocks:'#suso:all'}],show_in_tooltip:false}}}");
            HELMET = StringNbtReader.parse("{id:'leather_helmet',count:1,components:{'dyed_color':{rgb:0,show_in_tooltip:false},'unbreakable':{}}}");
            CHESTPLATE = StringNbtReader.parse("{id:'leather_chestplate',count:1,components:{'dyed_color':{rgb:0,show_in_tooltip:false},'unbreakable':{}}}");
            LEGGINGS = StringNbtReader.parse("{id:'leather_leggings',count:1,components:{'dyed_color':{rgb:0,show_in_tooltip:false},'unbreakable':{}}}");
            BOOTS = StringNbtReader.parse("{id:'leather_boots',count:1,components:{'dyed_color':{rgb:0,show_in_tooltip:false},'unbreakable':{}}}");

            AGILITY = StringNbtReader.parse("{id:'feather',count:1,components:{'custom_model_data':1,'custom_name':'{\"translate\":\"event.suso.agility\"}'}}");
            BRIDGE = StringNbtReader.parse("{id:'feather',count:1,components:{'custom_model_data':2,'custom_name':'{\"translate\":\"event.suso.bridge\"}'}}");
            GRAVITY = StringNbtReader.parse("{id:'feather',count:1,components:{'custom_model_data':3,'custom_name':'{\"translate\":\"event.suso.gravity\"}'}}");
            EMP = StringNbtReader.parse("{id:'feather',count:1,components:{'custom_model_data':4,'custom_name':'{\"translate\":\"event.suso.emp\"}','can_place_on':{predicates:[{blocks:'#minecraft:all'}],show_in_tooltip:false}}}");
            ARROW_BOW = StringNbtReader.parse("{id:'bow',count:3,components:{'custom_model_data':1,'custom_name':'{\"translate\":\"event.suso.arrow_bow\"}','enchantments':{levels:{'knockback':1,'infinity':1,'punch':1}},'unbreakable':{}}}");
            GUNK = StringNbtReader.parse("{id:'feather',count:1,components:{'custom_model_data':5,'custom_name':'{\"translate\":\"event.suso.gunk\"}'}}");

            GRAVITY_DISPLAY = StringNbtReader.parse("{id:'leather_chestplate',count:1,components:{'custom_model_data':1,'dyed_color':{rgb:0}}}");
            GUNK_DISPLAY = StringNbtReader.parse("{id:'leather_helmet',count:1,components:{'custom_model_data':1,'dyed_color':{rgb:0}}}");
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
            System.exit(0xDEADBEEF);
        }
    }


    public static String getCorrespondingGunk(int colorIndex) {
        return "suso:" + EvtBaseConstants.getTeamColor(colorIndex) + "_gunk";
    }

    public static String getCorrespondingBlock(int colorIndex) {
        return "suso:" + EvtBaseConstants.getTeamColor(colorIndex) + "_holoblock";
    }

    public static String getCorrespondingEmp(int colorIndex) {
        return "suso:" + EvtBaseConstants.getTeamColor(colorIndex) + "_emp";
    }
}
