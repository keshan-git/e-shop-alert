package sink;

import data.Title;
import lombok.extern.slf4j.Slf4j;
import org.apache.flink.annotation.PublicEvolving;
import org.apache.flink.streaming.api.functions.sink.SinkFunction;

@PublicEvolving
@Slf4j
public class AlertSink implements SinkFunction<Title> {

    public AlertSink( ) {
    }

    @Override
    public void invoke( Title value, Context context ) {
        log.info( value.toString( ) );
    }
}
