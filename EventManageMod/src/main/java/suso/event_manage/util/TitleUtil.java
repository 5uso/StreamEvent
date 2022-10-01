package suso.event_manage.util;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.network.Packet;
import net.minecraft.network.packet.s2c.play.OverlayMessageS2CPacket;
import net.minecraft.network.packet.s2c.play.SubtitleS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleFadeS2CPacket;
import net.minecraft.network.packet.s2c.play.TitleS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;

import java.util.function.Function;

public class TitleUtil {
    public static void sendTimes(ServerPlayerEntity player, int fadeIn, int stay, int fadeOut) {
        player.networkHandler.sendPacket(new TitleFadeS2CPacket(fadeIn, stay, fadeOut));
    }

    public static void sendTitle(ServerPlayerEntity player, Text title) {
        sendGenericTitle(player, title, TitleS2CPacket::new);
    }

    public static void sendSubtitle(ServerPlayerEntity player, Text title) {
        sendGenericTitle(player, title, SubtitleS2CPacket::new);
    }

    public static void sendActionbar(ServerPlayerEntity player, Text title) {
        sendGenericTitle(player, title, OverlayMessageS2CPacket::new);
    }

    private static void sendGenericTitle(ServerPlayerEntity player, Text title, Function<Text, Packet<?>> constructor) {
        try {
            player.networkHandler.sendPacket(constructor.apply(Texts.parse(player.server.getCommandSource(), title, player, 0)));
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}
