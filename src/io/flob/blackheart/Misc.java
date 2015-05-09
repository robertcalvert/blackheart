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

import org.lwjgl.Sys;

/**
 *
 * @author rob
 */
public final class Misc {
    
    public static long time() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }
    
    private static  boolean running_in_IDE;
    
    public static boolean running_in_IDE() {
        return running_in_IDE;
    }
    
    public static void set_lwjgl_librarypath() throws Exception {
        String base_path = Path.base();
        if ("NETBEANS".equals(System.getProperty("org.lwjgl.librarypath"))) {
            System.setProperty("org.lwjgl.librarypath", base_path
                    + "../../../../lib/lwjgl2/native/" + org.lwjgl.LWJGLUtil.getPlatformName());
            running_in_IDE = true;
        } else {
            System.setProperty("org.lwjgl.librarypath", base_path
                    + "lib/native/" + org.lwjgl.LWJGLUtil.getPlatformName());
        }
    }

}
