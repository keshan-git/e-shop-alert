package sources;

import data.Title;
import org.apache.flink.streaming.api.functions.source.FromIteratorFunction;

import java.io.Serializable;
import java.util.Iterator;

public class TitleSource extends FromIteratorFunction<Title> {

    public TitleSource( ) {
        super( new TitleSource.RateLimitedIterator( new TitleIterator( ) ) );
    }

    private static class RateLimitedIterator<T> implements Iterator<T>, Serializable {
        private static final long serialVersionUID = 1L;
        private final Iterator<T> inner;

        private RateLimitedIterator( Iterator<T> inner ) {
            this.inner = inner;
        }

        public boolean hasNext( ) {
            return this.inner.hasNext( );
        }

        public T next( ) {
            try {
                Thread.sleep( 100L );
            } catch ( InterruptedException var2 ) {
                throw new RuntimeException( var2 );
            }

            return this.inner.next( );
        }
    }
}
