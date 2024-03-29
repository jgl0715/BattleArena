package battlearena.main;

import android.os.Bundle;
import battlearena.game.BattleArena;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;

public class AndroidLauncher extends AndroidApplication
{

	@Override
	protected void onCreate (Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		initialize(new BattleArena(), config);
	}
}
