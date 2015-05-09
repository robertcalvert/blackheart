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

import java.nio.FloatBuffer;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class MobPistol extends IMob {

    private final int health_maximum = 15;
    private boolean attacking;
    private final int attacking_range = 3;
    private boolean shooting = false;
    private final int damage = 2;
    private int state = 0;
    private final TextureUV[] texture;
    private final int textures = 9;
    private final int textures_walking = 2;
    private final int animation_speed_walking = 150;
    private final int textures_shooting = 2;
    private final int animation_speed_shooting = 150;
    private final int animation_speed_shooting_delay = 1000;
    private final int textures_dying = 4;
    private final int animation_speed_dying = 175;
    private long animation_timer = Misc.time();
    private boolean dead = false;
    private boolean IRemove = false;
    private final int raypick_delay = 500; // improve fps
    private long raypick_timer = Misc.time();
    private boolean shoot_overide;

    public MobPistol(Level level, Vector3f _position) {
        super(level, _position);
        speed(Level.grid_size * 1f);
        size(new Vector3f(Level.grid_size / 2f,
                Level.grid_size / 1.75f,
                Level.grid_size / 2));
        collision_box().size(size());
        health(health_maximum);

        texture = new TextureUV[textures];
        // Standing
        texture[0] = new TextureUV(_level._game._core._texture.game_atlas, 0, 0, 12, 14);
        // Walking
        texture[1] = new TextureUV(_level._game._core._texture.game_atlas, 12, 0, 12, 14);
        texture[2] = new TextureUV(_level._game._core._texture.game_atlas, 24, 0, 12, 14);
        // Shooting
        texture[3] = new TextureUV(_level._game._core._texture.game_atlas, 36, 0, 12, 14);
        texture[4] = new TextureUV(_level._game._core._texture.game_atlas, 48, 0, 12, 14);
        // Dying
        texture[5] = new TextureUV(_level._game._core._texture.game_atlas, 60, 0, 12, 14);
        texture[6] = new TextureUV(_level._game._core._texture.game_atlas, 72, 0, 12, 14);
        texture[7] = new TextureUV(_level._game._core._texture.game_atlas, 84, 0, 12, 14);
        texture[8] = new TextureUV(_level._game._core._texture.game_atlas, 96, 0, 12, 14);
    }

    @Override
    public void tick() {
        yaw((360 - yaw()) + (float) player_angle());
        if (Misc.time() >= raypick_timer) {
            ray_pick();
            raypick_timer = Misc.time() + raypick_delay;
        }
        if (pain() & !shoot_overide) {
            shoot_overide = true;
        }
        if (dead) {
            if (state < textures_walking + textures_shooting) {
                state = textures_walking + textures_shooting;
            }
            if (Misc.time() >= animation_timer) {
                state += 1;
                animation_timer = Misc.time() + animation_speed_dying;
                if (state == textures_walking + textures_shooting + textures_dying + 1) {
                    IRemove = true;
                    state -= 1;
                    _level.objects_dynamic.add(new CollectablePistolAmmo(_level, camera()));
                    _level.objects_dynamic.add(new FloorBlood(_level, position()));
                }
            }
        } else {
            if (!shoot_overide & ((attacking & ray_picker().object() != _level._barry)
                    | (ray_picker().object() == _level._barry & player_distance() > attacking_range))) {
                attacking = true;
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
            } else if (attacking & ray_picker().object().equals(_level._barry)) {
                if (Misc.time() >= animation_timer) {
                    if (!shooting) {
                        state = textures_walking + textures_shooting;
                        animation_timer = Misc.time() + animation_speed_shooting;
                        shooting = true;
                        _level._barry.pain(damage);
                        _level._game._core._sound.weapon_pistol.playAsSoundEffect(1.0f, 1.0f, false,
                                _level._barry.position().getX() - position().getX(),
                                _level._barry.position().getY() - position().getY(),
                                _level._barry.position().getZ() - position().getZ());
                    } else {
                        state = textures_walking + 1;
                        animation_timer = Misc.time() + animation_speed_shooting_delay;
                        shooting = false;
                    }
                }
            } else if (ray_picker().object().equals(_level._barry)) {
                attacking = true;
            } else if (ray_picker().object() != _level._barry) {
                shoot_overide = false;
            }
            if (health() <= 0) {
                animation_timer = Misc.time();
                dead = true;
                _level._statistics.mobs_killed(1);
                _level._game._core._sound.mob_dead.playAsSoundEffect(1.0f, 1.0f, false);
            }
        }
        _render();
        super.tick();
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