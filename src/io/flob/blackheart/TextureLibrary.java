/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      TextureLibrary.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.util.BufferedImageUtil;

/**
 *
 * @author rob
 */
public final class TextureLibrary {

    private final int filter = GL11.GL_NEAREST;
    public final Texture game_atlas;
    public final Texture title_overlay;
    public final Texture title_help;
    public final Texture title_background;
    public static TextureUV placeholder;

    public TextureLibrary() throws Exception {
        game_atlas = BufferedImageUtil.getTexture(null, get("image/game/atlas.png"), filter);
        title_overlay = BufferedImageUtil.getTexture(null, get("image/title/overlay.png"), filter);
        title_help = BufferedImageUtil.getTexture(null, get("image/title/help.png"), filter);
        title_background = BufferedImageUtil.getTexture(null, get("image/title/background.png"), filter);
        placeholder = new TextureUV(game_atlas, 0, 30, 1, 1);
    }

    private BufferedImage get(String name) throws Exception {
        BufferedImage bImage = ImageIO.read(new BufferedInputStream(getClass().getResourceAsStream(name)));
        return bImage;
    }
}
