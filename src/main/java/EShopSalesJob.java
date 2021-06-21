import data.Title;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

import sink.AlertSink;
import sources.TitleSource;

public class EShopSalesJob {
    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment( );
        DataStream<Title> titles = environment
                .addSource( new TitleSource( ) )
                .name( "titles-on-sale" );

        titles
                .addSink( new AlertSink( ) )
                .name( "send-alerts" );

        environment.execute( "Sales Alert" );
    }
}
