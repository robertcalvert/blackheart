/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      (August 10th-17th 2013) 
 *      <http://7dfps.calvert.io>
 *
 *      blackheart
 *      Copyright (c) 2013 Robert Calvert <http://robert.calvert.io>
 *
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package io.flob.blackheart;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author rob
 */
public final class StateTitleHelp implements IState {

    public final Core _core;
    private final String id = "TITLE_HELP";
    private final Button button_ok;
    private final TextureUV button_ok_texture;
    private final String button_ok_text = "OK";
    private final int button_size = 70;


    public StateTitleHelp(Core core) throws Exception {
        _core = core;
        button_ok_texture = new TextureUV(_core._texture.game_atlas, 32, 93, 64, 16);
        button_ok = new Button(_core._display.width() / 2 - (button_size * 4 / 2),
                _core._display.height() - (_core._display.height() / 3.5f),
                button_size * 4, button_size, button_ok_texture, _core._sound.button_over);
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
        if (button_ok.clicked()) {
            Mouse.setCursorPosition((int) (button_ok.getX() + (button_ok.width() / 2)),
                    (int) (button_ok.getY() - button_ok.height()));
            _core.state(_core._title);
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

        texture = _core._texture.title_help;
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
        _core._font.button.render_centred((int) button_ok.getCenterX(),
                (int) button_ok.getCenterY(), button_ok_text);
        
        _core._font.copyrite.render_centred(_core._display.width() / 2,
                _core._display.height() - _core._font.copyrite.font().getHeight(),
                "Escape from prison, shoot the bad guys, rescue the prisoners and collect the gold!");
    }

    private void render_buttons() {
        _core._texture.game_atlas.bind();
        GL11.glBegin(GL11.GL_QUADS);
        button_ok.tick();
        GL11.glEnd();
    }
}
