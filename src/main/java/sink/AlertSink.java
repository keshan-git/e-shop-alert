package sink;

import data.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

import java.time.LocalDateTime;

@PublicEvolving
@Slf4j
public class AlertSink implements SinkFunction<Title> {

    public AlertSink( ) {
    }

    @Override
    public void invoke( Title title, Context context ) {
        String ending = calculateEnding( title.getEndDate() );
        if ( title.getDiscountPercent() >= 75 ) {
            log.info( "[ High Discount (75% or high)      ] [{}] {}", ending, title );
        } else if ( title.getDiscountPercent() >= 50 ) {
            log.info( "[ Medium Discount (75%-50%)        ] [{}] {}", ending, title );
        } else {
            log.info( "[ Regular Discount (less than 50%) ] [{}] {}", ending, title );
        }
    }

    private String calculateEnding ( LocalDateTime endDate ){
        if ( endDate.isBefore( LocalDateTime.now().plusDays( 3 ) )) {
            return "Ending Soon";
        } else {
            return "Ending " + endDate.getMonthValue() + "/" + endDate.getDayOfMonth();
        }
    }
}
