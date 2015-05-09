/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      (August 10th-17th 2013) 
 *      <http://7dfps.calvert.io>
 *
 *      blackheart
 *      Copyright (c) 2013 Robert Calvert <http://robert.calvert.io>
 *
 *      This program is free software; you can redistribute it and/or modify
 *      it under the terms of the GNU General Public License as published by
 *      the Free Software Foundation; either version 2 of the License, or
 *      (at your option) any later version.
 *
 *      This program is distributed in the hope that it will be useful,
 *      but WITHOUT ANY WARRANTY; without even the implied warranty of
 *      MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *      GNU General Public License for more details.
 *
 *      You should have received a copy of the GNU General Public License
 *      along with this program.  If not, see <http://www.gnu.org/licenses/>
 *
 */
package io.flob.blackheart;

import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class MobBarry extends IMob {

    private final int health_maximum = 100;
    public Armoury _armoury;

    public MobBarry(Level level, Vector3f _position) {
        super(level, _position);
        health(health_maximum);
        _armoury = new Armoury(this);
    }

    @Override
    public void tick() {
        look_through();
        ray_pick();
        super.tick();
    }

    public void interact() {
        if (ray_picker().object() instanceof IDoor) {
            IDoor _object = (IDoor) ray_picker().object();
            _object.open();
            return;
        }
        if (ray_picker().object() instanceof IWallSecret) {
            IWallSecret _object = (IWallSecret) ray_picker().object();
            _object.open();
            return;
        }
        if (ray_picker().object() instanceof WallLiftPanel) {
            WallLiftPanel _object = (WallLiftPanel) ray_picker().object();
            _object.use();
            return;
        }
        if (ray_picker().object() instanceof MobPrisoner) {
            MobPrisoner _object = (MobPrisoner) ray_picker().object();
            _object.follow();
        }
    }

    public int prisoners_following() {
        int prisoners_following = 0;
        for (int count = 0; count < _level.objects_dynamic.size(); count++) {
            if (_level.objects_dynamic.get(count) instanceof MobPrisoner) {
                MobPrisoner _object = (MobPrisoner) _level.objects_dynamic.get(count);
                if (_object.following()) {
                    prisoners_following += 1;
                }
            }
        }
        return prisoners_following;
    }
}
