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
public final class FloorBlood implements ILevelObject {
    
    // Like an IFloor only less ICollidable :)

    private final Level _level;
    private final Vector3f position;
    private final float size = (Level.grid_size / 2);
    public TextureUV texture;

    public FloorBlood(Level level, Vector3f _position) {
        _level = level;
        position = new Vector3f(_position.getX(), -1 + _level.floorblood_offset(), _position.getZ());
        texture = new TextureUV(level._game._core._texture.game_atlas, 53, 30, 5, 5);
    }

    @Override
    public Vector3f position() {
        return position;
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
        GL11.glNormal3f(0, 1, 0);
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex3f(-size + position.getX(),
                size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex3f(-size + position.getX(),
                size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex3f(size + position.getX(),
                size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex3f(size + position.getX(),
                size + position.getY(), -size + position.getZ());
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
