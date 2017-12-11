package chess;

public class AppSettings
{
	public final int	defaultHeight;
	public final int	defaultWidth;

	public final String	gamename;
	public final String	version;

	public int	width;
	public int	height;

	public AppSettings( String gamename, String version, int defaultWidth, int defaultHeight, int width, int height )
	{
		this.gamename = gamename;
		this.version = version;
		this.defaultHeight = defaultHeight;
		this.defaultWidth = defaultWidth;
		this.width = width;
		this.height = height;
	}

}
