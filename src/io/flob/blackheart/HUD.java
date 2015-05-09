/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      HUD.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.opengl.GL11;

/**
 *
 * @author rob
 */
public final class HUD {

    private final Level _level;
    private final TextureUV crosshair_texture;
    private final TextureUV info_health_texture;
    private final TextureUV info_points_texture;
    private final TextureUV info_weapon_texture;
    private final int crosshair_size = 14;
    private final int info_texture_size = 64;
    private final int info_texture_offset = 5;

    public HUD(Level level) {
        _level = level;
        crosshair_texture = new TextureUV(_level._game._core._texture.game_atlas, 1, 30, 7, 7);
        info_health_texture = new TextureUV(_level._game._core._texture.game_atlas, 10, 93, 10, 10);
        info_points_texture = new TextureUV(_level._game._core._texture.game_atlas, 0, 93, 10, 10);
        info_weapon_texture = new TextureUV(_level._game._core._texture.game_atlas, 20, 93, 10, 10);
    }

    public void tick() {
        _level._game._core._display.mode_2D();
        GL11.glBegin(GL11.GL_QUADS);
        render_crosshair();
        render_weapon();
        if (!Debug.info) {
            render_info_images();
        }
        GL11.glEnd();
        if (!Debug.info) {
            render_info_text();
        }
        _level._game._core._display.mode_3D();
    }

    private void render_info_images() {
        // Points
        GL11.glTexCoord2f(info_points_texture.getU(), info_points_texture.getV());
        GL11.glVertex2i(_level._game._core._display.width()
                - (info_texture_size + info_texture_offset), info_texture_offset);
        GL11.glTexCoord2f(info_points_texture.getU2(), info_points_texture.getV());
        GL11.glVertex2i(_level._game._core._display.width() - info_texture_offset, info_texture_offset);
        GL11.glTexCoord2f(info_points_texture.getU2(), info_points_texture.getV2());
        GL11.glVertex2i(_level._game._core._display.width() - info_texture_offset, info_texture_offset + info_texture_size);
        GL11.glTexCoord2f(info_points_texture.getU(), info_points_texture.getV2());
        GL11.glVertex2i(_level._game._core._display.width()
                - (info_texture_size + info_texture_offset), info_texture_offset + info_texture_size);

        // Weapon
        GL11.glTexCoord2f(info_weapon_texture.getU(), info_weapon_texture.getV());
        GL11.glVertex2i(_level._game._core._display.width()
                - (info_texture_size + info_texture_offset), _level._game._core._display.height() - info_texture_offset - info_texture_size);
        GL11.glTexCoord2f(info_weapon_texture.getU2(), info_weapon_texture.getV());
        GL11.glVertex2i(_level._game._core._display.width() - info_texture_offset,
                _level._game._core._display.height() - info_texture_offset - info_texture_size);
        GL11.glTexCoord2f(info_weapon_texture.getU2(), info_weapon_texture.getV2());
        GL11.glVertex2i(_level._game._core._display.width() - info_texture_offset,
                _level._game._core._display.height() - info_texture_offset);
        GL11.glTexCoord2f(info_weapon_texture.getU(), info_weapon_texture.getV2());
        GL11.glVertex2i(_level._game._core._display.width()
                - (info_texture_size + info_texture_offset), _level._game._core._display.height() - info_texture_offset);

        // Health
        GL11.glTexCoord2f(info_health_texture.getU(), info_health_texture.getV());
        GL11.glVertex2i(info_texture_offset, _level._game._core._display.height() - info_texture_offset - info_texture_size);
        GL11.glTexCoord2f(info_health_texture.getU2(), info_health_texture.getV());
        GL11.glVertex2i(info_texture_offset + info_texture_size,
                _level._game._core._display.height() - info_texture_offset - info_texture_size);
        GL11.glTexCoord2f(info_health_texture.getU2(), info_health_texture.getV2());
        GL11.glVertex2i(info_texture_offset + info_texture_size,
                _level._game._core._display.height() - info_texture_offset);
        GL11.glTexCoord2f(info_health_texture.getU(), info_health_texture.getV2());
        GL11.glVertex2i(info_texture_offset, _level._game._core._display.height() - info_texture_offset);
    }

    private void render_info_text() {
        // TODO the font seems to render offset down by 20px ????
        int int_offset = 20;
        // Health
        _level._game._core._font.hud.render(info_texture_offset + info_texture_size + info_texture_offset,
                _level._game._core._display.height() - _level._game._core._font.hud.font().getHeight() - info_texture_offset + int_offset,
                _level._barry.health());
        // Weapon
        String weapon_usages;
        if (_level._barry._armoury.weapon().usages() < 0) {
            weapon_usages = "0";
        } else {
            weapon_usages = "" + _level._barry._armoury.weapon().usages();
        }
        _level._game._core._font.hud.render(
                _level._game._core._display.width()
                - (info_texture_offset + _level._game._core._font.hud.font().getWidth(weapon_usages) + info_texture_size + info_texture_offset),
                _level._game._core._display.height() - _level._game._core._font.hud.font().getHeight() - info_texture_offset + int_offset,
                weapon_usages);
        // Points
        String points = "" + _level._statistics.points();
        _level._game._core._font.hud.render(
                _level._game._core._display.width()
                - (info_texture_offset + _level._game._core._font.hud.font().getWidth(points) + info_texture_size + info_texture_offset),
                info_texture_offset - int_offset,
                points);
    }

    private void render_crosshair() {
        GL11.glTexCoord2f(crosshair_texture.getU(), crosshair_texture.getV());
        GL11.glVertex2i(((_level._game._core._display.width() / 2) - crosshair_size / 2), ((_level._game._core._display.height() / 2) - crosshair_size / 2));
        GL11.glTexCoord2f(crosshair_texture.getU2(), crosshair_texture.getV());
        GL11.glVertex2i(((_level._game._core._display.width() / 2) + crosshair_size / 2), ((_level._game._core._display.height() / 2) - crosshair_size / 2));
        GL11.glTexCoord2f(crosshair_texture.getU2(), crosshair_texture.getV2());
        GL11.glVertex2i(((_level._game._core._display.width() / 2) + crosshair_size / 2), ((_level._game._core._display.height() / 2) + crosshair_size / 2));
        GL11.glTexCoord2f(crosshair_texture.getU(), crosshair_texture.getV2());
        GL11.glVertex2i(((_level._game._core._display.width() / 2) - crosshair_size / 2), ((_level._game._core._display.height() / 2) + crosshair_size / 2));
    }

    private void render_weapon() {
        _level._barry._armoury.weapon().tick();
    }
}