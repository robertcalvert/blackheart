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

import org.lwjgl.opengl.GL11;

/**
 *
 * @author rob
 */
public final class WeaponKnife implements IWeapon {

    private int usages = -1;
    private final int damage = 3;
    private final float range = Level.grid_size / 2;
    private boolean inuse = false;
    private final Armoury _armoury;
    private int state = 0;
    private final TextureUV[] texture;
    private final int textures = 5;
    private final int textures_inuse = 4;
    private long animation_timer = Misc.time();
    private final int animation_speed_inuse = 125;
    private final int texture_upsize = 20;

    public WeaponKnife(Armoury armoury) {
        _armoury = armoury;
        texture = new TextureUV[textures];
        // Standard
        texture[0] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 0, 62, 6, 5);
        // Inuse
        texture[1] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 6, 57, 6, 10);
        texture[2] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 12, 55, 6, 12);
        texture[3] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 18, 52, 6, 15);
        texture[4] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 24, 55, 6, 12);
    }

    @Override
    public int damage() {
        return damage;
    }

    @Override
    public float range() {
        return range;
    }

    @Override
    public void use() {
        if (!inuse) {
            inuse = true;
        }
    }

    @Override
    public int usages() {
        return usages;
    }

    @Override
    public void usages(int amount) {
    }

    @Override
    public void tick() {
        if (inuse) {
            if (Misc.time() >= animation_timer) {
                state += 1;
                animation_timer = Misc.time() + animation_speed_inuse;
                if (state == 2) {
                    if (_armoury._barry.ray_picker().object() != null) {
                        IParticle _particle;
                        if (_armoury._barry.ray_picker().object() instanceof IMob) {
                            if (_armoury._barry.ray_picker().object().player_distance() <= range) {
                                IMob _mob = (IMob) _armoury._barry.ray_picker().object();
                                _mob.pain(damage);
                                _particle = new ParticleBlood(_armoury._barry._level, _armoury._barry.ray_picker().position());
                                _armoury._barry._level.objects_dynamic.add(_particle);
                            }
                        }
                    }
                }
            }
            if (state == textures_inuse + 1) {
                inuse = false;
                state = 0;
            }
        } else {
            state = 0;
        }
        render();
    }

    private void render() {
        GL11.glTexCoord2f(texture[state].getU(), texture[state].getV());
        GL11.glVertex2i(((_armoury._barry._level._game._core._display.width() / 2) - (texture[state].width() / 2 * texture_upsize)),
                _armoury._barry._level._game._core._display.height() - texture[state].height() * texture_upsize);
        GL11.glTexCoord2f(texture[state].getU2(), texture[state].getV());
        GL11.glVertex2i(((_armoury._barry._level._game._core._display.width() / 2) + (texture[state].width() / 2 * texture_upsize)),
                _armoury._barry._level._game._core._display.height() - texture[state].height() * texture_upsize);
        GL11.glTexCoord2f(texture[state].getU2(), texture[state].getV2());
        GL11.glVertex2i(((_armoury._barry._level._game._core._display.width() / 2) + (texture[state].width() / 2 * texture_upsize)),
                _armoury._barry._level._game._core._display.height());
        GL11.glTexCoord2f(texture[state].getU(), texture[state].getV2());
        GL11.glVertex2i(((_armoury._barry._level._game._core._display.width() / 2) - (texture[state].width() / 2 * texture_upsize)),
                _armoury._barry._level._game._core._display.height());
    }
}