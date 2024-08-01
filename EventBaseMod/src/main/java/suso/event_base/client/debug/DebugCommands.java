package suso.event_base.client.debug;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.command.argument.IdentifierArgumentType;
import net.minecraft.command.argument.NbtElementArgumentType;
import net.minecraft.command.suggestion.SuggestionProviders;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtFloat;
import net.minecraft.nbt.NbtInt;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import suso.event_base.EvtBaseConstants;

public class DebugCommands {
    public static void register(CommandDispatcher<ServerCommandSource> dispatcher) {
        dispatcher.register(FADE_CMD);
        dispatcher.register(VOLUME_CMD);
        dispatcher.register(PITCH_CMD);
        dispatcher.register(UNIFORM_CMD);
        dispatcher.register(SHADER_CMD);
    }

    private static final LiteralArgumentBuilder<ServerCommandSource> FADE_CMD = LiteralArgumentBuilder.literal("fade");
    static {
        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> volumeArgument = CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Float> pitchArgument = CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Boolean> loopArgument = CommandManager.argument("loop", BoolArgumentType.bool());

        loopArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "volume"));
            p.writeFloat(FloatArgumentType.getFloat(context, "pitch"));
            p.writeBoolean(BoolArgumentType.getBool(context, "loop"));
            p.writeByte(SoundCategory.RECORDS.ordinal());
            p.writeBoolean(true);

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.PLAY_FADE_SOUND, p);
            return 1;
        });

        pitchArgument.then(loopArgument);
        volumeArgument.then(pitchArgument);
        soundArgument.then(volumeArgument);
        FADE_CMD.then(soundArgument);
    }

    private static final LiteralArgumentBuilder<ServerCommandSource> VOLUME_CMD = LiteralArgumentBuilder.literal("volume");
    static {
        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> volumeArgument = CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Integer> lengthArgument = CommandManager.argument("fadeLengthTicks", IntegerArgumentType.integer(0));

        lengthArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "volume"));
            p.writeInt(IntegerArgumentType.getInteger(context, "fadeLengthTicks"));

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.UPDATE_FADE_VOLUME, p);
            return 1;
        });

        volumeArgument.then(lengthArgument);
        soundArgument.then(volumeArgument);
        VOLUME_CMD.then(soundArgument);
    }

    private static final LiteralArgumentBuilder<ServerCommandSource> PITCH_CMD = LiteralArgumentBuilder.literal("pitch");
    static {
        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> pitchArgument = CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Integer> lengthArgument = CommandManager.argument("fadeLengthTicks", IntegerArgumentType.integer(0));

        lengthArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "pitch"));
            p.writeInt(IntegerArgumentType.getInteger(context, "fadeLengthTicks"));

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.UPDATE_FADE_PITCH, p);
            return 1;
        });

        pitchArgument.then(lengthArgument);
        soundArgument.then(pitchArgument);
        PITCH_CMD.then(soundArgument);
    }

    private static final LiteralArgumentBuilder<ServerCommandSource> UNIFORM_CMD = LiteralArgumentBuilder.literal("uniform");
    static {
        RequiredArgumentBuilder<ServerCommandSource, String> nameArgument = CommandManager.argument("name", StringArgumentType.word());
        RequiredArgumentBuilder<ServerCommandSource, NbtElement> values = CommandManager.argument("values", NbtElementArgumentType.nbtElement());

        values.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();
            p.writeString(StringArgumentType.getString(context, "name"));

            NbtElement nbt = NbtElementArgumentType.getNbtElement(context, "values");

            if(nbt.getType() == NbtElement.LIST_TYPE) {
                NbtList l = (NbtList) nbt;
                if(l.getHeldType() == NbtElement.FLOAT_TYPE) {
                    p.writeBoolean(true);
                    p.writeInt(l.size());
                    for(NbtElement e : l) p.writeFloat(((NbtFloat) e).floatValue());
                    ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.SET_SHADER_UNIFORM, p);
                    return 1;
                }

                if(l.getHeldType() == NbtElement.INT_TYPE) {
                    p.writeBoolean(false);
                    p.writeInt(l.size());
                    for(NbtElement e : l) p.writeInt(((NbtInt) e).intValue());
                    ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.SET_SHADER_UNIFORM, p);
                    return 1;
                }
            }

            context.getSource().sendError(Text.literal("Nope").formatted(Formatting.RED));
            return 0;
        });

        nameArgument.then(values);
        UNIFORM_CMD.then(nameArgument);
    }

    private static final LiteralArgumentBuilder<ServerCommandSource> SHADER_CMD = LiteralArgumentBuilder.literal("shader");
    static {
        RequiredArgumentBuilder<ServerCommandSource, Identifier> shaderArgument = CommandManager.argument("shader", IdentifierArgumentType.identifier());

        shaderArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "shader").toString());

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.SET_POST_SHADER, p);
            return 1;
        });

        SHADER_CMD.then(shaderArgument);
    }
}
