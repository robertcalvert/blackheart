/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      Button.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.openal.Audio;

/**
 *
 * @author rob
 */
public final class Button {

    private final Vector2f position;
    private final Vector2f size;
    private final TextureUV _texture;
    private boolean mouse_down = false;
    private boolean clicked = false;
    private boolean active = true;
    private boolean selected = true;
    private Audio _sound_over;

    public Button(float x, float y, int width, int height, TextureUV texture) {
        position = new Vector2f(x, y);
        size = new Vector2f(width, height);
        _texture = texture;
    }

    public Button(float x, float y, int width, int height, TextureUV texture, Audio sound_over) {
        this(x, y, width, height, texture);
        _sound_over = sound_over;
    }

    public void tick() {
        clicked = false;
        if (selected()) {
            if (!Mouse.isButtonDown(0)) {
                if (mouse_down) {
                    clicked = true;
                }
                mouse_down = false;
            } else {
                mouse_down = true;
            }
        }
        render();
    }

    private void render() {
        if (!active) {
            GL11.glColor3d(0.25f, 0.25f, 0.25f);
        } else if (selected()) {
            GL11.glColor3d(0.75f, 0.75f, 0.75f);
        }
        GL11.glTexCoord2f(_texture.getU(), _texture.getV());
        GL11.glVertex2f(position.getX(), position.getY());
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV());
        GL11.glVertex2f(position.getX() + (size.getX()), position.getY());
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV2());
        GL11.glVertex2f(position.getX() + (size.getX()), position.getY() + (size.getY()));
        GL11.glTexCoord2f(_texture.getU(), _texture.getV2());
        GL11.glVertex2f(position.getX(), position.getY() + (size.getY()));
        GL11.glColor3d(1, 1, 1);
    }

    public boolean clicked() {
        return clicked;
    }

    private boolean selected() {
        if (Mouse.getX() > position.getX()
                & Mouse.getX() < position.getX() + size.getX()) {
            if (Display.getHeight() - Mouse.getY() > position.getY()
                    & Display.getHeight() - Mouse.getY() < position.getY() + size.getY()) {
                if (!selected & active) {
                    if (_sound_over != null) {
                        _sound_over.playAsSoundEffect(1.0f, 1.0f, false);
                    }
                }
                selected = true;
                return true;
            }
        }
        selected = false;
        return false;
    }

    public float getX() {
        return position.getX();
    }

    public float getY() {
        return position.getY();
    }

    public float getCenterX() {
        return position.getX() + width() / 2;
    }

    public float getCenterY() {
        return position.getY() + height() / 2;
    }

    public float width() {
        return size.getX();
    }

    public float height() {
        return size.getY();
    }

    public void active(boolean value) {
        active = value;
    }
}