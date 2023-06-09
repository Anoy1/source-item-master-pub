package com.anoy.load.item.master.pub.sourceitemmasterpub.config;

import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;
import org.springframework.stereotype.Component;

@Component
public class ProducerPartition implements Partitioner{
	
	private static final int PARTITION_COUNT = 5;

	public int returnPartition(int min, int max) {
		return ThreadLocalRandom.current().nextInt(min,max+1);
	}
	
	@Override
	public void configure(Map<String, ?> configs) {
		
	}

	@Override
	public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
		int keyInt =  Integer.parseInt(key.toString());
		return keyInt % PARTITION_COUNT;
	}

	@Override
	public void close() {
			
	}
	

}
