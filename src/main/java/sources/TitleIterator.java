package sources;

import data.Title;
import lombok.extern.slf4j.Slf4j;
import restclient.EShopClient;
import restclient.response.TitleResponseWrapper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class TitleIterator  implements Iterator<Title>, Serializable {
    private int index = -1;
    private int total = 0;
    private int lastLoadIndex = 0;

    private List<Title> dataList;

    private transient EShopClient client;

    public TitleIterator() {
        this.dataList = new ArrayList<>(  );
        this.client = new EShopClient();
    }

    @Override
    public boolean hasNext() {
            return index < total;
    }

    @Override
    public Title next() {
        index++;
        if ( dataList.size() <= index ) {
            log.info( "Loading new set of data from the client" );
            Optional<TitleResponseWrapper> response = client.loadTitles( 10, ( lastLoadIndex / 10 ) );
            if ( response.isPresent() ) {
                total = response.get().getTotal();
                dataList.addAll( response.get().getContents().stream( ).map( t -> {
                    Title title = new Title();
                    title.setId( t.getId() );
                    title.setName( t.getName() );
                    return title;
                } ).collect( Collectors.toList()) );
            }
        }

        return dataList.get( index );
    }
}
