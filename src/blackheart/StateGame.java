/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      StateGame.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

/**
 *
 * @author rob
 */
public final class StateGame implements IState {

    public boolean dirty = false;
    public final Core _core;
    public final Level _level;
    private final String id = "GAME";
    public final Statistics _statistics;
    public boolean anaglyph = false;
    public int mouse_invert = 1;
    public final int number_of_levels = 4;

    public StateGame(Core core) throws Exception {
        _core = core;
        _statistics = new Statistics();
        _level = new Level(this);
    }

    @Override
    public String id() {
        return id;
    }

    @Override
    public void tick() throws Exception {
        dirty = true;
        _core._input.mouse(true);
        _level.tick();
    }

    public boolean anaglyph() {
        return anaglyph;
    }
}
