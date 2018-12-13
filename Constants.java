package agario;

/***************************************

***************************************/
public interface Constants {

	/* Window Dimensions*/
	public static final int _WIDTH 		 = 1050;
	public static final int _HEIGHT 	 = 750;

	/* Game Dimensions */
	public static final int GAME_WIDTH 	 = 750;
	public static final int GAME_HEIGHT  = 750;

	/* Game status */
	public final int 		START		 = 0;
	public final int 		IN_PROGRESS  = 1;
	public final int 		END          = 2;
	public final int 		WAITING		 = 3;
	public final int 		READY 		 = 4;

	/* Server port */
	public final int 		PORT 		 = 3010;

	/* Blob movements */
	public static int 		RIGHT 		 = 1;
	public static int 		LEFT		 = 2;
	public static int 		UP 			 = 3;
	public static int 		DOWN 		 = 4;
	public static int 		NONE 		 = 5;

}