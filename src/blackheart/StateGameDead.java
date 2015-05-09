/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      StateGameDead.java
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
public final class StateGameDead implements IState {

    public final Core _core;
    private final String id = "GAME_DEAD";
    private final TextureUV texture_background;
    private final String button_tryagain_text = "TRY AGAIN";
    private final Button button_tryagain;
    private final TextureUV button_tryagain_texture;
    private final int button_size = 70;
    private final String title_text = "YOU ARE DEAD!";

    public StateGameDead(Core core) throws Exception {
        _core = core;
        texture_background = new TextureUV(_core._texture.game_atlas, 0, 31, 1, 1);
        button_tryagain_texture = new TextureUV(_core._texture.game_atlas, 32, 93, 64, 16);
        button_tryagain = new Button(_core._display.width() / 2 - (button_size * 4 / 2),
                _core._display.height() - (_core._display.height() / 3.5f),
                button_size * 4,
                button_size,
                button_tryagain_texture, _core._sound.button_over);
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
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(texture_background.getU(), texture_background.getV());
        GL11.glVertex2f(0, 0);
        GL11.glTexCoord2f(texture_background.getU2(), texture_background.getV());
        GL11.glVertex2f(_core._display.width(), 0);
        GL11.glTexCoord2f(texture_background.getU2(), texture_background.getV2());
        GL11.glVertex2f(_core._display.width(), _core._display.height());
        GL11.glTexCoord2f(texture_background.getU(), texture_background.getV2());
        GL11.glVertex2f(0, _core._display.height());
        button_tryagain.tick();
        GL11.glEnd();
        _core._font.button.render_centred((int) button_tryagain.getCenterX(),
                (int) button_tryagain.getCenterY(), button_tryagain_text);
        // Title
        _core._font.title.render_centred(_core._display.width() / 2,
                80, title_text);
        _core._display.mode_3D();
        if (button_tryagain.clicked()) {
            Mouse.setCursorPosition((int) (button_tryagain.getX() + (button_tryagain.width() / 2)),
                    (int) (button_tryagain.getY() - button_tryagain.height()));
            _core._game._level.reload();
        }
    }
}
