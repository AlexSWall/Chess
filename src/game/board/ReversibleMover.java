package game.board;

import game.pieces.King;
import game.pieces.Pawn;
import game.pieces.Piece;
import game.pieces.Rook;

public class ReversibleMover
{
	private final Board board;

	private ReversalData data;

	public ReversibleMover( Board board )
	{
		this.board = board;
	}

	public void doMove ( Piece piece, int newX, int newY, boolean isCastling, boolean isTakingEnPassant )
	{
		data = new ReversalData( piece, newX, newY, isCastling, isTakingEnPassant );

		if ( data.isCastling )
		{
			board.setPiece( null, piece.getX(), piece.getY() );
			board.setPiece( null, data.castlingData.rookOldX, data.castlingData.rookOldY );

			if ( data.castlingData.kingside )
			{
				board.setPiece( piece, 6, piece.getY() );
				board.setPiece( data.castlingData.rook, 5, piece.getY() );
			}
			else
			{
				board.setPiece( piece, 2, piece.getY() );
				board.setPiece( data.castlingData.rook, 3, piece.getY() );
			}

			data.movedPiece.setHasMoved( true );
			data.castlingData.rook.setHasMoved( true );
		}
		else if ( data.isTakingEnPassant )
		{
			board.getPieces().remove( data.pieceTaken );

			board.setPiece( null, data.oldX, data.oldY );
			board.setPiece( data.movedPiece, data.newX, data.newY );
			board.setPiece( null, data.newX, data.oldY );
			data.movedPiece.setHasMoved( true );
		}
		else
		{
			board.getPieces().remove( data.pieceTaken );

			if ( data.pieceTaken != null && data.pieceTaken.getClass() == King.class )
			{
				board.getPlayersKings().get( data.pieceTaken.getColour() ).remove( data.pieceTaken );
			}

			board.setPiece( null, data.oldX, data.oldY );
			board.setPiece( data.movedPiece, data.newX, data.newY );
			data.movedPiece.setHasMoved( true );
		}
	}

	public void reverseLastMove ()
	{
		if ( data.isCastling )
		{
			board.setPiece( null, data.movedPiece.getX(), data.movedPiece.getY() );
			board.setPiece( null, data.castlingData.rook.getX(), data.castlingData.rook.getY() );

			board.setPiece( data.movedPiece, data.oldX, data.oldY );
			board.setPiece( data.castlingData.rook, data.castlingData.rookOldX, data.castlingData.rookOldY );
			data.movedPiece.setHasMoved( false );
			data.castlingData.rook.setHasMoved( false );
		}
		else if ( data.isTakingEnPassant )
		{
			board.getPieces().add( data.pieceTaken );

			board.setPiece( data.movedPiece, data.oldX, data.oldY );
			board.setPiece( data.pieceTaken, data.newX, data.oldY );
			data.movedPiece.setHasMoved( data.hadMoved );
		}
		else
		{
			if ( data.pieceTaken != null )
			{
				board.getPieces().add( data.pieceTaken );

				if ( data.pieceTaken.getClass() == King.class )
				{
					board.getPlayersKings().get( data.pieceTaken.getColour() ).add( (King) data.pieceTaken );
				}
			}

			board.setPiece( data.movedPiece, data.oldX, data.oldY );
			board.setPiece( data.pieceTaken, data.newX, data.newY );
			data.movedPiece.setHasMoved( data.hadMoved );
		}
	}

	private class ReversalData
	{
		public final Piece			movedPiece;
		public final Piece			pieceTaken;
		public final int			oldX;
		public final int			oldY;
		public final int			newX;
		public final int			newY;
		public final boolean		hadMoved;
		public final boolean		isCastling;
		public final boolean		isTakingEnPassant;
		public final CastlingData	castlingData;

		public ReversalData( Piece piece, int newX, int newY, boolean isCastling, boolean isTakingEnPassant )
		{
			this.movedPiece = piece;
			this.isCastling = isCastling;
			if ( isCastling )
			{
				this.pieceTaken = null;
				Rook rook = board.getCastlingLogic().getCastlingRookInDirection( (King) piece, newX - piece.getX(), newY - piece.getY() );
				castlingData = new CastlingData( rook );
			}
			else if ( isTakingEnPassant )
			{
				this.pieceTaken = board.getPiece( newX, ( (Pawn) piece ).getY() );
				castlingData = null;
			}
			else
			{
				this.pieceTaken = board.getPiece( newX, newY );
				castlingData = null;
			}
			this.isTakingEnPassant = isTakingEnPassant;
			this.oldX = piece.getX();
			this.oldY = piece.getY();
			this.newX = newX;
			this.newY = newY;
			this.hadMoved = piece.hasMoved();
		}

		private class CastlingData
		{
			public final boolean	kingside;
			public final Rook		rook;
			public final int		rookOldX;
			public final int		rookOldY;

			public CastlingData( Rook rook )
			{
				this.kingside = ( rook.getX() > movedPiece.getX() );
				this.rook = rook;
				this.rookOldX = rook.getX();
				this.rookOldY = rook.getY();
			}

		}
	}
}
