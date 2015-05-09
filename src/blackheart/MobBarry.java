/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      MobBarry.java
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
