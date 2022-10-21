package suso.event_base;

import net.minecraft.util.Identifier;

public class EvtBaseConstants {
    public static final Identifier PLAY_FADE_SOUND = new Identifier("suso:play_fade_sound");
    public static final Identifier UPDATE_FADE_VOLUME = new Identifier("suso:update_fade_volume");
    public static final Identifier UPDATE_FADE_PITCH = new Identifier("suso:update_fade_pitch");

    public static final Identifier SET_SHADER_UNIFORM = new Identifier("suso:set_shader_uniform");
    public static final Identifier SET_POST_SHADER = new Identifier("suso:set_post_shader");
    public static final Identifier SET_BLOCK_COLOR = new Identifier("suso:set_block_color");

    public static final Identifier JUMP_INPUT = new Identifier("suso:jump_input");

    public static final Identifier LOGIN_CHECK = new Identifier("suso:login_check");

    public static final boolean DEBUG = false;
}
