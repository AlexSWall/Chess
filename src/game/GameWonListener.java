package game;

import java.util.EventListener;

import game.modes.TwoPlayerColour;

public interface GameWonListener
		extends EventListener
{
	public void gameWon ( TwoPlayerColour colourThatWon );
}
