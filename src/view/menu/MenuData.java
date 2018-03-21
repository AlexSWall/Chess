package view.menu;

import java.util.Random;

import org.newdawn.slick.Image;
import org.newdawn.slick.Input;

import game.GameSettings;
import game.NewGameEventFirer;
import game.board.BoardSetupType;
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

	public void update ( Input input, int delta )
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

		if ( input.isKeyPressed( Input.KEY_ENTER ) )
		{
			menuRoot.children.get( optionSelected ).data.action.act();
		}
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
		if ( optionsAction.twoPlayered )
			return TwoPlayerColour.BOTH;
		else
			return ( ( new Random() ).nextInt( 2 ) == 0 ? TwoPlayerColour.WHITE : TwoPlayerColour.BLACK );
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
			GameSettings settings = new GameSettings( BoardSetupType.STANDARD, generatePlayerColour() );
			fireNewGameEvent( settings );
		}
	}

	class StartChess960GameAction extends NewGameEventFirer
			implements Action
	{
		@Override
		public void act ()
		{
			GameSettings settings = new GameSettings( BoardSetupType.CHESS960, generatePlayerColour() );
			fireNewGameEvent( settings );
		}
	}

	class OptionsAction
			implements Action
	{
		public boolean twoPlayered = false;

		@Override
		public void act ()
		{
			twoPlayered = !twoPlayered;
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
