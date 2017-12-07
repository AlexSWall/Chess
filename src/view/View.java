package view;

import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;

import chess.AppSettings;
import game.GameSettings;
import game.board.Board;
import view.game.GameView;
import view.menu.MenuView;

public class View extends StateBasedGame
{
	public final AppSettings	settings;
	private GameView			game;
	private MenuView			menu;

	AppGameContainer appGameContainer;

	public View( AppSettings settings, Board board )
	{
		super( settings.gamename );

		this.settings = settings;
		this.game = new GameView( this, State.GAME.getValue(), board );
		this.menu = new MenuView( this, State.MENU.getValue() );

		this.addState( game );
		this.addState( menu );
	}

	@Override
	public void initStatesList ( GameContainer gc ) throws SlickException
	{
		this.getState( State.MENU.getValue() ).init( gc, this );
		this.getState( State.GAME.getValue() ).init( gc, this );

		this.enterState( State.MENU.getValue() );
	}

	public void start ()
	{
		try
		{
			appGameContainer = new AppGameContainer( this );
			appGameContainer.setDisplayMode( settings.width, settings.height, false );
			appGameContainer.setVSync( true );
			appGameContainer.setShowFPS( false );
			appGameContainer.start();
		}
		catch ( SlickException e )
		{
			e.printStackTrace();
		}
	}

	public void startGame ( GameSettings settings )
	{
		this.enterState( State.GAME.getValue() );
	}
}
