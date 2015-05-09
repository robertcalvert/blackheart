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

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author rob
 */
public final class StateSplash implements IState {

    private final String id = "SPLASH";
    private final Core _core;

    @SuppressWarnings("LeakingThisInConstructor")
    public StateSplash(Core core) throws Exception {
        _core = core;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
        _core._display.prepare();
        _core._display.mode_2D();
        Texture texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("image/splash/flob.png"));
        texture.bind();
        int x1 = (_core._display.width() / 2) - (texture.getTextureWidth() / 2);
        int y1 = (_core._display.height() / 2) - (texture.getTextureHeight() / 2);
        int x2 = (_core._display.width() / 2) + (texture.getTextureWidth() / 2);
        int y2 = (_core._display.height() / 2) + (texture.getTextureHeight() / 2);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(x1, y1);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(x2, y1);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(x2, y2);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(x1, y2);
        GL11.glEnd();
        _core._display.mode_3D();
        _core._display.update();
    }
}
