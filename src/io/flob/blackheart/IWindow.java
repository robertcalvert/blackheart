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
public class IWindow implements ICollidable {

    public final Level _level;
    private final Vector3f position;
    private final float size = (Level.grid_size / 2);
    private final CollisionBox collision_box;
    public TextureUV texture;
    private boolean parallel = true;

    public IWindow(Level level, Vector3f _position, boolean _parallel) {
        _level = level;
        position = _position;
        collision_box = new CollisionBox(this);
        parallel = _parallel;
        if (parallel) {
            collision_box.size(new Vector3f(Level.grid_size, Level.grid_size, Level.grid_size / 4));
        } else {
            collision_box.size(new Vector3f(Level.grid_size / 4, Level.grid_size, Level.grid_size));
        }
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
        if (texture == null) {
            texture = TextureLibrary.placeholder;
        }
        if (parallel) {
            GL11.glNormal3f(0f, 0f, 1f);
            GL11.glTexCoord2f(texture.getU(), texture.getV2());
            GL11.glVertex3f(-size + position.getX(), -size + position.getY(), position.getZ());
            GL11.glTexCoord2f(texture.getU2(), texture.getV2());
            GL11.glVertex3f(size + position.getX(), -size + position.getY(), position.getZ());
            GL11.glTexCoord2f(texture.getU2(), texture.getV());
            GL11.glVertex3f(size + position.getX(), size + position.getY(), position.getZ());
            GL11.glTexCoord2f(texture.getU(), texture.getV());
            GL11.glVertex3f(-size + position.getX(), size + position.getY(), position.getZ());
        } else {
            GL11.glNormal3f(-1, 0, 0);
            GL11.glTexCoord2f(texture.getU(), texture.getV2());
            GL11.glVertex3f(position.getX(), -size + position.getY(), -size + position.getZ());
            GL11.glTexCoord2f(texture.getU2(), texture.getV2());
            GL11.glVertex3f(position.getX(), -size + position.getY(), size + position.getZ());
            GL11.glTexCoord2f(texture.getU2(), texture.getV());
            GL11.glVertex3f(position.getX(), size + position.getY(), size + position.getZ());
            GL11.glTexCoord2f(texture.getU(), texture.getV());
            GL11.glVertex3f(position.getX(), size + position.getY(), -size + position.getZ());
        }
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
}