package battlearena.main.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import battlearena.editor.WorldEditor;
import battlearena.game.BattleArena;

public class DesktopLauncher
{
	public static void main (String[] args)
	{
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		
		if(args.length < 1 || !(args[0].equals("-editor")))
		{
			config.width = 432;
			config.height = 888;
			config.resizable = false;
			config.title = "Battle Arena";

			new LwjglApplication(new BattleArena(false), config);
		}
		else
		{
			config.width = 16*60;
			config.height = 9*60;
			config.resizable = true;
			config.title = "Battle Arena World Editor";

			new LwjglApplication(WorldEditor.I, config);
		}

	}

}
