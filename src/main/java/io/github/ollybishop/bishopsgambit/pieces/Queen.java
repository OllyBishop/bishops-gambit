package io.github.ollybishop.bishopsgambit.pieces;

import java.util.List;

import io.github.ollybishop.bishopsgambit.board.Board;
import io.github.ollybishop.bishopsgambit.board.Square;
import io.github.ollybishop.bishopsgambit.player.Player;
import io.github.ollybishop.bishopsgambit.util.ListUtils;

public class Queen extends Piece
{
    public Queen( Player player, char startFile, char startRank )
    {
        super( player, startFile, startRank );
    }

    @Override
    public Type getType()
    {
        return Type.QUEEN;
    }

    @Override
    public int getValue()
    {
        return 9;
    }

    @Override
    public List<Square> getPseudoLegalMoves( Board board )
    {
        List<Square> bishopMoves = Bishop.getPseudoLegalMoves( board, this );
        List<Square> rookMoves = Rook.getPseudoLegalMoves( board, this );
        return ListUtils.combine( bishopMoves, rookMoves );
    }
}
