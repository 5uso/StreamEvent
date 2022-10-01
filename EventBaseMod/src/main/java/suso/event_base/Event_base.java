package suso.event_base;

import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
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
import net.minecraft.text.LiteralTextContent;
import net.minecraft.text.MutableText;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;

public class Event_base implements ModInitializer {
    @Override
    public void onInitialize() {
        if(EvtBaseConstants.DEBUG) registerDebugCommands();
    }

    private void registerDebugCommands() {
        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument1 = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> volumeArgument1 = CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Float> pitchArgument1 = CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Boolean> loopArgument = CommandManager.argument("loop", BoolArgumentType.bool());
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("fade").then(soundArgument1.then(volumeArgument1.then(pitchArgument1.then(loopArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "volume"));
            p.writeFloat(FloatArgumentType.getFloat(context, "pitch"));
            p.writeBoolean(BoolArgumentType.getBool(context, "loop"));

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.PLAY_FADE_SOUND, p);
            return 1;
        })))))));

        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument2 = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> volumeArgument2 = CommandManager.argument("volume", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Integer> lengthArgument1 = CommandManager.argument("fadeLengthTicks", IntegerArgumentType.integer(0));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("volume").then(soundArgument2.then(volumeArgument2.then(lengthArgument1.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "volume"));
            p.writeInt(IntegerArgumentType.getInteger(context, "fadeLengthTicks"));

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.UPDATE_FADE_VOLUME, p);
            return 1;
        }))))));

        RequiredArgumentBuilder<ServerCommandSource, Identifier> soundArgument3 = CommandManager.argument("sound", IdentifierArgumentType.identifier()).suggests(SuggestionProviders.AVAILABLE_SOUNDS);
        RequiredArgumentBuilder<ServerCommandSource, Float> pitchArgument2 = CommandManager.argument("pitch", FloatArgumentType.floatArg(0.0F));
        RequiredArgumentBuilder<ServerCommandSource, Integer> lengthArgument2 = CommandManager.argument("fadeLengthTicks", IntegerArgumentType.integer(0));
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("pitch").then(soundArgument3.then(pitchArgument2.then(lengthArgument2.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "sound").toString());
            p.writeFloat(FloatArgumentType.getFloat(context, "pitch"));
            p.writeInt(IntegerArgumentType.getInteger(context, "fadeLengthTicks"));

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.UPDATE_FADE_PITCH, p);
            return 1;
        }))))));

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

            context.getSource().sendError(MutableText.of(new LiteralTextContent("Nope")).formatted(Formatting.RED));
            return 0;
        });
        nameArgument.then(values);
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("gaming").then(nameArgument)));

        RequiredArgumentBuilder<ServerCommandSource, Identifier> shaderArgument = CommandManager.argument("shader", IdentifierArgumentType.identifier());
        shaderArgument.executes(context -> {
            PacketByteBuf p = PacketByteBufs.create();

            p.writeString(IdentifierArgumentType.getIdentifier(context, "shader").toString());

            ServerPlayNetworking.send(context.getSource().getPlayer(), EvtBaseConstants.SET_POST_SHADER, p);
            return 1;
        });
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> dispatcher.register(LiteralArgumentBuilder.<ServerCommandSource>literal("epic").then(shaderArgument)));
    }
}
