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
public final class RayPicker implements ICollidable {

    private final float step = 0.25f;
    private final Vector3f size = new Vector3f(0.05f, 0.05f, 0.05f);
    private final IMob _mob;
    private final CollisionBox collision_box;
    private final float distance_maximum = -10.0f;
    private float distance;
    private ICollidable object;
    private Vector3f position;

    public RayPicker(IMob mob) {
        _mob = mob;
        collision_box = new CollisionBox(this);
        collision_box.size(size);
    }

    private Vector3f ray_pick(float distance) {
        float yaw_cos = (float) Math.cos(-_mob.yaw() * Maths.pi_over_180 - Math.PI);
        float yaw_sin = (float) Math.sin(-_mob.yaw() * Maths.pi_over_180 - Math.PI);
        float pitch_cos = (float) -Math.cos(-_mob.pitch() * Maths.pi_over_180);
        float pitch_sin = (float) Math.sin(-_mob.pitch() * Maths.pi_over_180);
        Vector3f direction = new Vector3f(distance * (yaw_sin * pitch_cos),
                distance * (pitch_sin * -1),
                distance * (yaw_cos * pitch_cos));
        Vector3f return_value = new Vector3f(_mob.camera().getX() + direction.getX(),
                _mob.camera().getY() + direction.getY(),
                _mob.camera().getZ() + direction.getZ());
        return return_value;
    }

    @Override
    public void tick() {
        object = this;
        for (float i = 0; i >= distance_maximum; i = i - step) {
            position = ray_pick(i);
            distance = i * -1;
            ICollidable _pick = _mob._level.collision(this);
            if (_pick != null) {
                if (_pick != _mob) {
                    collision(_pick);
                    for (float j = i; j <= 0; j = j + 0.01f) {
                        position = ray_pick(j);
                        distance = j * -1;
                        ICollidable __pick = _mob._level.collision(this);
                        if (__pick == null | __pick == _mob) {
                            j = j - 0.01f;
                            position = ray_pick(j);
                            distance = j * -1;
                            return;
                        }
                    }
                    return;
                }
            }
        }
    }

    @Override
    public void _render() {
        collision_box.render();
    }

    @Override
    public CollisionBox collision_box() {
        return collision_box;
    }

    @Override
    public void collision(ICollidable with) {
        object = with;
    }

    @Override
    public Vector3f position() {
        return position;
    }

    public ICollidable object() {
        return object;
    }

    public float distance() {
        return distance;
    }

    @Override
    public boolean solid() {
        return false;
    }

    @Override
    public double player_distance() {
        float x = _mob._level._barry.position().getX() - position.getX();
        float y = _mob._level._barry.position().getY() - position.getY();
        float z = _mob._level._barry.position().getZ() - position.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public boolean IRemove() {
        return false;
    }
}