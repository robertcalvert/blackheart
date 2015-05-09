/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      CollisionBox.java
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
public final class CollisionBox {

    private ICollidable _object;
    private Vector3f _size = new Vector3f(Level.grid_size, Level.grid_size, Level.grid_size);

    public CollisionBox(ICollidable object) {
        _object = object;

    }
    
    public void size(Vector3f size) {
        _size = size;
    }

    public boolean intersects(CollisionBox that) {
        if (that.position_minimum().getX() < this.position_maximum().getX()
                && that.position_maximum().getX() > this.position_minimum().getX()) {
            if (that.position_minimum().getY() < this.position_maximum().getY()
                    && that.position_maximum().getY() > this.position_minimum().getY()) {
                if (that.position_minimum().getZ() < this.position_maximum().getZ()
                        && that.position_maximum().getZ() > this.position_minimum().getZ()) {
                    return true;
                }
            }
        }
        return false;
    }
    
    public Vector3f position_minimum() {
        return new Vector3f(_object.position().getX() - (_size.getX() / 2),
                _object.position().getY() - (_size.getY() / 2),
                _object.position().getZ() - (_size.getZ() / 2));
    }

    public Vector3f position_maximum() {
        return new Vector3f(_object.position().getX() + (_size.getX() / 2),
                _object.position().getY() + (_size.getY() / 2),
                _object.position().getZ() + (_size.getZ() / 2));
    }

    public void render() {
        GL11.glColor3d(1, 0, 1);
        GL11.glBegin(GL11.GL_LINE_STRIP);
        GL11.glVertex3f(position_minimum().getX(), position_minimum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_minimum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_maximum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_maximum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_minimum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_minimum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_minimum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_maximum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_maximum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_minimum().getY(), position_maximum().getZ());
        GL11.glEnd();
        GL11.glBegin(GL11.GL_LINES);
        GL11.glVertex3f(position_maximum().getX(), position_minimum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_minimum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_maximum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_maximum().getX(), position_maximum().getY(), position_maximum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_maximum().getY(), position_minimum().getZ());
        GL11.glVertex3f(position_minimum().getX(), position_maximum().getY(), position_maximum().getZ());
        GL11.glEnd();
        GL11.glColor3d(1, 1, 1);
    }
}