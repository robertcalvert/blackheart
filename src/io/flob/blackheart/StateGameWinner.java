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

/**
 *
 * @author rob
 */
public final class StateGameWinner implements IState {

    public final Core _core;
    private final String id = "GAME_LEVEL_WINNER";
    private final TextureUV texture_background;
    private final String button_done_text = "DONE";
    private final Button button_done;
    private final TextureUV button_done_texture;
    private final int button_size = 70;
    private final String title_text = "YOU ARE A WINNER";
    private String code;

    public StateGameWinner(Core core) throws Exception {
        _core = core;
        texture_background = new TextureUV(_core._texture.game_atlas, 0, 33, 1, 1);
        button_done_texture = new TextureUV(_core._texture.game_atlas, 32, 93, 64, 16);
        button_done = new Button(_core._display.width() / 2 - (button_size * 4 / 2),
                _core._display.height() - (_core._display.height() / 3.5f),
                button_size * 4,
                button_size,
                button_done_texture, _core._sound.button_over);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
        if (code == null) {
            code = _core._game._statistics.hash();
        }
        _core._input.mouse(false);
        _core._texture.game_atlas.bind();
        _core._game._level._render();
        _core._display.mode_2D();
        render_background();
        render_button();
        GL11.glEnd();
        render_text();
        _core._display.mode_3D();

    }

    private void render_background() {
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(texture_background.getU(), texture_background.getV());
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(texture_background.getU2(), texture_background.getV());
        GL11.glVertex2f(_core._display.width(), 0);
        GL11.glTexCoord2f(texture_background.getU2(), texture_background.getV2());
        GL11.glVertex2f(_core._display.width(), _core._display.height());
        GL11.glTexCoord2f(texture_background.getU(), texture_background.getV2());
        GL11.glVertex2f(0, _core._display.height());
    }

    private void render_button() throws Exception {
        button_done.tick();
        if (button_done.clicked()) {
            Mouse.setCursorPosition((int) (button_done.getX() + (button_done.width() / 2)),
                    (int) (button_done.getY() - button_done.height()));
            _core._game = new StateGame(_core);
            _core.state(_core._title);
        }
    }

    private void render_text() {
        // button
        _core._font.button.render_centred((int) button_done.getCenterX(),
                (int) button_done.getCenterY(), button_done_text);
        // Title
        _core._font.title.render_centred(_core._display.width() / 2,
                80, title_text);
        _core._font.copyrite.render_centred(_core._display.width() / 2,
                120, "Thank you for playing!");
        // stats
        _core._font.statistics.render_centred(_core._display.width() / 2,
                170, "Total Gold = " + _core._game._statistics.points());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                200, "Total Shots Fired = " + _core._game._statistics.shots_fired());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                230, "Total Enemies Killed = " + _core._game._statistics.mobs_killed());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                260, "Total Prisoners Killed = " + _core._game._statistics.prisoners_killed());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                290, "Total Prisoners Rescued = " + _core._game._statistics.prisoners_rescued());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                320, "Total Secrets Found = " + _core._game._statistics.secrets_found());
        
        _core._font.copyrite.render_centred(_core._display.width() / 2,
                _core._display.height() - _core._font.copyrite.font().getHeight(),
                code);
    }

}
