/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      DisplayDriver.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import java.awt.image.BufferedImage;
import java.nio.ByteBuffer;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.newdawn.slick.opengl.PNGImageData;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

/**
 *
 * @author rob
 */
public final class DisplayDriver {

    private final int framerate = 60;
    private boolean sync = true;
    private final int height = 540;
    private final int width = 960;
    private int fps_count;
    private int fps;
    private long fps_previous = Misc.time();
    private float delta = 0.0f;
    private long frame_time_previous = 0;
    private Texture texture_flush;

    public DisplayDriver() throws Exception {
        init_display();
        init_GL();
        System.out.println("GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
        System.out.println("GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
        System.out.println("GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));
        System.out.println("#####################################");

        texture_flush = BufferedImageUtil.getTexture("", new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB), -1985);
    }

    private void init_display() throws Exception {
        set_icon();
        Display.setDisplayMode(new DisplayMode(width, height));
        Display.setTitle(About.title);
        Display.setVSyncEnabled(true);
        Display.create();
    }

    private void set_icon() throws Exception {
        PNGImageData icon16x16 = new PNGImageData();
        PNGImageData icon32x32 = new PNGImageData();
        PNGImageData icon128x128 = new PNGImageData();

        icon16x16.loadImage(getClass().getResourceAsStream("image/icon/16x16.png"));
        icon32x32.loadImage(getClass().getResourceAsStream("image/icon/32x32.png"));
        icon128x128.loadImage(getClass().getResourceAsStream("image/icon/128x128.png"));

        Display.setIcon(new ByteBuffer[]{icon16x16.getImageBufferData(), icon32x32.getImageBufferData(), icon128x128.getImageBufferData()});
    }

    private void init_GL() {
        GL11.glClearColor(0.1F, 0.1F, 0.1F, 1.0F);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GLU.gluPerspective(60.0f, ((float) Display.getWidth()) / ((float) Display.getHeight()), 0.1f, 40.0f);
    }

    public void mode_2D() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glOrtho(0, Display.getWidth(), Display.getHeight(), 0, 0, 1);
        GL11.glDisable(GL11.GL_DEPTH_TEST);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPushMatrix();
        GL11.glLoadIdentity();
        GL11.glDisable(GL11.GL_FOG);
    }

    public void mode_3D() {
        GL11.glEnable(GL11.GL_DEPTH_TEST);
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glPopMatrix();
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
        GL11.glPopMatrix();
        GL11.glLoadIdentity();
        GL11.glEnable(GL11.GL_FOG);
    }

    public void prepare() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update() {
        long frame_time = Misc.time();
        if (frame_time - fps_previous > 1000) {
            fps = fps_count;
            fps_count = 0;
            fps_previous += 1000;
        }
        fps_count++;
        delta = (frame_time - frame_time_previous) / 1000.0f;
        frame_time_previous = frame_time;
        Display.update();
        if (sync) {
            Display.sync(framerate);
        }
    }

    public void destroy() {
        Display.destroy();
    }

    public void flush_texture() {
        texture_flush.bind();
    }

    public int width() {
        return Display.getWidth();
    }

    public int height() {
        return Display.getHeight();
    }

    public boolean resizable() {
        return Display.isResizable();
    }

    public void resizable(boolean value) {
        Display.setResizable(value);
    }

    public int fps() {
        return fps;
    }

    public float delta() {
        return delta;
    }
}