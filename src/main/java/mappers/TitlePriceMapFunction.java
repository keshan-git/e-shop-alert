package mappers;

import data.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.api.common.functions.RichMapFunction;
import org.apache.flink.configuration.Configuration;
import restclient.EShopClient;
import restclient.response.PriceResponse;
import restclient.response.PriceResponseWrapper;

import java.util.Optional;

@Slf4j
public class TitlePriceMapFunction extends RichMapFunction<Title, Title> {
    private transient EShopClient client;

    @Override
    public Title map( Title title ) throws Exception {
        log.info( "Loading price information for the title, title_id=" + title.getId( ) );
        Optional<PriceResponseWrapper> response = client.loadPrice( title.getId( ) );

        if ( !response.isPresent( ) ) {
            log.warn( "A transaction could not be created, waiting and will try again..." );
            return title;
        }

        PriceResponse price = response.get( ).getPrices( ).get( 0 );
        title.setCurrency( price.getRegular_price( ).getCurrency( ) );
        title.setRegularPrice( price.getRegular_price( ).getRaw_value( ) );
        title.setDiscountPrice( price.getDiscount_price( ).getRaw_value( ) );
        title.setEndDate( price.getDiscount_price( ).getEnd_datetime( ) );

        title.setDiscountAmount( round( title.getRegularPrice( ) - title.getDiscountPrice( ), 2 ) );
        title.setDiscountPercent( ( int ) Math.round( ( title.getDiscountAmount( ) * 100.0 ) / title.getRegularPrice( ) ) );

        return title;
    }

    @Override
    public void open( Configuration parameters ) throws Exception {
        super.open( parameters );
        client = new EShopClient( );

        log.info( "Init TitlePriceMapFunction" );
    }

    @Override
    public void close( ) throws Exception {
        super.close( );
        client.close( );
    }

    private double round( double value, int decimals ) {
        return Math.round( value * Math.pow( 10, decimals ) ) / ( Math.pow( 10, decimals ) * 1.0 );
    }

}
