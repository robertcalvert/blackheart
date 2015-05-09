/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      SoundLibrary.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.openal.SoundStore;

/**
 *
 * @author rob
 */
public class SoundLibrary {

    public final Audio level_gold;
    public final Audio level_pickup;
    public final Audio weapon_pistol;
    public final Audio weapon_shotgun;
    public final Audio weapon_machinegun;
    public final Audio level_door;
    public final Audio level_secret;
    public final Audio mob_prisoner;
    public final Audio button_over;
    public final Audio mob_dead;

    ;

    public SoundLibrary() throws Exception {
        level_gold = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/level/gold.ogg"));
        level_pickup = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/level/pickup.ogg"));
        weapon_pistol = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/weapon/pistol.ogg"));
        weapon_shotgun = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/weapon/shotgun.ogg"));
        weapon_machinegun = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/weapon/machinegun.ogg"));
        level_door = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/level/secret.ogg"));
        level_secret = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/level/secret.ogg"));
        mob_prisoner = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/mob/prisoner.ogg"));
        button_over = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/button/over.ogg"));
        mob_dead = AudioLoader.getAudio("OGG", getClass().getResourceAsStream("sound/mob/dead.ogg"));
    }

    public void poll() {
        SoundStore.get().poll(0);
    }

    public void destroy() {
        AL.destroy();
    }

    public boolean mute() {
        return SoundStore.get().soundsOn();
    }

    public void mute(boolean active) {
        SoundStore.get().setSoundsOn(active);
    }
}
