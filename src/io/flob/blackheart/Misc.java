/*
 *    ______     __         ______     ______     __  __     __  __     ______     ______     ______     ______  
 *   /\  == \   /\ \       /\  __ \   /\  ___\   /\ \/ /    /\ \_\ \   /\  ___\   /\  __ \   /\  == \   /\__  _\ 
 *   \ \  __<   \ \ \____  \ \  __ \  \ \ \____  \ \  _"-.  \ \  __ \  \ \  __\   \ \  __ \  \ \  __<   \/_/\ \/ 
 *    \ \_____\  \ \_____\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\  \ \_____\  \ \_\ \_\  \ \_\ \_\    \ \_\ 
 *     \/_____/   \/_____/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/\/_/   \/_____/   \/_/\/_/   \/_/ /_/     \/_/ 
 * 
 *      Misc.java
 *
 *      Copyright (c) 2013 Robert Calvert <http://www.facebook.com/robert.calvert>
 *      All Rights Reserved.
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
