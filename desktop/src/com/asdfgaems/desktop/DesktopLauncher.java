package com.asdfgaems.desktop;

import com.asdfgaems.core.SpaceSurvival;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

public class DesktopLauncher {
    public static void main(String[] args) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "SpaceStationEscape";
        config.width = SpaceSurvival.V_WIDTH;
        config.height = SpaceSurvival.V_HEIGHT;
        new LwjglApplication(new SpaceSurvival(), config);
    }
}
