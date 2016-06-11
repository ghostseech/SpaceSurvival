package com.asdfgaems.desktop;

import com.asdfgaems.App;
import com.asdfgaems.Vars;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SpaceStationEscape";
        config.width = Vars.WIDTH;
        config.height = Vars.HEIGHT;
        new LwjglApplication(new App(true), config);
    }
}
