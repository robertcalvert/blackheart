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
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class WallLiftPanel implements ICollidable {

    public final Level _level;
    private final Vector3f position;
    private final float size = (Level.grid_size / 2);
    private final CollisionBox collision_box;
    private final TextureUV texture;
    private final float range = Level.grid_size * 1.5f;

    public WallLiftPanel(Level level, Vector3f _position) {
        _level = level;
        position = _position;
        collision_box = new CollisionBox(this);
        texture = new TextureUV(_level._game._core._texture.game_atlas, 112, 46, 16, 16);
    }

    @Override
    public Vector3f position() {
        return position;
    }

    @Override
    public boolean solid() {
        return true;
    }

    @Override
    public CollisionBox collision_box() {
        return collision_box;
    }

    @Override
    public void collision(ICollidable with) {
    }

    @Override
    public void tick() {
        _render();
    }

    @Override
    public void _render() {
        render_front();
        render_back();
        render_left();
        render_right();
    }

    private void render_front() {
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), size + position.getZ());
    }

    private void render_back() {
        GL11.glNormal3f(0f, 0f, -1f);
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), -size + position.getZ());
    }

    private void render_left() {
        GL11.glNormal3f(-1, 0, 0);
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), -size + position.getZ());
    }

    private void render_right() {
        GL11.glNormal3f(1, 0, 0);
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), size + position.getZ());
    }

    @Override
    public double player_distance() {
        float x = _level._barry.position().getX() - position.getX();
        float y = _level._barry.position().getY() - position.getY();
        float z = _level._barry.position().getZ() - position.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public boolean IRemove() {
        return false;
    }

    public void use() {
        if (player_distance() <= range) {
            _level._statistics.prisoners_rescued(_level._barry.prisoners_following());
            _level._game._core.state(_level._game._core._game_level_complete);
        }
    }
}