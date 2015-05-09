/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      FloorBlood.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
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
