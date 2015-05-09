/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      IWall.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public class IWall implements ICollidable {

    public final Level _level;
    private final Vector3f position;
    private final float size = (Level.grid_size / 2);
    private final CollisionBox collision_box;
    public TextureUV texture_front;
    public TextureUV texture_back;
    public TextureUV texture_left;
    public TextureUV texture_right;

    public IWall(Level level, Vector3f _position) {
        _level = level;
        position = _position;
        collision_box = new CollisionBox(this);
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
        if (texture_front == null) {
            texture_front = TextureLibrary.placeholder;
        }
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(texture_front.getU(), texture_front.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_front.getU2(), texture_front.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_front.getU2(), texture_front.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_front.getU(), texture_front.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), size + position.getZ());
    }

    private void render_back() {
        if (texture_back == null) {
            texture_back = TextureLibrary.placeholder;
        }
        GL11.glNormal3f(0f, 0f, -1f);
        GL11.glTexCoord2f(texture_back.getU2(), texture_back.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_back.getU2(), texture_back.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_back.getU(), texture_back.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_back.getU(), texture_back.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), -size + position.getZ());
    }

    private void render_left() {
        if (texture_left == null) {
            texture_left = TextureLibrary.placeholder;
        }
        GL11.glNormal3f(-1, 0, 0);
        GL11.glTexCoord2f(texture_left.getU(), texture_left.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_left.getU2(), texture_left.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_left.getU2(), texture_left.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_left.getU(), texture_left.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), -size + position.getZ());
    }

    private void render_right() {
        if (texture_right == null) {
            texture_right = TextureLibrary.placeholder;
        }
        GL11.glNormal3f(1, 0, 0);
        GL11.glTexCoord2f(texture_right.getU2(), texture_right.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_right.getU2(), texture_right.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(texture_right.getU(), texture_right.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(texture_right.getU(), texture_right.getV2());
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
}