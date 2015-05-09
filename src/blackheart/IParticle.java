/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      IParticle.java
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
public class IParticle implements ILevelObject {
    
    
    public final Level _level;
    private final Vector3f position;
    public TextureUV texture;
    private final float size;
    private final long timer;
    private boolean IRemove = false;
    
     public IParticle(Level level, Vector3f _position, float _size, int time) {
        _level = level;
        position = _position;
        size = (Level.grid_size / _size) / 2;
        timer = Misc.time() + time;
     }
    

    @Override
    public Vector3f position() {
        return position;
    }
    
    @Override
    public void tick() throws Exception {
        _render();
        if (Misc.time() >= timer) {
            IRemove = true;
        }
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
}
