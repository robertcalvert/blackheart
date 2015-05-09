/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      StateSplash.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

/**
 *
 * @author rob
 */
public final class StateSplash implements IState {

    private final String id = "SPLASH";
    private final Core _core;

    @SuppressWarnings("LeakingThisInConstructor")
    public StateSplash(Core core) throws Exception {
        _core = core;
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
        _core._display.prepare();
        _core._display.mode_2D();
        Texture texture = TextureLoader.getTexture("PNG", getClass().getResourceAsStream("image/splash/flob.png"));
        texture.bind();
        int x1 = (_core._display.width() / 2) - (texture.getTextureWidth() / 2);
        int y1 = (_core._display.height() / 2) - (texture.getTextureHeight() / 2);
        int x2 = (_core._display.width() / 2) + (texture.getTextureWidth() / 2);
        int y2 = (_core._display.height() / 2) + (texture.getTextureHeight() / 2);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glTexCoord2f(0, 0);
        GL11.glVertex2i(x1, y1);
        GL11.glTexCoord2f(1, 0);
        GL11.glVertex2i(x2, y1);
        GL11.glTexCoord2f(1, 1);
        GL11.glVertex2i(x2, y2);
        GL11.glTexCoord2f(0, 1);
        GL11.glVertex2i(x1, y2);
        GL11.glEnd();
        _core._display.mode_3D();
        _core._display.update();
    }
}
