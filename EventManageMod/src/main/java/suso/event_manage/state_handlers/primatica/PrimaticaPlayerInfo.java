package suso.event_manage.state_handlers.primatica;

import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import suso.event_manage.util.SoundUtil;

public class PrimaticaPlayerInfo {
    public boolean isUnderground = false;
    public boolean isSkyline = false;

    public boolean withinEMPPrev = false;
    public boolean withinEMPNow = false;
    public boolean withinGravityPrev = false;
    public boolean withinGravityNow = false;

    public boolean hasPowerup = false;

    private final ServerPlayerEntity player;

    public PrimaticaPlayerInfo(ServerPlayerEntity player) {
        this.player = player;
    }

    public void setUnderground(boolean value) {
        if(value != isUnderground) {
            isUnderground = value;
            transition(80);
        }
    }

    public void setSkyline(boolean value) {
        if(value != isSkyline) {
            isSkyline = value;
            transition(80);
        }
    }

    public void setWithinEMP(boolean value) {
        if(value != withinEMPPrev) {
            System.out.println(withinEMPPrev + " -> " + value);
            withinEMPPrev = value;
            transition(3);
        }
    }

    public void setWithinGravity(boolean value) {
        if(value != withinGravityPrev) {
            withinGravityPrev = value;
            if(!value) changePitch(1.0f, 10);
        }
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
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, ticks);
    }

    private void transitionLowEQ(int ticks) {
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, ticks);
    }

    private void transitionUnderground(int ticks) {
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, ticks);
    }

    private void transitionUndergroundLowEQ(int ticks) {
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 1.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 0.0f, ticks);
    }

    private void transitionSkyline(int ticks) {
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_main"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_loweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_underground"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_undergroundloweq"), 0.0f, ticks);
        SoundUtil.updateFadeVolume(player, new Identifier("eniah:music.1a_skyline"), 1.0f, ticks);
    }

    public void changePitch(float pitch, int ticks) {
        SoundUtil.updateFadePitch(player, new Identifier("eniah:music.1a_main"), pitch, ticks);
        SoundUtil.updateFadePitch(player, new Identifier("eniah:music.1a_loweq"), pitch, ticks);
        SoundUtil.updateFadePitch(player, new Identifier("eniah:music.1a_underground"), pitch, ticks);
        SoundUtil.updateFadePitch(player, new Identifier("eniah:music.1a_undergroundloweq"), pitch, ticks);
        SoundUtil.updateFadePitch(player, new Identifier("eniah:music.1a_skyline"), pitch, ticks);
    }
}
