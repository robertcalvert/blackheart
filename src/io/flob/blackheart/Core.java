/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      Core.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;

/**
 *
 * @author rob
 */
public final class Core {

    public final DisplayDriver _display;
    private final StateSplash _splash;
    public final TextureLibrary _texture;
    private final StateSleep _sleep;
    public final StateTitle _title;
    public final StateTitleHelp _title_help;
    public final InputHandler _input;
    public final FontLibrary _font;
    public final SoundLibrary _sound;
    public StateGame _game;
    public final StateGameDead _game_dead;
    public final StateGameLevelComplete _game_level_complete;
    public final StateGameWinner _game_winner;
    private IState state;
    private IState state_previous;

    public Core() throws Exception {
        _display = new DisplayDriver();
        _splash = new StateSplash(this);
        state(_splash);
        _splash.tick();
        _texture = new TextureLibrary();
        _sleep = new StateSleep(this);
        _sound = new SoundLibrary();
        _title = new StateTitle(this);
        _title_help = new StateTitleHelp(this);
        _input = new InputHandler(this);
        _font = new FontLibrary();
        _game = new StateGame(this);
        _game_dead = new StateGameDead(this);
        _game_level_complete = new StateGameLevelComplete(this);
        _game_winner = new StateGameWinner(this);
        state(_title);
    }

    public void state(IState new_state) {
        state_previous = state;
        state = new_state;
    }

    public IState state() {
        return state;
    }

    public void run() throws Exception {
        while (!Display.isCloseRequested()) {
            _display.prepare();
            if (Display.isActive()) {
                if (state.equals(_sleep)) {
                    state(state_previous);
                }
                state.tick();
            } else {
                _sleep.tick();
            }
            tick();
            _input.poll();
            _sound.poll();
            _display.update();
        }
        _display.destroy();
        _input.destroy();
        _sound.destroy();
    }

    private void tick() {
        if (Debug.info) {
            _display.mode_2D();
            int right_offset = 180;
            int KB = 1024;

            _font.debug.render(4, 5, About.title + " " + About.version);

            _font.debug.render(4, 25, "OS_NAME: " + System.getProperty("os.name"));
            _font.debug.render(4, 35, "OS_ARCH: " + System.getProperty("os.arch"));
            _font.debug.render(4, 45, "OS_VERSION: " + System.getProperty("os.version"));
            _font.debug.render(4, 55, "LWJGL_VERSION: " + Sys.getVersion()
                    + " (" + org.lwjgl.LWJGLUtil.getPlatformName() + ")");
            _font.debug.render(4, 65, "JRE_VENDOR: " + System.getProperty("java.vendor"));
            _font.debug.render(4, 75, "JRE_VERSION: " + System.getProperty("java.version"));
            _font.debug.render(4, 85, "GL_VENDOR: " + GL11.glGetString(GL11.GL_VENDOR));
            _font.debug.render(4, 95, "GL_RENDERER: " + GL11.glGetString(GL11.GL_RENDERER));
            _font.debug.render(4, 105, "GL_VERSION: " + GL11.glGetString(GL11.GL_VERSION));

            _font.debug.render(4, 125, ("RUNNING_IN_IDE: " + Misc.running_in_IDE()).toUpperCase());

            _font.debug.render(4, 145, "STATE: " + state.id());
            _font.debug.render(4, 155, "STATE_PREVIOUS: " + state_previous.id());

            if (state.equals(_game)) {
                _font.debug.render(4, 175, "PLAYER_POSITION: " + _game._level._barry.position());
                _font.debug.render(4, 185, "PLAYER_YAW: " + _game._level._barry.yaw());
                _font.debug.render(4, 195, "PLAYER_PITCH: " + _game._level._barry.pitch());
                _font.debug.render(4, 205, "PLAYER_RAYPICK_OBJECT: " + _game._level._barry.ray_picker().object());
                _font.debug.render(4, 215, "PLAYER_RAYPICK_DISTANCE: " + _game._level._barry.ray_picker().distance());
                _font.debug.render(4, 225, "PLAYER_HEALTH: " + _game._level._barry.health());

                _font.debug.render(4, 245, "LEVEL_POINTS: " + _game._level._statistics.points());
                _font.debug.render(4, 255, "LEVEL_MOBS_KILLED: " + _game._level._statistics.mobs_killed());
                _font.debug.render(4, 265, "LEVEL_SHOTS_FIRED: " + _game._level._statistics.shots_fired());
                _font.debug.render(4, 275, "LEVEL_SECRETS_FOUND: " + _game._level._statistics.secrets_found());
                _font.debug.render(4, 285, "LEVEL_PRISONERS_KILLED: " + _game._level._statistics.prisoners_killed());

                _font.debug.render(4, 305, "WEAPON_USAGES: " + _game._level._barry._armoury.weapon().usages());

                _font.debug.render(4, 325, "LEVEL_OBJECTS: " + _game._level.objects_static.size() + _game._level.objects_dynamic.size());
            }

            _font.debug.render(_display.width() - right_offset, 5, "TIME: " + Misc.time());
            _font.debug.render(_display.width() - right_offset, 15, "DELTA: " + _display.delta());
            _font.debug.render(_display.width() - right_offset, 25, "FPS: " + _display.fps());
            _font.debug.render(_display.width() - right_offset, 45, "JVM_MAX_MEMORY: "
                    + (Runtime.getRuntime().maxMemory() / KB) + " KB");
            _font.debug.render(_display.width() - right_offset, 55, "JVM_TOTAL_MEMORY: "
                    + (Runtime.getRuntime().totalMemory() / KB) + " KB");
            _font.debug.render(_display.width() - right_offset, 65, "JVM_FREE_MEMORY: "
                    + (Runtime.getRuntime().freeMemory() / KB) + " KB");
            _font.debug.render(_display.width() - right_offset, 75, "JVM_INUSE_MEMORY: "
                    + ((Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / KB) + " KB");
            _display.mode_3D();
        }
    }
}
