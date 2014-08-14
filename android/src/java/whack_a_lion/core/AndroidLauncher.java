package whack_a_lion.core;

import clojure.lang.RT;
import clojure.lang.Symbol;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.Game;

public class AndroidLauncher extends AndroidApplication {
	public void onCreate (android.os.Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
          RT.var("clojure.core", "require").invoke(Symbol.intern("whack-a-lion.core"));
		try {
			Game game = (Game) RT.var("whack-a-lion.core", "whack-a-lion").deref();
			initialize(game);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
