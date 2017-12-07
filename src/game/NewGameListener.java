package game;

import java.util.EventListener;

public interface NewGameListener
		extends EventListener
{
	public void makeNewGame ( GameSettings settings );
}
