/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      FontRenderer.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import java.awt.Font;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.Color;
import org.newdawn.slick.TrueTypeFont;

/**
 *
 * @author rob
 */
public final class FontRenderer {

    private final TrueTypeFont _font;
    private final Color font_colour = Color.lightGray;

    public FontRenderer(String resource, float size) throws Exception {
        Font awtFont;
        awtFont = Font.createFont(Font.TRUETYPE_FONT, getClass().getResourceAsStream(resource));
        awtFont = awtFont.deriveFont(size);
        _font = new TrueTypeFont(awtFont, true);
    }

    public TrueTypeFont font() {
        return _font;
    }

    public void render(int x, int y, Object text) {
        _font.drawString(x, y, "" + text, font_colour);
        GL11.glColor3d(1, 1, 1);
    }

    public void render_centred(int x, int y, Object text) {
        render(x - (_font.getWidth("" + text) / 2), y - (_font.getHeight("" + text) / 2), text);
    }

    public void render(int x, int y, Object text, Color _font_colour) {
        _font.drawString(x, y, "" + text, _font_colour);
        GL11.glColor3d(1, 1, 1);
    }

    public void render_centred(int x, int y, Object text, Color _font_colour) {
        render(x - (_font.getWidth("" + text) / 2), y - (_font.getHeight("" + text) / 2), text, _font_colour);
    }
}