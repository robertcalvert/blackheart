/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      CollectableHealth.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class CollectableHealth extends ICollectable {

    private final int value = 25;

    public CollectableHealth(Level level, Vector3f _position) {
        super(level, _position);
        texture = new TextureUV(_level._game._core._texture.game_atlas, 63, 43, 9, 9);
    }

    @Override
    public void collision(ICollidable with) {
        if (with.equals(_level._barry)) {
            if (_level._barry.health() < _level._barry.health_maximum()) {
                super.collision(with);
                _level._barry.heal(value);
            }
        }
    }
}
