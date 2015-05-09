/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      FontLibrary.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

/**
 *
 * @author rob
 */
public final class FontLibrary {

    public final FontRenderer debug;
    public final FontRenderer hud;
    public final FontRenderer button;
    public final FontRenderer copyrite;
    public final FontRenderer title;
    public final FontRenderer statistics;

    public FontLibrary() throws Exception {
        debug = new FontRenderer("font/Grand9KPixel.ttf", 8);
        hud = new FontRenderer("font/Grand9KPixel.ttf", 64);
        button = new FontRenderer("font/Grand9KPixel.ttf", 40);
        copyrite = new FontRenderer("font/Grand9KPixel.ttf", 16);
        title = new FontRenderer("font/Grand9KPixel.ttf", 48);
        statistics = new FontRenderer("font/Grand9KPixel.ttf", 24);
    }
}
