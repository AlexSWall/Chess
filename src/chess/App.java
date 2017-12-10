package chess;

import java.util.logging.Level;
import java.util.logging.Logger;

import game.GameSettings;
import game.GameWonListener;
import game.NewGameEventFirer;
import game.NewGameListener;
import game.board.Board;
import game.board.promotion.PromotionChoice;
import game.board.promotion.PromotionListener;
import game.modes.TwoPlayerColour;
import game.pieces.Pawn;
import view.View;

public class App
		implements NewGameListener, GameWonListener, PromotionListener
{
	private final static Logger logger = Logger.getLogger( App.class.getName() );

	final AppSettings	settings;
	final Board			board;
	final View			view;

	App( AppSettings settings )
	{
		logger.log( Level.FINE, "Creating and Starting Chess App." );
		this.settings = settings;

		logger.log( Level.FINE, "Creating Board." );
		this.board = new Board();

		logger.log( Level.FINE, "Creating View." );
		this.view = new View( settings, board );

		NewGameEventFirer.addNewGameListener( this );
		this.board.gameWonFirer.addGameWonListener( this );
		this.board.setPromotionListener( this );
		this.view.getGameView().createPromotionView( this );

		logger.log( Level.FINE, "Starting View." );
		this.view.start();
	}

	@Override
	public void makeNewGame ( GameSettings settings )
	{
		logger.log( Level.FINE, "Creating New Game." );
		board.setup( settings );
		view.startGame( settings );
	}

	@Override
	public void gameWon ( TwoPlayerColour winningColour )
	{
		if ( winningColour != null )
		{
			logger.log( Level.FINE, "Game has been won by " + winningColour.toString() + "." );
			// view.enterState( State.MENU.getValue() );
		}
		else
		{
			logger.log( Level.FINE, "Game has been drawn." );
			//			view.enterState( State.MENU.getValue() );
		}
	}

	@Override
	public void promote ( Pawn pawn, PromotionChoice choice )
	{
		if ( choice == PromotionChoice.CHOOSING )
		{
			view.getGameView().startPromotionChoiceMode( pawn );
		}
		else
		{
			board.promote( pawn, choice );
		}
	}
}
