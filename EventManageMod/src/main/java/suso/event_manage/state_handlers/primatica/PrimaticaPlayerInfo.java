package suso.event_manage.state_handlers.primatica;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import suso.event_manage.util.HudUtil;
import suso.event_manage.util.SoundUtil;

import java.util.UUID;

public class PrimaticaPlayerInfo {
    public boolean isUnderground = false;
    public boolean isSkyline = false;

    public boolean withinEMPPrev = false;
    public boolean withinEMPNow = false;
    public boolean withinGravityPrev = false;
    public boolean withinGravityNow = false;

    public boolean hasPowerup = false;
    public boolean isChargingBow = false;
    public boolean agilityActive = false;
    public boolean gunkBounce = false;

    public float hologramVolume = 0.0f;
    public float gravityVolume = 0.0f;

    private final UUID player;
    private final ServerWorld world;

    public PrimaticaPlayerInfo(ServerPlayerEntity player) {
        this.player = player.getUuid();
        this.world = player.getServerWorld();
    }

    public void setAgilityActive(boolean value) {
        if(value == agilityActive) return;
        agilityActive = value;
        HudUtil.setAgility((ServerPlayerEntity) world.getPlayerByUuid(this.player), value);
    }

    public void setUnderground(boolean value) {
        if(value == isUnderground) return;
        isUnderground = value;
        transition(80);
    }

    public void setSkyline(boolean value) {
        if(value == isSkyline) return;
        isSkyline = value;
        transition(80);
    }

    public void setWithinEMP(boolean value) {
        if(value == withinEMPPrev) return;
        withinEMPPrev = value;
        transition(3);
    }

    public void setWithinGravity(boolean value) {
        if(value == withinGravityPrev) return;
        withinGravityPrev = value;
        if(!value) changePitch(1.0f, 10);
    }

    public void increaseHologramVolume(float target) {
        if(target > hologramVolume) hologramVolume = target;
    }

    public void increaseGravityVolume(float target) {
        if(target > gravityVolume) gravityVolume = target;
    }

    private void transition(int ticks) {
        if(isUnderground) {
            if(withinEMPPrev) transitionUndergroundLowEQ(ticks);
            else transitionUnderground(ticks);
        }

        else if(isSkyline) {
            if(withinEMPPrev) transitionLowEQ(ticks);
            else transitionSkyline(ticks);
        }

        else {
            if(withinEMPPrev) transitionLowEQ(ticks);
            else transitionMain(ticks);
        }
    }

    private void transitionMain(int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_main"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_skyline"), 0.0f, ticks);
    }

    private void transitionLowEQ(int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_loweq"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_skyline"), 0.0f, ticks);
    }

    private void transitionUnderground(int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_underground"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_skyline"), 0.0f, ticks);
    }

    private void transitionUndergroundLowEQ(int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_undergroundloweq"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_skyline"), 0.0f, ticks);
    }

    private void transitionSkyline(int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, Identifier.of("eniah", "music.1b_skyline"), 1.0f, ticks);
    }

    public void changePitch(float pitch, int ticks) {
        ServerPlayerEntity player = (ServerPlayerEntity) world.getPlayerByUuid(this.player);
        SoundUtil.updateFadePitch(player, Identifier.of("eniah", "music.1b_main"), pitch, ticks);
        SoundUtil.updateFadePitch(player, Identifier.of("eniah", "music.1b_loweq"), pitch, ticks);
        SoundUtil.updateFadePitch(player, Identifier.of("eniah", "music.1b_underground"), pitch, ticks);
        SoundUtil.updateFadePitch(player, Identifier.of("eniah", "music.1b_undergroundloweq"), pitch, ticks);
        SoundUtil.updateFadePitch(player, Identifier.of("eniah", "music.1b_skyline"), pitch, ticks);
    }
}
