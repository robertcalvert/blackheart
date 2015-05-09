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

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class CollectableGold extends ICollectable {

    private final TextureUV[] _texture;
    private final int textures = 5;
    private long animation_timer = Misc.time();
    private final int animation_speed = 100;
    private int state = 0;
    private final int points = 250;

    public CollectableGold(Level level, Vector3f _position) {
        super(level, _position);
        _texture = new TextureUV[textures];
        _texture[0] = new TextureUV(_level._game._core._texture.game_atlas, 0, 43, 9, 9);
        _texture[1] = new TextureUV(_level._game._core._texture.game_atlas, 9, 43, 9, 9);
        _texture[2] = new TextureUV(_level._game._core._texture.game_atlas, 18, 43, 9, 9);
        _texture[3] = new TextureUV(_level._game._core._texture.game_atlas, 27, 43, 9, 9);
        _texture[4] = new TextureUV(_level._game._core._texture.game_atlas, 36, 43, 9, 9);
    }

    @Override
    public void collision(ICollidable with) {
        super.collision(with);
        if (with.equals(_level._barry)) {
            _level._statistics.points(points);
        }
    }
    
    @Override
    public void pickup_sound() {
        _level._game._core._sound.level_gold.playAsSoundEffect(1.0f, 1.0f, false);
    }
    
    

    @Override
    public void tick() throws Exception {
        if (Misc.time() >= animation_timer) {
            state += 1;
            animation_timer = Misc.time() + animation_speed;
            if (state == textures) {
                state = 0;
            }
        }
        _render();
    }

    @Override
    public void _render() {
        GL11.glEnd();


        GL11.glPushMatrix();
        GL11.glTranslatef(position().getX(), position().getY(), position().getZ());
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    modelview.put(i * 4 + j, 1.0f);
                } else {
                    modelview.put(i * 4 + j, 0.0f);
                }
            }
        }
        GL11.glLoadMatrix(modelview);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(_texture[state].getU(), _texture[state].getV2());
        GL11.glVertex2f(-size(), -size());
        GL11.glTexCoord2f(_texture[state].getU2(), _texture[state].getV2());
        GL11.glVertex2f(size(), -size());
        GL11.glTexCoord2f(_texture[state].getU2(), _texture[state].getV());
        GL11.glVertex2f(size(), size());
        GL11.glTexCoord2f(_texture[state].getU(), _texture[state].getV());
        GL11.glVertex2f(-size(), size());
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glBegin(GL11.GL_QUADS);
    }
}