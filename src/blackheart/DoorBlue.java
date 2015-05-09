/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      DoorBlue.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class DoorBlue extends IDoor {
    
    public DoorBlue(Level level, Vector3f _position, boolean _parallel) {
        super(level, _position, _parallel);
        texture_face = new TextureUV(_level._game._core._texture.game_atlas, 80, 14, 16, 16);
        texture_side = new TextureUV(_level._game._core._texture.game_atlas, 80, 30, 16, 16);
        texture_surround = texture_side;
    }
    
}