/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      StateGameLevelComplete.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author rob
 */
public final class StateGameLevelComplete implements IState {

    public final Core _core;
    private final String id = "GAME_LEVEL_COMPLETE";
    private final TextureUV texture_background;
    private final String button_continue_text = "CONTINUE";
    private final Button button_continue;
    private final TextureUV button_continue_texture;
    private final int button_size = 70;
    private final String title_text = "LEVEL COMPLETED";
    private final String title_text_winner = "GAME COMPLETED";

    public StateGameLevelComplete(Core core) throws Exception {
        _core = core;
        texture_background = new TextureUV(_core._texture.game_atlas, 0, 32, 1, 1);
        button_continue_texture = new TextureUV(_core._texture.game_atlas, 32, 93, 64, 16);
        button_continue = new Button(_core._display.width() / 2 - (button_size * 4 / 2),
                _core._display.height() - (_core._display.height() / 3.5f),
                button_size * 4,
                button_size,
                button_continue_texture, _core._sound.button_over);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
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
        button_continue.tick();
        if (button_continue.clicked()) {
            Mouse.setCursorPosition((int) (button_continue.getX() + (button_continue.width() / 2)),
                    (int) (button_continue.getY() - button_continue.height()));
            _core._game._level.complete();
        }
    }

    private void render_text() {
        // button
        _core._font.button.render_centred((int) button_continue.getCenterX(),
                (int) button_continue.getCenterY(), button_continue_text);
        // Title
        String text;
        if (_core._game._level.number() == _core._game.number_of_levels) {
            text = title_text_winner;
        } else {
            text = title_text;
        }
        _core._font.title.render_centred(_core._display.width() / 2,
                80, text);
        // stats
        _core._font.statistics.render_centred(_core._display.width() / 2,
                170, "Gold = " + _core._game._level._statistics.points());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                200, "Shots Fired = " + _core._game._level._statistics.shots_fired());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                230, "Enemies Killed = " + _core._game._level._statistics.mobs_killed());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                260, "Prisoners Killed = " + _core._game._level._statistics.prisoners_killed());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                290, "Prisoners Rescued = " + _core._game._level._statistics.prisoners_rescued());
        _core._font.statistics.render_centred(_core._display.width() / 2,
                320, "Secrets Found = " + _core._game._level._statistics.secrets_found());
    }
}
