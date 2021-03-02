package io.github.fire.dragonking.job.runner;

import org.apache.kafka.streams.processor.Processor;
import org.apache.kafka.streams.processor.ProcessorContext;
import org.apache.kafka.streams.state.KeyValueStore;

public class MergeProcess implements Processor<String, String> {

    private ProcessorContext context;
    private KeyValueStore<String, Integer> kvStore;


    @Override
    public void init(ProcessorContext processorContext) {
        this.context = processorContext;
//        this.context.schedule(1000);
        kvStore = (KeyValueStore<String, Integer>) this.context.getStateStore("1");
    }

    @Override
    public void process(String key, String value) {

    }

    @Override
    public void close() {

    }
}
