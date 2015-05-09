/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      InputHandler.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package io.flob.blackheart;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

/**
 *
 * @author rob
 */
public final class InputHandler {

    private final Core _core;

    public InputHandler(Core core) throws Exception {
        _core = core;
        Output.info("The keyboard has " + Keyboard.getKeyCount() + " keys");
    }

    public void mouse(boolean active) {
        if (Mouse.isGrabbed() != active) {
            Mouse.setGrabbed(active);
        }
    }

    public void poll() throws Exception {

        if (_core.state().equals(_core._title)) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (Misc.running_in_IDE()) {
                        if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
                            Debug.info = !Debug.info;
                        }
                    }
                }
            }
            return;
        }

        if (_core.state().equals(_core._game)) {
            while (Keyboard.next()) {
                if (Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                        _core.state(_core._title);
                        return;
                    }
                    if (Keyboard.getEventKey() == Keyboard.KEY_F5) {
                        _core._game._level.level_editor_refresh();
                        return;
                    }
                    if (Misc.running_in_IDE()) {
                        if (Keyboard.getEventKey() == Keyboard.KEY_F1) {
                            Debug.info = !Debug.info;
                        }
                        if (Keyboard.getEventKey() == Keyboard.KEY_F2) {
                            Debug.collision_boxs = !Debug.collision_boxs;
                        }
                        if (Keyboard.getEventKey() == Keyboard.KEY_F3) {
                            Debug.god = !Debug.god;
                        }
                        if (Keyboard.getEventKey() == Keyboard.KEY_F4) {
                            Debug.no_clip = !Debug.no_clip;
                        }
                    }
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_UP) | Keyboard.isKeyDown(Keyboard.KEY_W)) {
                _core._game._level._barry.move_forward();
                if (Keyboard.isKeyDown(Keyboard.KEY_UP)) {
                    _core._game._level._barry.pitch_correct();
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_DOWN) | Keyboard.isKeyDown(Keyboard.KEY_S)) {
                _core._game._level._barry.move_backwards();
                if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)) {
                    _core._game._level._barry.pitch_correct();
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
                _core._game._level._barry.move_left();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LEFT) | Keyboard.isKeyDown(Keyboard.KEY_Q)) {
                _core._game._level._barry.turn_left();
                if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)) {
                    _core._game._level._barry.pitch_correct();
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
                _core._game._level._barry.move_right();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT) | Keyboard.isKeyDown(Keyboard.KEY_E)) {
                _core._game._level._barry.turn_right();
                if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)) {
                    _core._game._level._barry.pitch_correct();
                }
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) | Mouse.isButtonDown(0)) {
                _core._game._level._barry._armoury.weapon().use();
            }
            if (Keyboard.isKeyDown(Keyboard.KEY_SPACE) | Mouse.isButtonDown(1)) {
                _core._game._level._barry.interact();
            }
            return;
        }

    }

    public void destroy() {
        Keyboard.destroy();
        Mouse.destroy();
    }
}
