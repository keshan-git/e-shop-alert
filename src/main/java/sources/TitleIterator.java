package sources;

import data.Title;

import java.io.Serializable;
import java.util.Iterator;

public class TitleIterator  implements Iterator<Title>, Serializable {
    private int index = 0;

    public TitleIterator() {

    }

    @Override
    public boolean hasNext() {
            return true;
    }

    @Override
    public Title next() {
        Title title = new Title( index++ );
        return title;
    }
}
