package Main;

import java.io.File;

public class Run {
	public static void main(String[] args) {
		System.setProperty("org.lwjgl.librarypath", new File("natives").getAbsolutePath());
		GameMain main = new GameMain();
		main.Start();
		System.exit(0);
	}
}
