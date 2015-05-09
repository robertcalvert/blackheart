/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      WeaponPistol.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author rob
 */
public final class WeaponPistol implements IWeapon {

    private int usages = 0;
    private final int damage = 5;
    private final float range = 20;
    private boolean inuse = false;
    private final Armoury _armoury;
    private int state = 0;
    private final TextureUV[] texture;
    private final int textures = 5;
    private final int textures_inuse = 4;
    private long animation_timer = Misc.time();
    private final int animation_speed_inuse = 100;
    private final int texture_upsize = 20;

    public WeaponPistol(Armoury armoury) {
        _armoury = armoury;
        texture = new TextureUV[textures];
        // Standard
        texture[0] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 0, 37, 12, 6);
        // Inuse
        texture[1] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 12, 34, 12, 9);
        texture[2] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 24, 31, 12, 12);
        texture[3] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 36, 30, 12, 13);
        texture[4] = new TextureUV(_armoury._barry._level._game._core._texture.game_atlas, 48, 37, 12, 6);
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
        if (!inuse & usages > 0) {
            inuse = true;
            usages(-1);
            _armoury._barry._level._statistics.shots_fired(1);
        }
    }

    @Override
    public int usages() {
        return usages;
    }

    @Override
    public void usages(int amount) {
        usages += amount;
    }

    @Override
    public void tick() {
        if (inuse) {
            if (Misc.time() >= animation_timer) {
                state += 1;
                animation_timer = Misc.time() + animation_speed_inuse;
                if (state == 2) {
                    _armoury._barry._level._game._core._sound.weapon_pistol.playAsSoundEffect(1.0f, 1.0f, false);
                    if (_armoury._barry.ray_picker().object() != null) {
                        IParticle _particle;
                        if (_armoury._barry.ray_picker().object() instanceof IMob) {
                            if (_armoury._barry.ray_picker().object().player_distance() <= range) {
                                IMob _mob = (IMob) _armoury._barry.ray_picker().object();
                                _mob.pain(damage);
                                _particle = new ParticleBlood(_armoury._barry._level, _armoury._barry.ray_picker().position());
                                _armoury._barry._level.objects_dynamic.add(_particle);
                            }
                        } else {
                            _particle = new ParticleBulletImpact(_armoury._barry._level, _armoury._barry.ray_picker().position());
                            _armoury._barry._level.objects_dynamic.add(_particle);
                        }
                    }
                }
            }
            if (state == textures_inuse + 1) {
                inuse = false;
                state = 0;
                if (usages <= 0) {
                    _armoury.set_weapon();
                }
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