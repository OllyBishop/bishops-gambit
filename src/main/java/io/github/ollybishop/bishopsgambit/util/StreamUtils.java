package io.github.ollybishop.bishopsgambit.util;

import java.util.List;
import java.util.stream.Stream;

public class StreamUtils
{
    public static <T> T findOnlyOrNull( Stream<T> stream )
    {
        List<T> results = stream.toList();

        return switch ( results.size() )
        {
            case 0 -> null;
            case 1 -> results.get( 0 );
            default -> throw new IllegalStateException( "Expected at most one result, but found " + results.size() );
        };
    }
}
