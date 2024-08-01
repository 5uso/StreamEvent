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
import suso.event_base.custom.network.payloads.*;

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
            ServerPlayNetworking.send(context.getSource().getPlayer(), new PlayFadeSoundPayload(
                    IdentifierArgumentType.getIdentifier(context, "sound"),
                    FloatArgumentType.getFloat(context, "volume"),
                    FloatArgumentType.getFloat(context, "pitch"),
                    BoolArgumentType.getBool(context, "loop"),
                    SoundCategory.RECORDS,
                    true
            ));
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
            ServerPlayNetworking.send(context.getSource().getPlayer(), new UpdateFadeVolumePayload(
                    IdentifierArgumentType.getIdentifier(context, "sound"),
                    FloatArgumentType.getFloat(context, "volume"),
                    IntegerArgumentType.getInteger(context, "fadeLengthTicks")
            ));
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
            ServerPlayNetworking.send(context.getSource().getPlayer(), new UpdateFadePitchPayload(
                    IdentifierArgumentType.getIdentifier(context, "sound"),
                    FloatArgumentType.getFloat(context, "pitch"),
                    IntegerArgumentType.getInteger(context, "fadeLengthTicks")
            ));
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
                    float[] floats = new float[l.size()];
                    for(int i = 0; i < l.size(); i++) floats[i] = l.getFloat(i);
                    ServerPlayNetworking.send(context.getSource().getPlayer(), new SetShaderUniformPayload(
                            StringArgumentType.getString(context, "name"),
                            floats
                    ));
                    return 1;
                }

                if(l.getHeldType() == NbtElement.INT_TYPE) {
                    ServerPlayNetworking.send(context.getSource().getPlayer(), new SetShaderUniformPayload(
                            StringArgumentType.getString(context, "name"),
                            l.stream().mapToInt(nbtElement -> ((NbtInt) nbtElement).intValue()).toArray()
                    ));
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
            ServerPlayNetworking.send(context.getSource().getPlayer(), new SetPostShaderPayload(
                    IdentifierArgumentType.getIdentifier(context, "shader")
            ));
            return 1;
        });

        SHADER_CMD.then(shaderArgument);
    }
}
