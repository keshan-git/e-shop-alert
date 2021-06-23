package sources;

import data.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.configuration.Configuration;
import org.apache.flink.streaming.api.functions.source.RichParallelSourceFunction;
import restclient.EShopClient;
import restclient.response.TitleResponseWrapper;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class ParallelTitleSource extends RichParallelSourceFunction<Title> {
    private static final long DEFAULT_WAIT_TIME_MS = 1000;

    private transient EShopClient client;
    private volatile boolean isRunning = true;
    private int taskIdx = 0;
    @Override
    public void open( Configuration parameters ) throws Exception {
        super.open( parameters );
        client = new EShopClient( );

        int stepSize = this.getRuntimeContext().getNumberOfParallelSubtasks();
        taskIdx = this.getRuntimeContext().getIndexOfThisSubtask();
        log.info( "Init ParallelTitleSource stepSize=" + stepSize + " taskIdx=" + taskIdx );
    }

    @Override
    public void run( SourceContext<Title> sourceContext ) throws Exception {
        while ( isRunning ) {
            log.info( "Loading new set of data from the client, offset=" + taskIdx );
            Optional<TitleResponseWrapper> response = client.loadTitles( 10, taskIdx );

            if ( !response.isPresent( ) ) {
                log.warn( "Title can't be created, waiting and will try again..." );
                try {
                    Thread.sleep( DEFAULT_WAIT_TIME_MS );
                } catch ( InterruptedException ignored ) {

                }
                continue;
            }

            int total = response.get().getTotal();
            int offset = response.get().getOffset();

            List<Title> dataList = response.get( ).getContents( ).stream( ).map( t -> {
                Title title = new Title( );
                title.setId( t.getId( ) );
                title.setName( t.getName( ) );
                return title;
            } ).collect( Collectors.toList( ) );

            dataList.forEach( d -> sourceContext.collect( d ) );
            cancel();
        }
    }

    @Override
    public void cancel( ) {
        isRunning = false;
    }

    @Override
    public void close( ) throws Exception {
        super.close( );
        client.close( );
    }
}
