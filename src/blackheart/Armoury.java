/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      Armoury.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
 *
 */
package blackheart;

import java.util.ArrayList;

/**
 *
 * @author rob
 */
public final class Armoury {

    public static int knife = 0;
    public static int pistol = 1;
    public static int shotgun = 2;
    public static int machinegun = 3;
    
    public final MobBarry _barry;
    private final ArrayList<IWeapon> armoury;
    private IWeapon weapon;

    public Armoury(MobBarry barry) {
        _barry = barry;
        armoury = new ArrayList<IWeapon>();
        armoury.add(knife, new WeaponKnife(this));
        armoury.add(pistol, new WeaponPistol(this));
        armoury.add(shotgun, new WeaponShotgun(this));
        armoury.add(machinegun, new WeaponMachinegun(this));
        set_weapon();
    }
    
    public IWeapon weapon() {
        return weapon;
    }
    
    public void usages(int index, int amount) {
        armoury.get(index).usages(amount);
        set_weapon();
    }
    
    public void set_weapon() {
        for (int i = armoury.size() - 1; i > 0; i -= 1) {
            if (armoury.get(i).usages() > 0) {
                weapon = armoury.get(i);
                return;
            }
        }
        weapon = armoury.get(knife);
    }
    
    @Override
    public Armoury clone() {
        Armoury _clone = new Armoury(_barry);
        for (int i = armoury.size() - 1; i > 0; i -= 1) {
            if (armoury.get(i).usages() > 0) {
                _clone.armoury.get(i).usages(armoury.get(i).usages());
            }
        }
        _clone.set_weapon();
        return _clone;
    }
       
}
