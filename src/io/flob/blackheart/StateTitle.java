/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      StateTitle.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.Sys;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author rob
 */
public final class StateTitle implements IState {

    public final Core _core;
    private final String id = "TITLE";
    private final int button_size = 70;
    private final Button button_mute;
    private final Button button_help;
    private final Button button_anaglyph;
    private final Button button_facebook;
    private final Button button_play;
    private final Button button_mouse_invert;
    private final TextureUV button_mute_texture;
    private final TextureUV button_help_texture;
    private final TextureUV button_anaglyph_texture;
    private final TextureUV button_facebook_texture;
    private final TextureUV button_mouse_invert_texture;
    private final TextureUV button_play_texture;
    private final String button_play_text = "PLAY";
    private final String button_play_continue_text = "CONTINUE";

    public StateTitle(Core core) throws Exception {
        _core = core;
        button_mute_texture = new TextureUV(_core._texture.game_atlas, 96, 14, 16, 16);
        button_help_texture = new TextureUV(_core._texture.game_atlas, 96, 46, 16, 16);
        button_anaglyph_texture = new TextureUV(_core._texture.game_atlas, 96, 30, 16, 16);
        button_facebook_texture = new TextureUV(_core._texture.game_atlas, 96, 62, 16, 16);
        button_play_texture = new TextureUV(_core._texture.game_atlas, 32, 93, 64, 16);
        button_mouse_invert_texture = new TextureUV(_core._texture.game_atlas, 96, 93, 16, 16);

        button_mute = new Button(0, 0, button_size, button_size, button_mute_texture, _core._sound.button_over);
        button_mouse_invert = new Button(0, button_mute.height(), button_size, button_size,
                button_mouse_invert_texture, _core._sound.button_over);
        button_anaglyph = new Button(0, button_mouse_invert.height() + button_mouse_invert.height(),
                button_size, button_size, button_anaglyph_texture, _core._sound.button_over);
        button_help = new Button(0, button_anaglyph.getY() + button_anaglyph.height(),
                button_size, button_size, button_help_texture, _core._sound.button_over);
        button_facebook = new Button(0, _core._display.height() - button_size, button_size, button_size,
                button_facebook_texture, _core._sound.button_over);
        button_play = new Button(_core._display.width() / 2 - (button_size * 4 / 2),
                _core._display.height() - (_core._display.height() / 3.5f),
                button_size * 4, button_size, button_play_texture, _core._sound.button_over);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
        _core._input.mouse(false);
        _core._display.mode_2D();
        render_background();
        render_buttons();
        render_text();
        _core._display.mode_3D();
        if (button_mute.clicked()) {
            _core._sound.mute(!_core._sound.mute());
        } else if (button_mouse_invert.clicked()) {
            if (_core._game.mouse_invert == 1) {
                _core._game.mouse_invert = -1;
            } else {
                _core._game.mouse_invert = 1;
            }
        } else if (button_anaglyph.clicked()) {
            _core._game.anaglyph = !_core._game.anaglyph;
        } else if (button_help.clicked()) {
            _core.state(_core._title_help);
        } else if (button_play.clicked()) {
            Mouse.setCursorPosition((int) (button_play.getX() + (button_play.width() / 2)),
                    (int) (button_play.getY() - button_play.height()));
            _core.state(_core._game);
        } else if (button_facebook.clicked()) {
            Sys.openURL("http://www.facebook.com/robert.calvert");
        }
    }

    private void render_background() {
        Texture texture = _core._texture.title_background;
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(texture.getTextureWidth(), 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(texture.getTextureWidth(), texture.getTextureHeight());
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(0, texture.getTextureHeight());
        GL11.glEnd();

        // Due to a limit in the font size the title_overlay has to be an image
        texture = _core._texture.title_overlay;
        texture.bind();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(0, 0);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(texture.getTextureWidth(), 0);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(texture.getTextureWidth(), texture.getTextureHeight());
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(0, texture.getTextureHeight());
        GL11.glEnd();
    }

    private void render_text() {
        String play_text;
        if (_core._game.dirty) {
            play_text = button_play_continue_text;
        } else {
            play_text = button_play_text;
        }
        _core._font.button.render_centred((int) button_play.getCenterX(),
                (int) button_play.getCenterY(), play_text);

        _core._font.copyrite.render(_core._display.width() - 5 - _core._font.copyrite.font().getWidth(About.copyrite),
                _core._display.height() - 5 - _core._font.copyrite.font().getHeight(),
                About.copyrite);
        String version_text = "Version " + About.version;
        _core._font.copyrite.render(_core._display.width() - 5 - _core._font.copyrite.font().getWidth(version_text),
                0,
                version_text);
    }

    private void render_buttons() {
        _core._texture.game_atlas.bind();
        GL11.glBegin(GL11.GL_QUADS);
        button_mute.active(_core._sound.mute());
        button_mute.tick();
        button_help.tick();
        button_anaglyph.active(_core._game.anaglyph);
        button_anaglyph.tick();
        button_facebook.tick();
        button_mouse_invert.active(_core._game.mouse_invert == 1);
        button_mouse_invert.tick();
        button_play.tick();
        GL11.glEnd();
    }
}
