package com.hari.flyingbird.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.hari.flyingbird.FlyingBird;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		new LwjglApplication(new FlyingBird(), config);
		config.height=1024;
		config.width=720;
		config.resizable=false;
	}
}
