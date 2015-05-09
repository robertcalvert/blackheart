/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      ICollectable.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public class ICollectable implements ICollidable {

    public final Level _level;
    private final Vector3f position;
    private final CollisionBox collision_box;
    private final float size = Level.grid_size / 10;
    public TextureUV texture;
    private boolean IRemove = false;

    public ICollectable(Level level, Vector3f _position) {
        _level = level;
        position = _position;
        collision_box = new CollisionBox(this);
        collision_box.size(new Vector3f(size, size, size));
        position.y -= (Level.grid_size / 2) - size;
        position.x += Maths.random_float(-(Level.grid_size / 3), (Level.grid_size / 3));
        position.z += Maths.random_float(-(Level.grid_size / 3), (Level.grid_size / 3));
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
        if (with == _level._barry) {
            IRemove = true;
            pickup_sound();
        }
    }

    @Override
    public Vector3f position() {
        return position;
    }
    
    public float size() {
        return size;
    }

    @Override
    public void tick() throws Exception {
        _render();
    }

    @Override
    public void _render() {
        GL11.glEnd();

        if (texture == null) {
            texture = TextureLibrary.placeholder;
        }
        GL11.glPushMatrix();
        GL11.glTranslatef(position.getX(), position.getY(), position.getZ());
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
        GL11.glTexCoord2f(texture.getU(), texture.getV2());
        GL11.glVertex2f(-size, -size);
        GL11.glTexCoord2f(texture.getU2(), texture.getV2());
        GL11.glVertex2f(size, -size);
        GL11.glTexCoord2f(texture.getU2(), texture.getV());
        GL11.glVertex2f(size, size);
        GL11.glTexCoord2f(texture.getU(), texture.getV());
        GL11.glVertex2f(-size, size);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glBegin(GL11.GL_QUADS);
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
        return IRemove;
    }
    
    public void pickup_sound() {
        _level._game._core._sound.level_pickup.playAsSoundEffect(1.0f, 1.0f, false);
    }
}