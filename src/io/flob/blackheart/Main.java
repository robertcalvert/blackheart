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

import java.util.Date;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.openal.AL;
import org.lwjgl.opengl.Display;

/**
 * 
 * @author rob
 */
public final class Main {

    @SuppressWarnings("CallToThreadDumpStack")
    public static void main(String[] args) throws Exception {
        Core _core;
        String stack_trace = null;
        Misc.set_lwjgl_librarypath();
        System.out.println("#####################################");
        System.out.println(About.title + " " + About.version);
        System.out.println("#####################################");
        System.out.println(new Date());
        System.out.println("OS_NAME: " + System.getProperty("os.name"));
        System.out.println("OS_ARCH: " + System.getProperty("os.arch"));
        System.out.println("OS_VERSION: " + System.getProperty("os.version"));
        System.out.println("LWJGL_VERSION: " + Sys.getVersion() + " (" + org.lwjgl.LWJGLUtil.getPlatformName() + ")");
        System.out.println("JRE_VENDOR: " + System.getProperty("java.vendor"));
        System.out.println("JRE_VERSION: " + System.getProperty("java.version"));
        try {
            _core = new Core();
            _core.run();
        } catch (Exception ex) {
            System.out.println("************** ERROR! ***************");
            System.out.println(ex.getMessage());
            System.out.println(ex.toString());
            ex.printStackTrace();
            for (StackTraceElement element : ex.getStackTrace()) {
                stack_trace = stack_trace + element + System.getProperty("line.separator");
            }
            Sys.alert(About.title,
                    "************** ERROR! ***************"
                    + System.getProperty("line.separator")
                    + ex.getMessage()
                    + System.getProperty("line.separator")
                    + ex.toString()
                    + System.getProperty("line.separator")
                    + stack_trace
                    + "*************************************");
            System.out.println("*************************************");
            Display.destroy();
            Keyboard.destroy();
            Mouse.destroy();
            AL.destroy();
        } finally {
            System.out.println("#####################################");
        }
    }
}
