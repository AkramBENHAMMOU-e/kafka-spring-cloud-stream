package com.proj.kafkaspringcloudstream.handlers;

import com.proj.kafkaspringcloudstream.events.PageEvent;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.kstream.Grouped;
import org.apache.kafka.streams.kstream.KStream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;

@Component
public class PageEventHandler {
    @Bean
    public Consumer<PageEvent> pageEventConsumer(){
        return (input) ->{
            System.out.println("*************************");
            System.out.println(input.toString());
            System.out.println("*************************");
        };

    }

    @Bean
    public Supplier<PageEvent> pageEventSupplier(){
        return () -> new PageEvent(Math.random()>0.5?"P11":"P2",Math.random()>0.5?"U1":"U2",new Date(),1000);
    }

    @Bean
    public Function<KStream<String, PageEvent>, KStream<String, Long>> KStreamFunction(){
        return (input) ->
                input.filter((k,v)->v.duration()>100)
                .map((k,v)->new KeyValue<>(v.name(),v.duration()))
                        .groupByKey(Grouped.with(Serdes.String(), Serdes.Long()))
                        .count()
                        .toStream();
    }

}
