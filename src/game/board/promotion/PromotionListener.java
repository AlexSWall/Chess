package game.board.promotion;

import java.util.EventListener;

import game.pieces.Pawn;

public interface PromotionListener
		extends EventListener
{
	public void promote ( Pawn pawn, PromotionChoice choice );
}
