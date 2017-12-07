package view.menu;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import game.GameSettings;
import game.NewGameEventFirer;
import game.modes.TwoPlayerColour;
import view.menu.MenuData.Action;

public class MenuData
{
	final TreeNode<MenuOptionData>	menuRoot;
	TreeNode<MenuOptionData>		currentOption;

	StartNormalGameAction	newGameAction;
	StartChess960GameAction	newChess960GameAction;
	OptionsAction			optionsAction;
	QuitAction				quitAction;

	MenuGraphics graphics;

	int		buttonTimer		= 0;
	boolean	buttonHeldDown;
	int		optionSelected	= 0;

	public MenuData()
	{
		menuRoot = new TreeNode<MenuOptionData>( null );

		newGameAction = new StartNormalGameAction();
		newChess960GameAction = new StartChess960GameAction();
		optionsAction = new OptionsAction();
		quitAction = new QuitAction();

		menuRoot.addChild( new MenuOptionData( "Start Start Game", newGameAction ) );
		menuRoot.addChild( new MenuOptionData( "Start Custom Game", newChess960GameAction ) );
		menuRoot.addChild( new MenuOptionData( "Options", optionsAction ) );
		menuRoot.addChild( new MenuOptionData( "Quit", quitAction ) );

		currentOption = menuRoot.children.get( 0 );
	}

	public void init ()
	{
		graphics = new MenuGraphics();
	}

	public Image getBackground ()
	{
		return graphics.getBackground( optionSelected );
	}

	public int update ( Input input, int delta )
	{
		if ( !( input.isKeyDown( Input.KEY_DOWN ) || input.isKeyDown( Input.KEY_UP ) ) )
		{
			buttonHeldDown = false;
			buttonTimer = 0;
		}
		else
		{
			if ( !buttonHeldDown )
			{
				if ( buttonTimer == 0 )
				{
					if ( input.isKeyDown( Input.KEY_UP ) )
					{
						optionSelected = modulo( optionSelected - 1, 4 );
					}
					if ( input.isKeyDown( Input.KEY_DOWN ) )
					{
						optionSelected = modulo( optionSelected + 1, 4 );
					}
				}
				else if ( buttonTimer > 200 )
				{
					buttonTimer = 0;
					buttonHeldDown = true;
				}
			}
			else
			{
				if ( buttonTimer > 100 )
				{
					buttonTimer = 0;

					if ( input.isKeyDown( Input.KEY_UP ) )
					{
						optionSelected = modulo( optionSelected - 1, 4 );
					}
					if ( input.isKeyDown( Input.KEY_DOWN ) )
					{
						optionSelected = modulo( optionSelected + 1, 4 );
					}
				}
			}

			buttonTimer += delta;
		}

		if ( input.isKeyDown( Input.KEY_ENTER ) )
		{
			menuRoot.children.get( optionSelected ).data.action.act();
		}

		return -1;
	}

	private int modulo ( int n, int m )
	{
		n = n % m;
		if ( n < 0 )
		{
			n += m;
		}
		return n;
	}

	protected TwoPlayerColour generatePlayerColour ()
	{
		TwoPlayerColour playerColour = TwoPlayerColour.WHITE;
		if ( optionsAction.random )
		{
			if ( ( new Random() ).nextInt( 2 ) == 0 )
			{
				playerColour = TwoPlayerColour.BLACK;
			}
		}
		return playerColour;
	}

	interface Action
	{
		public void act ();
	}

	class StartNormalGameAction extends NewGameEventFirer
			implements Action
	{
		@Override
		public void act ()
		{
			GameSettings settings = new GameSettings( true, generatePlayerColour() == TwoPlayerColour.WHITE );
			fireNewGameEvent( settings );
		}
	}

	class StartChess960GameAction extends NewGameEventFirer
			implements Action
	{
		@Override
		public void act ()
		{
			GameSettings settings = new GameSettings( false, generatePlayerColour() == TwoPlayerColour.WHITE );
			fireNewGameEvent( settings );
		}
	}

	class OptionsAction
			implements Action
	{
		public boolean random = false;

		@Override
		public void act ()
		{
			random = !random;
		}
	}

	class QuitAction
			implements Action
	{
		@Override
		public void act ()
		{
			System.exit( 0 );
		}
	}
}

class MenuOptionData
{
	public final String	text;
	public final Action	action;

	public MenuOptionData( String text, Action action )
	{
		this.text = text;
		this.action = action;
	}
}