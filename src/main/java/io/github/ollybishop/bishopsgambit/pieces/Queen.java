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
    protected List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> bishopCandidateSquares = Bishop.getCandidateSquares( this, board, includeFriendlySquares );
        List<Square> rookCandidateSquares = Rook.getCandidateSquares( this, board, includeFriendlySquares );
        return ListUtils.combine( bishopCandidateSquares, rookCandidateSquares );
    }
}
