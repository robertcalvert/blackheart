/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      MobPrisoner.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class MobPrisoner extends IMob {

    private final int health_maximum = 15;
    private boolean following;
    private int state = 0;
    private final TextureUV[] texture;
    private final int textures = 7;
    private final int textures_walking = 2;
    private final int animation_speed_walking = 150;
    private final int textures_dying = 4;
    private final int animation_speed_dying = 175;
    private long animation_timer = Misc.time();
    private boolean dead = false;
    private boolean IRemove = false;
    private final float range = Level.grid_size * 1.5f;

    public MobPrisoner(Level level, Vector3f _position) {
        super(level, _position);
        speed(Level.grid_size * 1);
        size(new Vector3f(Level.grid_size / 2f,
                Level.grid_size / 1.75f,
                Level.grid_size / 2));
        collision_box().size(size());
        health(health_maximum);

        texture = new TextureUV[textures];
        // Standing
        texture[0] = new TextureUV(_level._game._core._texture.game_atlas, 0, 109, 12, 14);
        // Walking
        texture[1] = new TextureUV(_level._game._core._texture.game_atlas, 12, 109, 12, 14);
        texture[2] = new TextureUV(_level._game._core._texture.game_atlas, 24, 109, 12, 14);
        // Dying
        texture[3] = new TextureUV(_level._game._core._texture.game_atlas, 36, 109, 12, 14);
        texture[4] = new TextureUV(_level._game._core._texture.game_atlas, 48, 109, 12, 14);
        texture[5] = new TextureUV(_level._game._core._texture.game_atlas, 60, 109, 12, 14);
        texture[6] = new TextureUV(_level._game._core._texture.game_atlas, 72, 109, 12, 14);
    }

    @Override
    public void tick() {
        yaw((360 - yaw()) + (float) player_angle());
        if (dead) {
            if (state < textures_walking) {
                state = textures_walking;
            }
            if (Misc.time() >= animation_timer) {
                state += 1;
                animation_timer = Misc.time() + animation_speed_dying;
                if (state == textures_walking + textures_dying + 1) {
                    IRemove = true;
                    state -= 1;
                    _level.objects_dynamic.add(new FloorBlood(_level, position()));
                }
            }
        } else {
            if (following & (player_distance() > ((Level.grid_size / 4) * 3))) {
                move_forward();
                if (state > textures_walking) {
                    animation_timer = Misc.time();
                }
                if (Misc.time() >= animation_timer) {
                    state += 1;
                    animation_timer = Misc.time() + animation_speed_walking;
                    if (state > textures_walking) {
                        state = 1;
                    }
                }
            } else if (following & (player_distance() < ((Level.grid_size / 4) * 2.9))) {
                move_backwards();
                if (state > textures_walking) {
                    animation_timer = Misc.time();
                }
                if (Misc.time() >= animation_timer) {
                    state += 1;
                    animation_timer = Misc.time() + animation_speed_walking;
                    if (state > textures_walking) {
                        state = 1;
                    }
                }
            } else {
                state = 0;
            }
            if (health() <= 0) {
                animation_timer = Misc.time();
                dead = true;
                _level._statistics.prisoners_killed(1);
                _level._game._core._sound.mob_dead.playAsSoundEffect(1.0f, 1.0f, false);
            }
        }
        _render();
        super.tick();
    }

    public void follow() {
        if (!following & player_distance() <= range) {
            following = true;
            _level._game._core._sound.mob_prisoner.playAsSoundEffect(1.0f, 1.0f, false);
        }
    }

    public boolean following() {
        return following;
    }

    @Override
    public void _render() {
        GL11.glEnd();

        float size_x = size().getX() / 2f;
        float size_y = size().getY() / 2f + 0.025f; // HARD SET!
        GL11.glPushMatrix();
        GL11.glTranslatef(position().getX(), position().getY(), position().getZ());
        FloatBuffer modelview = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(GL11.GL_MODELVIEW_MATRIX, modelview);
        for (int i = 0; i < 3; i += 2) {
            for (int j = 0; j < 3; j++) {
                if (i == j) {
                    modelview.put(i * 4 + j, 1.0f);
                } else {
                    modelview.put(i * 4 + j, 0.0f);
                }
            }
        }
        GL11.glLoadMatrix(modelview);
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(texture[state].getU(), texture[state].getV2());
        GL11.glVertex2f(-size_x, -size_y);
        GL11.glTexCoord2f(texture[state].getU2(), texture[state].getV2());
        GL11.glVertex2f(size_x, -size_y);
        GL11.glTexCoord2f(texture[state].getU2(), texture[state].getV());
        GL11.glVertex2f(size_x, size_y);
        GL11.glTexCoord2f(texture[state].getU(), texture[state].getV());
        GL11.glVertex2f(-size_x, size_y);
        GL11.glEnd();
        GL11.glPopMatrix();

        GL11.glBegin(GL11.GL_QUADS);
    }
    
    @Override
    public boolean IRemove() {
        return IRemove;
    }
}