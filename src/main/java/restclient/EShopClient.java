package restclient;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import lombok.extern.slf4j.Slf4j;
import restclient.response.PriceResponseWrapper;
import restclient.response.TitleResponseWrapper;

import java.util.ArrayList;
import java.util.Optional;


@Slf4j
public class EShopClient {
    public Optional<TitleResponseWrapper> loadTitlesMock( int count, int offset ) {
        log.info( "Requesting title on sales count-" + count + " offset-" + offset );

        TitleResponseWrapper w = new TitleResponseWrapper();
        w.setTotal( 50 );
        w.setOffset( offset );
        w.setContents( new ArrayList<>(  ) );
        return Optional.of( w );
    }

    public Optional<TitleResponseWrapper> loadTitles( int count, int offset ) {
        //https://ec.nintendo.com/api/NL/nl/search/sales?count=10&offset=0
        log.info( "Requesting title on sales count-" + count + " offset-" + offset );
        HttpResponse<TitleResponseWrapper> response = Unirest.get( "https://ec.nintendo.com/api/NL/nl/search/sales" )
                .header( "accept", "application/json" )
                .queryString( "count", count )
                .queryString( "offset", offset )
                .asObject( TitleResponseWrapper.class );

        if ( response.isSuccess( ) ) {
            TitleResponseWrapper body = response.getBody( );
            log.debug( body.toString( ) );
            return Optional.of( body );
        } else {
            log.error( "Unable to extract data from the eshop api", response.getStatus( ) + ":" + response.getStatusText( ) );
            if ( response.getParsingError( ).isPresent( ) ) {
                log.error( "Fail to parse data - " + response.getParsingError( ).get( ).getMessage( ) + ":" +
                        response.getParsingError( ).get( ).getCause( ) );
                log.debug( "Original Response - " + response.getParsingError( ).get( ).getOriginalBody( ) );
            }
        }
        return Optional.empty( );
    }

    public void close( ) {

    }

    public Optional<PriceResponseWrapper> loadPrice( long id ) {
        //https://api.ec.nintendo.com/v1/price?country=NL&lang=en&ids=70010000000818
        log.info( "Requesting title price for the id=" + id );
        HttpResponse<PriceResponseWrapper> response = Unirest.get( "https://api.ec.nintendo.com/v1/price" )
                .header( "accept", "application/json" )
                .queryString( "country", "NL" )
                .queryString( "lang", "en" )
                .queryString( "ids", id )
                .asObject( PriceResponseWrapper.class );

        if ( response.isSuccess( ) ) {
            PriceResponseWrapper body = response.getBody( );
            log.debug( body.toString( ) );
            return Optional.of( body );
        } else {
            log.error( "Unable to extract data from the eshop api", response.getStatus( ) + ":" + response.getStatusText( ) );
            if ( response.getParsingError( ).isPresent( ) ) {
                log.error( "Fail to parse data - " + response.getParsingError( ).get( ).getMessage( ) + ":" +
                        response.getParsingError( ).get( ).getCause( ) );
                log.debug( "Original Response - " + response.getParsingError( ).get( ).getOriginalBody( ) );
            }
        }
        return Optional.empty( );
    }
}
