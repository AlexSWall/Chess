package view.menu;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.state.BasicGameState;
import org.newdawn.slick.state.StateBasedGame;

import view.View;

public class MenuView extends BasicGameState
{
	private final View		mainView;
	private final MenuData	menuData;
	private final int		ID;

	public MenuView( View mainView, int ID )
	{
		this.mainView = mainView;
		this.menuData = new MenuData();
		this.ID = ID;
	}

	@Override
	public void init ( GameContainer arg0, StateBasedGame arg1 ) throws SlickException
	{
		menuData.init();
	}

	@Override
	public void render ( GameContainer gc, StateBasedGame sbg, Graphics g ) throws SlickException
	{
		g.scale( mainView.getAppSettings().width / 666f, mainView.getAppSettings().height / 666f );

		menuData.getBackground().draw( 0, 0 );
	}

	@Override
	public void update ( GameContainer gc, StateBasedGame sbg, int delta ) throws SlickException
	{
		menuData.update( gc.getInput(), delta );
	}

	@Override
	public int getID ()
	{
		return ID;
	}
}
