package chess;

import game.GameSettings;
import game.GameWonListener;
import game.NewGameEventFirer;
import game.NewGameListener;
import game.board.Board;
import game.modes.TwoPlayerColour;
import view.State;
import view.View;

public class App
		implements NewGameListener, GameWonListener
{
	final AppSettings	settings;
	final Board			board;
	final View			view;

	App( AppSettings settings )
	{
		this.settings = settings;
		this.board = new Board();
		this.view = new View( settings, board );

		NewGameEventFirer.addNewGameListener( this );
		this.board.gameWonFirer.addGameWonListener( this );

		this.view.start();
	}

	@Override
	public void makeNewGame ( GameSettings settings )
	{
		board.setup( settings );
		view.startGame( settings );
	}

	@Override
	public void gameWon ( TwoPlayerColour colourThatWon )
	{
		view.enterState( State.MENU.getValue() );
	}
}
