/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      IDoor.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public class IDoor implements ICollidable {

    public final Level _level;
    private final Vector3f position_start;
    private Vector3f position;
    private final float size = (Level.grid_size / 2);
    private final CollisionBox collision_box;
    public TextureUV texture_face;
    public TextureUV texture_side;
    public TextureUV texture_surround;
    private boolean parallel = true;
    private boolean opening = false;
    private final float animation_speed = 0.5f;
    private final float range = Level.grid_size * 1.5f;

    public IDoor(Level level, Vector3f _position, boolean _parallel) {
        _level = level;
        position = _position;
        position_start = new Vector3f(_position.getX(), _position.getY(), _position.getZ());
        collision_box = new CollisionBox(this);
        parallel = _parallel;
    }

    @Override
    public Vector3f position() {
        return position;
    }

    @Override
    public boolean solid() {
        return true;
    }

    @Override
    public CollisionBox collision_box() {
        return collision_box;
    }

    @Override
    public void collision(ICollidable with) {
    }

    @Override
    public void tick() {
        if (opening) {
            if (parallel) {
                position.x -= _level._game._core._display.delta() * animation_speed;
            } else {
                position.z += _level._game._core._display.delta() * animation_speed;
            }
        }
        if (parallel) {
            if (position.getX() <= position_start.getX() - Level.grid_size + Level.texture_bleed_offset) {
                position.x = position_start.getX() - Level.grid_size + Level.texture_bleed_offset;
                opening = false;
            }
        } else {
            if (position.getZ() >= position_start.getZ() + Level.grid_size - Level.texture_bleed_offset) {
                position.z = position_start.getZ() + Level.grid_size - Level.texture_bleed_offset;
                opening = false;
            }
        }
        _render();
    }

    @Override
    public void _render() {
        if (texture_face == null) {
            texture_face = TextureLibrary.placeholder;
        }
        if (texture_side == null) {
            texture_side = TextureLibrary.placeholder;
        }
        if (texture_surround == null) {
            texture_surround = TextureLibrary.placeholder;
        }
        render_front();
        render_back();
        render_left();
        render_right();
        render_surround();
    }

    public void open() {
        if (!opening & player_distance() <= range & valid()) {
            opening = true;
            _level._game._core._sound.level_door.playAsSoundEffect(1.0f, 1.0f, false);
        }
    }

    public boolean valid() {
        return true;
    }

    private void render_front() {
        TextureUV _texture;
        float offset = -Level.texture_bleed_offset;
        if (parallel) {
            _texture = texture_face;
        } else {
            _texture = texture_side;
        }
        GL11.glNormal3f(0f, 0f, 1f);
        GL11.glTexCoord2f(_texture.getU(), _texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU(), _texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), size + position.getZ() + offset);
    }

    private void render_back() {
        TextureUV _texture;
        float offset;
        if (parallel) {
            _texture = texture_face;
            offset = Level.texture_bleed_offset;
        } else {
            _texture = texture_side;
            offset = -Level.texture_bleed_offset;
        }
        GL11.glNormal3f(0f, 0f, -1f);
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV2());
        GL11.glVertex3f(-size + position.getX(), -size + position.getY(), -size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV());
        GL11.glVertex3f(-size + position.getX(), size + position.getY(), -size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU(), _texture.getV());
        GL11.glVertex3f(size + position.getX(), size + position.getY(), -size + position.getZ() + offset);
        GL11.glTexCoord2f(_texture.getU(), _texture.getV2());
        GL11.glVertex3f(size + position.getX(), -size + position.getY(), -size + position.getZ() + offset);
    }

    private void render_left() {
        TextureUV _texture;
        float offset = Level.texture_bleed_offset;
        if (parallel) {
            _texture = texture_side;
        } else {
            _texture = texture_face;
        }
        GL11.glNormal3f(-1, 0, 0);
        GL11.glTexCoord2f(_texture.getU(), _texture.getV2());
        GL11.glVertex3f(-size + position.getX() + offset, -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV2());
        GL11.glVertex3f(-size + position.getX() + offset, -size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV());
        GL11.glVertex3f(-size + position.getX() + offset, size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(_texture.getU(), _texture.getV());
        GL11.glVertex3f(-size + position.getX() + offset, size + position.getY(), -size + position.getZ());
    }

    private void render_right() {
        TextureUV _texture;
        float offset;
        if (parallel) {
            _texture = texture_side;
            offset = -Level.texture_bleed_offset;
        } else {
            _texture = texture_face;
            offset = Level.texture_bleed_offset;
        }
        GL11.glNormal3f(1, 0, 0);
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV2());
        GL11.glVertex3f(size + position.getX() - offset, -size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(_texture.getU2(), _texture.getV());
        GL11.glVertex3f(size + position.getX() - offset, size + position.getY(), -size + position.getZ());
        GL11.glTexCoord2f(_texture.getU(), _texture.getV());
        GL11.glVertex3f(size + position.getX() - offset, size + position.getY(), size + position.getZ());
        GL11.glTexCoord2f(_texture.getU(), _texture.getV2());
        GL11.glVertex3f(size + position.getX() - offset, -size + position.getY(), size + position.getZ());
    }

    private void render_surround() {
        // Bottom
        GL11.glNormal3f(0, -1, 0);
        GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV());
        GL11.glVertex3f(-size + position_start.getX(),
                -size + position_start.getY() + Level.texture_bleed_offset, -size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV());
        GL11.glVertex3f(size + position_start.getX(),
                -size + position_start.getY() + Level.texture_bleed_offset, -size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV2());
        GL11.glVertex3f(size + position_start.getX(),
                -size + position_start.getY() + Level.texture_bleed_offset, size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV2());
        GL11.glVertex3f(-size + position_start.getX(),
                -size + position_start.getY() + Level.texture_bleed_offset, size + position_start.getZ());

        // Top
        GL11.glNormal3f(0, 1, 0);
        GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV());
        GL11.glVertex3f(-size + position_start.getX(),
                size + position_start.getY() - Level.texture_bleed_offset, -size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV2());
        GL11.glVertex3f(-size + position_start.getX(),
                size + position_start.getY() - Level.texture_bleed_offset, size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV2());
        GL11.glVertex3f(size + position_start.getX(),
                size + position_start.getY() - Level.texture_bleed_offset, size + position_start.getZ());
        GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV());
        GL11.glVertex3f(size + position_start.getX(),
                size + position_start.getY() - Level.texture_bleed_offset, -size + position_start.getZ());

        if (parallel) {
            // right 
            GL11.glNormal3f(1, 0, 0);
            GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV2());
            GL11.glVertex3f(size + position_start.getX() - Level.texture_bleed_offset, -size + position_start.getY(), -size + position_start.getZ());
            GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV());
            GL11.glVertex3f(size + position_start.getX() - Level.texture_bleed_offset, size + position_start.getY(), -size + position_start.getZ());
            GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV());
            GL11.glVertex3f(size + position_start.getX() - Level.texture_bleed_offset, size + position_start.getY(), size + position_start.getZ());
            GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV2());
            GL11.glVertex3f(size + position_start.getX() - Level.texture_bleed_offset, -size + position_start.getY(), size + position_start.getZ());
        } else {
            // back
            GL11.glNormal3f(0f, 0f, -1f);
            GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV2());
            GL11.glVertex3f(-size + position_start.getX(), -size + position_start.getY(), -size + position_start.getZ() + Level.texture_bleed_offset);
            GL11.glTexCoord2f(texture_surround.getU2(), texture_surround.getV());
            GL11.glVertex3f(-size + position_start.getX(), size + position_start.getY(), -size + position_start.getZ() + Level.texture_bleed_offset);
            GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV());
            GL11.glVertex3f(size + position_start.getX(), size + position_start.getY(), -size + position_start.getZ() + Level.texture_bleed_offset);
            GL11.glTexCoord2f(texture_surround.getU(), texture_surround.getV2());
            GL11.glVertex3f(size + position_start.getX(), -size + position_start.getY(), -size + position_start.getZ() + Level.texture_bleed_offset);
        }
    }

    @Override
    public double player_distance() {
        float x = _level._barry.position().getX() - position.getX();
        float y = _level._barry.position().getY() - position.getY();
        float z = _level._barry.position().getZ() - position.getZ();
        return Math.sqrt(x * x + y * y + z * z);
    }

    @Override
    public boolean IRemove() {
        return false;
    }
}