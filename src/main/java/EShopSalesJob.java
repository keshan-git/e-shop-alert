import data.Title;
import mappers.TitlePriceMapFunction;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import sink.AlertSink;
import sources.ParallelTitleSource;

public class EShopSalesJob {
    public static void main( String[] args ) throws Exception {
        StreamExecutionEnvironment environment = StreamExecutionEnvironment.getExecutionEnvironment( );
        DataStream<Title> titles = environment
                .addSource( new ParallelTitleSource( ) )
                .setParallelism( 1 )
                .name( "titles-on-sale" );

        DataStream<Title> pricedTitles = titles
                .map( new TitlePriceMapFunction() )
                .keyBy( title -> title.getEndDate() );
                //.name( "titles-on-sale-prices" );

        pricedTitles.addSink( new AlertSink( ) )
                .name( "send-alerts" );

        environment.execute( "Sales Alert" );
    }
}
