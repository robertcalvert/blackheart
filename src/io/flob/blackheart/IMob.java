/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      IMob.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public class IMob implements ICollidable {

    public final Level _level;
    private Vector3f position;
    private Vector3f size = new Vector3f(Level.grid_size / 3,
            Level.grid_size / 1.5f,
            Level.grid_size / 3);
    private final float look_speed = 0.075f;
    private final int yaw_speed = 128;
    private float yaw = 0;
    private float pitch = 0;
    private final int pitch_maximum = 45;
    private final int bias_speed = 600;
    private float bias = 0;
    private float bias_angle = 0;
    private boolean moving;
    private float walk_speed = Level.grid_size * 1.5f;
    private final CollisionBox collision_box;
    private final RayPicker ray_picker;
    private int health;
    private int health_maximum;
    private boolean pain;

    public IMob(Level level, Vector3f _position) {
        _level = level;
        position = _position;
        collision_box = new CollisionBox(this);
        collision_box.size(size);
        ray_picker = new RayPicker(this);
        position.y -= (Level.grid_size / 2) - (size.getY() / 2);
    }

    @Override
    public Vector3f position() {
        return position;
    }

    public void position(Vector3f _position) {
        position.x = _position.getX();
        position.z = _position.getZ();
        // y is not needed and should always be 0 anyway
    }

    public Vector3f camera() {
        return new Vector3f(position.getX(),
                position.getY() + ((Level.grid_size / 2) - (size.getY() / 2)),
                position.getZ());
    }

    public float yaw() {
        return yaw;
    }

    public void yaw(float amount) {
        yaw += amount;
        if (yaw > 360.0) {
            yaw -= 360;
        } else if (yaw < 0) {
            yaw += 360;
        }
    }

    public float pitch() {
        return pitch;
    }

    public void pitch(float amount) {
        pitch += (amount * _level._game.mouse_invert);
        if (pitch > pitch_maximum) {
            pitch = pitch_maximum;
        } else if (pitch < -pitch_maximum) {
            pitch = -pitch_maximum;
        }
    }

    public void speed(float value) {
        walk_speed = value;
    }

    public Vector3f size() {
        return size;
    }

    public void size(Vector3f value) {
        size = value;
    }

    @Override
    public CollisionBox collision_box() {
        return collision_box;
    }

    @Override
    public void collision(ICollidable with) {
    }

    @Override
    public boolean solid() {
        return true;
    }

    @Override
    public void tick() {
        moving = false;
        pain = false;
    }

    @Override
    public void _render() {
    }

    public void ray_pick() {
        ray_picker.tick();
    }

    public RayPicker ray_picker() {
        return ray_picker;
    }

    public void look_through() {
        yaw(Mouse.getDX() * look_speed);
        pitch(Mouse.getDY() * look_speed);
        _look_through();
    }

    public void _look_through() {
        GL11.glLoadIdentity();
        GL11.glRotatef(pitch, 1.0f, 0.0f, 0.0f);
        GL11.glRotatef(yaw, 0.0f, 1.0f, 0.0f);
        float off_set = 0F;
        if (moving) {
            off_set = -bias - (Level.grid_size / 50);
        }
        GL11.glTranslatef(-camera().getX(), -camera().getY() - off_set, -camera().getZ());
    }

    private void bias() {
        if (bias_angle >= 360.0f) {
            bias_angle = 0.0f;
        } else {
            float angle_offset = bias_speed * _level._game._core._display.delta();
            bias_angle += angle_offset;
        }
        bias = (float) Math.sin(bias_angle * Maths.pi_over_180) / 25.0f;
    }

    public void move_forward() {
        float _distance = (walk_speed * _level._game._core._display.delta());
        position.x += _distance * (float) Math.sin(Math.toRadians(yaw));
        ICollidable collision_with_x = _level.collision(this);
        if (collision_with_x != null) {
            if (collision_with_x.solid()) {
                position.x -= _distance * (float) Math.sin(Math.toRadians(yaw));
            }
            this.collision(collision_with_x);
            collision_with_x.collision(this);
        }
        position.z -= _distance * (float) Math.cos(Math.toRadians(yaw));
        ICollidable collision_with_z = _level.collision(this);
        if (collision_with_z != null) {
            if (collision_with_z.solid()) {
                position.z += _distance * (float) Math.cos(Math.toRadians(yaw));
            }
            if (collision_with_z != collision_with_x) {
                this.collision(collision_with_z);
                collision_with_z.collision(this);
            }
        }
        bias();
        moving = true;
    }

    public void move_backwards() {
        float _distance = (walk_speed * _level._game._core._display.delta());
        position.x -= _distance * (float) Math.sin(Math.toRadians(yaw));
        ICollidable collision_with_x = _level.collision(this);
        if (collision_with_x != null) {
            if (collision_with_x.solid()) {
                position.x += _distance * (float) Math.sin(Math.toRadians(yaw));
            }
            this.collision(collision_with_x);
            collision_with_x.collision(this);
        }
        position.z += _distance * (float) Math.cos(Math.toRadians(yaw));
        ICollidable collision_with_z = _level.collision(this);
        if (collision_with_z != null) {
            if (collision_with_z.solid()) {
                position.z -= _distance * (float) Math.cos(Math.toRadians(yaw));
            }
            if (collision_with_z != collision_with_x) {
                this.collision(collision_with_z);
                collision_with_z.collision(this);
            }
        }
        bias();
        moving = true;
    }

    public void move_right() {
        float _distance = (walk_speed * _level._game._core._display.delta());
        position.x -= _distance * (float) Math.sin(Math.toRadians(yaw - 90));
        ICollidable collision_with_x = _level.collision(this);
        if (collision_with_x != null) {
            if (collision_with_x.solid()) {
                position.x += _distance * (float) Math.sin(Math.toRadians(yaw - 90));
            }
            this.collision(collision_with_x);
            collision_with_x.collision(this);
        }
        position.z += _distance * (float) Math.cos(Math.toRadians(yaw - 90));
        ICollidable collision_with_z = _level.collision(this);
        if (collision_with_z != null) {
            if (collision_with_z.solid()) {
                position.z -= _distance * (float) Math.cos(Math.toRadians(yaw - 90));
            }
            if (collision_with_z != collision_with_x) {
                this.collision(collision_with_z);
                collision_with_z.collision(this);
            }
        }
    }

    public void move_left() {
        float _distance = (walk_speed * _level._game._core._display.delta());
        position.x -= _distance * (float) Math.sin(Math.toRadians(yaw + 90));
        ICollidable collision_with_x = _level.collision(this);
        if (collision_with_x != null) {
            if (collision_with_x.solid()) {
                position.x += _distance * (float) Math.sin(Math.toRadians(yaw + 90));
            }
            this.collision(collision_with_x);
            collision_with_x.collision(this);
        }
        position.z += _distance * (float) Math.cos(Math.toRadians(yaw + 90));
        ICollidable collision_with_z = _level.collision(this);
        if (collision_with_z != null) {
            if (collision_with_z.solid()) {
                position.z -= _distance * (float) Math.cos(Math.toRadians(yaw + 90));
            }
            if (collision_with_z != collision_with_x) {
                this.collision(collision_with_z);
                collision_with_z.collision(this);
            }
        }
    }

    public void turn_right() {
        yaw(yaw_speed * _level._game._core._display.delta());
    }

    public void turn_left() {
        yaw(-(yaw_speed * _level._game._core._display.delta()));
    }

    public void pitch_correct() {
        if (pitch > 0) {
            pitch -= (look_speed / 3) / _level._game._core._display.delta();
            if (pitch < 0) {
                pitch = 0;
            }
        } else if (pitch < 0) {
            pitch += (look_speed / 3) / _level._game._core._display.delta();
            if (pitch > 0) {
                pitch = 0;
            }
        }
    }

    @Override
    public double player_distance() {
        float x = _level._barry.position().getX() - position.getX();
        float y = _level._barry.position().getY() - position.getY();
        float z = _level._barry.position().getZ() - position.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    public double player_angle() {
        float x = _level._barry.position().getX() - position.getX();
        float z = _level._barry.position().getZ() - position.getZ();
        return Math.toDegrees(Math.atan2(x, -(z)));
    }

    public int health() {
        return health;
    }

    public int health_maximum() {
        return health_maximum;
    }

    public void health(int amount) {
        health = amount;
        health_maximum = amount;
    }

    public void pain(int amount) {
        if (Debug.god) {
            if (this.equals(_level._barry)) {
                return;
            }
        }
        health -= amount;
        if (health > health_maximum) {
            health = health_maximum;
        }
        pain = true;
    }
    
    public boolean pain() {
        return pain;
    }

    public void heal(int amount) {
        health += amount;
        if (health > health_maximum) {
            health = health_maximum;
        }
    }

    @Override
    public boolean IRemove() {
        return false;
    }
}