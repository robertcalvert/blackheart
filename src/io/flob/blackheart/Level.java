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

import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import javax.imageio.ImageIO;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector3f;

/**
 *
 * @author rob
 */
public final class Level {

    public static final float grid_size = 1f;
    public static final float texture_bleed_offset = 0.0004f;
     private float floorblood_offset = texture_bleed_offset;
    private float floorblood_offset_bump = 0.0001f;
    public final StateGame _game;
    public final MobBarry _barry;
    private Armoury _start_armoury;
    public ArrayList<ILevelObject> objects_static;
    public ArrayList<ILevelObject> objects_dynamic;
    private final HUD _hud;
    public Statistics _statistics;
    private int display_list = -1;
    private boolean display_list_dirty;
    private int level_number = 1;

    public Level(StateGame game) throws Exception {
        _game = game;
        _barry = new MobBarry(this, new Vector3f(0, 0, 0));
        _hud = new HUD(this);
        load(level_number);
    }

    private void load(int number) throws Exception {
        if (number > _game.number_of_levels) {
            _game._statistics.merge(_statistics);
            _game._core.state(_game._core._game_winner);
        } else {
            _game._core.state(_game);
            level_number = number;
            load_objects();
            _start_armoury = _barry._armoury.clone();
            _barry.yaw(-_barry.yaw());
            _barry.pitch(-_barry.pitch());
            _barry.health(_barry.health_maximum());
            _statistics = new Statistics();
        }
    }

    public int number() {
        return level_number;
    }

    public void complete() throws Exception {
        _game._statistics.merge(_statistics);
        load(level_number + 1);
    }

    public void reload() throws Exception {
        _barry._armoury = _start_armoury;
        load(level_number);
    }

    public void level_editor_refresh() throws Exception {
        if (Misc.running_in_IDE()) {
            Vector3f position = new Vector3f(_barry.position());
            load_objects();
            _barry.position(position);
        }
    }

    private void load_objects() throws Exception {
        floorblood_offset = texture_bleed_offset;
        objects_static = new ArrayList<ILevelObject>();
        objects_dynamic = new ArrayList<ILevelObject>();
        load_layer(1); // Floor
        load_layer(0); // Middle
        load_layer(-1); // Ceiling
        display_list_dirty = true;
    }

    private void load_layer(int layer) throws Exception {
        BufferedImage _objects;
        if (Misc.running_in_IDE()) {
            FileInputStream file = new FileInputStream(Path.base()
                    + "../src/io/flob/" + About.title + "/level/" + level_number + "/" + layer + ".png");
            _objects = ImageIO.read(new BufferedInputStream(file));
        } else {
            _objects = ImageIO.read(new BufferedInputStream(getClass().getResourceAsStream("level/"
                    + level_number + "/" + layer + ".png")));
        }
        for (int z = 0; z < (_objects.getHeight()); z++) {
            for (int x = 0; x < (_objects.getWidth()); x++) {
                int rgb = _objects.getRGB(x, z);
                Vector3f position = new Vector3f(x * Level.grid_size, layer, z * Level.grid_size);
                if (rgb == 0) {
                    continue;
                } else {
                    if (layer == -1) {
                        // Floor
                        if (rgb == -5086909) {
                            objects_static.add(new FloorWood(this, position));
                        } else if (rgb == -12040120) {
                            objects_static.add(new FloorLift(this, position));
                        } else if (rgb == -6984636) {
                            objects_static.add(new FloorLightWood(this, position));
                        } else if (rgb == -12365488) {
                            objects_static.add(new FloorDarkGrey(this, position));
                        } else {
                            Output.error("Level.load_layer(" + layer + ") - Unknow rgb(" + rgb + ")");
                        }
                    } else if (layer == 0) {
                        // Play area
                        if (rgb == -1) {
                            _barry.position(position);
                        } else if (rgb == -4737179) {
                            objects_dynamic.add(new MobPistol(this, position));
                        } else if (rgb == -8499253) {
                            objects_dynamic.add(new MobShotgun(this, position));
                        } else if (rgb == -11185590) {
                            objects_dynamic.add(new MobPrisoner(this, position));
                        } else if (rgb == -8479931) {
                            objects_dynamic.add(new MobZombie(this, position));
                        } else if (rgb == -6127072) {
                            objects_dynamic.add(new MobMachinegun(this, position));
                        } else if (rgb == -12422803) {
                            objects_static.add(new WallBlue(this, position));
                        } else if (rgb == -9345719) {
                            objects_static.add(new WallBluePainting(this, position));
                        } else if (rgb == -10854049) {
                            objects_dynamic.add(new WallSecretBlue(this, position));
                        } else if (rgb == -13679032) {
                            objects_dynamic.add(new DoorBlue(this, position, true));
                        } else if (rgb == -10580589) {
                            objects_dynamic.add(new DoorBlue(this, position, false));
                        } else if (rgb == -12829636) {
                            objects_dynamic.add(new DoorLift(this, position, true));
                        } else if (rgb == -12831178) {
                            objects_dynamic.add(new DoorLift(this, position, false));
                        } else if (rgb == -7325383) {
                            objects_static.add(new WallRed(this, position));
                        } else if (rgb == -12365488) {
                            objects_static.add(new WallGrey(this, position));
                        } else if (rgb == -12434885) {
                            objects_dynamic.add(new WindowBars(this, position, true));
                        } else if (rgb == -10921648) {
                            objects_dynamic.add(new WindowBars(this, position, false));
                        } else if (rgb == -2044210) {
                            objects_dynamic.add(new CollectableHealth(this, position));
                        } else if (rgb == -1387165) {
                            objects_dynamic.add(new CollectableGold(this, position));
                        } else if (rgb == -5881278) {
                            objects_dynamic.add(new WallSecretRed(this, position));
                        } else if (rgb == -14140101) {
                            objects_dynamic.add(new WallSecretGrey(this, position));
                        } else if (rgb == -10461088) {
                            objects_static.add(new WallLiftPanel(this, position));
                        } else if (rgb == -9740706) {
                            objects_static.add(new WallLiftPanelInactive(this, position));
                        } else if (rgb == -8816263) {
                            objects_static.add(new WallLiftSides(this, position));
                        } else if (rgb == -6337203) {
                            objects_static.add(new WallRedDeadInside1(this, position));
                        } else if (rgb == -4107180) {
                            objects_static.add(new WallRedDeadInside2(this, position));
                        } else {
                            Output.error("Level.load_layer(" + layer + ") - Unknow rgb(" + rgb + ")");
                        }

                    } else if (layer == 1) {
                        // Ceiling
                        if (rgb == -9078165) {
                            objects_static.add(new CeilingLightGrey(this, position));
                        } else if (rgb == -12040120) {
                            objects_static.add(new CeilingLift(this, position));
                        } else if (rgb == -12365488) {
                            objects_static.add(new CeilingDarkGrey(this, position));
                        } else {
                            Output.error("Level.load_layer(" + layer + ") - Unknow rgb(" + rgb + ")");
                        }
                    }
                }
            }
        }
    }

    private void load_display_list() throws Exception {
        display_list = GL11.glGenLists(1);
        GL11.glNewList(display_list, GL11.GL_COMPILE);
        for (int count = 0; count < objects_static.size(); count++) {
            objects_static.get(count).tick();
        }
        GL11.glEndList();
        display_list_dirty = false;
    }

    public void tick() throws Exception {

        if (display_list_dirty) {
            load_display_list();
        }

        ByteBuffer temp = ByteBuffer.allocateDirect(16);
        temp.order(ByteOrder.nativeOrder());
        float fog_colour[] = new float[]{0f, 0f, 0f, 0f};
        GL11.glFogi(GL11.GL_FOG_MODE, GL11.GL_EXP);
        temp.asFloatBuffer().put(fog_colour).flip();
        GL11.glFog(GL11.GL_FOG_COLOR, temp.asFloatBuffer());
        GL11.glFogf(GL11.GL_FOG_DENSITY, 0.25f);
        GL11.glHint(GL11.GL_FOG_HINT, GL11.GL_DONT_CARE);
        GL11.glFogf(GL11.GL_FOG_START, 0);
        GL11.glFogf(GL11.GL_FOG_END, 200);

        _barry.tick();

        try {
            Collections.sort(objects_dynamic, this.sort_objects);
        } catch (Exception ex) {
            Output.error(ex.getMessage());
        }

        if (Debug.collision_boxs) {
            _game._core._display.flush_texture();
            for (int count = 0; count < objects_static.size(); count++) {
                if (objects_static.get(count) instanceof ICollidable) {
                    ICollidable objcet = (ICollidable) objects_static.get(count);
                    objcet.collision_box().render();
                }
            }
            for (int count = 0; count < objects_dynamic.size(); count++) {
                if (objects_dynamic.get(count) instanceof ICollidable) {
                    ICollidable objcet = (ICollidable) objects_dynamic.get(count);
                    objcet.collision_box().render();
                }
            }
            _barry.collision_box().render();
            _barry.ray_picker().collision_box().render();
        }

        GL11.glPushMatrix();
        _game._core._texture.game_atlas.bind();
        if (_game.anaglyph()) {
            GL11.glTranslatef(1 * 0.03f, 0, 0);
            GL11.glColorMask(true, false, false, true);
        }
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glCallList(display_list);
        for (int count = 0; count < objects_dynamic.size(); count++) {
            objects_dynamic.get(count).tick();
        }
        GL11.glEnd();
        if (_game.anaglyph()) {
            GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
            GL11.glColorMask(false, true, true, true);
            GL11.glTranslatef(-1 * 0.03f, 0, 0);
            GL11.glBegin(GL11.GL_QUADS);
            GL11.glCallList(display_list);
            for (int count = 0; count < objects_dynamic.size(); count++) {
                objects_dynamic.get(count)._render();
            }
            GL11.glEnd();
            GL11.glColorMask(true, true, true, true);
        }
        GL11.glPopMatrix();

        for (int count = 0; count < objects_dynamic.size(); count++) {
            if (objects_dynamic.get(count).IRemove()) {
                objects_dynamic.remove(count);
            }
        }

        _hud.tick();

        if (_barry.health() <= 0) {
            _game._core.state(_game._core._game_dead);
        }
    }
    private Comparator<ILevelObject> sort_objects = new Comparator<ILevelObject>() {
        @Override
        public int compare(ILevelObject object1, ILevelObject object2) {
            Double distance1 = object1.player_distance();
            Double distance2 = object2.player_distance();
            // Always push FloorBloods back in the distance stack so that they are rendered first
            if (object1 instanceof FloorBlood) {
                distance1 = distance2 + 0.1f;
            }
            if (object2 instanceof FloorBlood) {
                distance2 = distance1 + 0.1f;
            }
            // Always push IDoors back in the distance stack so that they are rendered first
            // This is behind the FloorBloods
            if (object1 instanceof IDoor) {
                distance1 = distance2 + 0.1f;
            }
            if (object2 instanceof IDoor) {
                distance2 = distance1 + 0.1f;
            }
            return distance2.compareTo(distance1);
        }
    };

    public void _render() throws Exception {
        // assume the texture is already bound
        _barry._look_through();
        GL11.glBegin(GL11.GL_QUADS);
        GL11.glCallList(display_list);
        for (int count = 0; count < objects_dynamic.size(); count++) {
            objects_dynamic.get(count)._render();
        }
        GL11.glEnd();
    }

    public ICollidable collision(ICollidable what) {
        if (Debug.no_clip) {
            if (what.equals(_barry)) {
                return null;
            }
        }
        for (int count = 0; count < objects_static.size(); count++) {
            if (objects_static.get(count) instanceof ICollidable) {
                ICollidable with = (ICollidable) objects_static.get(count);
                if (what != with) {
                    if (what.collision_box().intersects(with.collision_box())) {
                        return with;
                    }
                }
            }
        }
        for (int count = 0; count < objects_dynamic.size(); count++) {
            if (objects_dynamic.get(count) instanceof ICollidable) {
                ICollidable with = (ICollidable) objects_dynamic.get(count);
                if (what != with) {
                    if (what.collision_box().intersects(with.collision_box())) {
                        if (what.equals(_barry)) {
                            if (with instanceof MobPrisoner) {
                                MobPrisoner _object = (MobPrisoner) with;
                                if (_object.following()) {
                                    return null;
                                }
                            }
                        }
                        return with;
                    }
                }
            }
        }
        if (what != _barry) {
            if (what.collision_box().intersects(_barry.collision_box())) {
                return _barry;
            }
        }
        return null;
    }
    
    // Used to stack the layers better
    // May not work well on some cards.....
    public float floorblood_offset() {
        floorblood_offset += floorblood_offset_bump;
        return floorblood_offset;
    }
}