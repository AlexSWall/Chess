package game.board.promotion;

import game.board.Board;
import game.pieces.Bishop;
import game.pieces.Knight;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Queen;
import game.pieces.Rook;

public enum PromotionChoice
{
	BISHOP, KNIGHT, ROOK, QUEEN, CHOOSING;

	public Piece getPromotionPiece ( Board board, Pawn pawn )
	{
		switch ( this )
		{
			case BISHOP:
				return new Bishop( board, pawn.getColour(), pawn.getX(), pawn.getY() );
			case KNIGHT:
				return new Knight( board, pawn.getColour(), pawn.getX(), pawn.getY() );
			case QUEEN:
				return new Queen( board, pawn.getColour(), pawn.getX(), pawn.getY() );
			case ROOK:
				return new Rook( board, pawn.getColour(), pawn.getX(), pawn.getY() );
			case CHOOSING:
			default:
				return null;

		}

	}
}
