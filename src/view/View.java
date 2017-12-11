package view;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.StateBasedGame;
import org.newdawn.slick.state.transition.FadeInTransition;
import org.newdawn.slick.state.transition.FadeOutTransition;

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
		this.enterState( State.MENU.getValue() );
	}

	public void start ()
	{
		try
		{
			Display.setResizable( false );
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
		fadeChangeState( State.GAME, 1000, 400 );
	}

	public void leaveGame ()
	{
		fadeChangeState( State.MENU, 6000, 400 );
	}

	private void fadeChangeState ( State state, int fadeoutMillis, int fadeinMillis )
	{
		this.enterState( state.getValue(), new FadeOutTransition( Color.black, fadeoutMillis ), new FadeInTransition( Color.black, fadeinMillis ) );
	}

	public GameView getGameView ()
	{
		return game;
	}

	public AppSettings getAppSettings ()
	{
		return settings;
	}
}
