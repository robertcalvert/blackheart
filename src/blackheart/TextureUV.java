/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      TextureUV.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.newdawn.slick.opengl.Texture;

/**
 *
 * @author rob
 */
public final class TextureUV {
    
    private final float u;
    private final float u2;
    private final float v;
    private final float v2;
    private final int _width;
    private final int _height;
    
    public TextureUV(Texture texture, float x, float y, int width, int height) {
        u = x / texture.getTextureHeight();
        v = y / texture.getTextureWidth();
        u2 = (x + width) / texture.getTextureHeight();
        v2 = (y + height) / texture.getTextureWidth();
        _width = width;
        _height = height;
    }
    
    public float getU() {
        return u;
    }
    
    public float getV() {
        return v;
    }
    
    public float getU2() {
        return u2;
    }
    
    public float getV2() {
        return v2;
    }
    
    public int width() {
        return _width;
    }
    
    public int height() {
        return _height;
    }
    
}