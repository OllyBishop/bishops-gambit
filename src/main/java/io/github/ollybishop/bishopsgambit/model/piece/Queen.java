package io.github.ollybishop.bishopsgambit.model.piece;

import java.util.List;

import io.github.ollybishop.bishopsgambit.model.Board;
import io.github.ollybishop.bishopsgambit.model.Player;
import io.github.ollybishop.bishopsgambit.model.Square;
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
    List<Square> getCandidateSquares( Board board, boolean includeFriendlySquares )
    {
        List<Square> bishopCandidateSquares = Bishop.getCandidateSquares( this, board, includeFriendlySquares );
        List<Square> rookCandidateSquares = Rook.getCandidateSquares( this, board, includeFriendlySquares );
        return ListUtils.combine( bishopCandidateSquares, rookCandidateSquares );
    }
}
