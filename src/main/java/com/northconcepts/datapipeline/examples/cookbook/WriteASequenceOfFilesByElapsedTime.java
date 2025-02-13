/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.core.Record;
import com.northconcepts.datapipeline.core.SequenceWriter;
import com.northconcepts.datapipeline.core.SequenceWriter.DataWriterFactory;
import com.northconcepts.datapipeline.core.SequenceWriter.ElapsedTimeSequenceStrategy;
import com.northconcepts.datapipeline.core.SequenceWriter.Sequence;
import com.northconcepts.datapipeline.csv.CSVWriter;
import com.northconcepts.datapipeline.job.Job;

public class WriteASequenceOfFilesByElapsedTime {

    private static final int MAX_ORDERS = 10;
    private static final long MAX_TRANSACTIONS = 200;
    private static final int RECORD_DELAY_MILLISECONDS = 250;

    public static void main(String[] args) {
        
        DataReader reader = new FakeMessageQueueReader(MAX_ORDERS, MAX_TRANSACTIONS, RECORD_DELAY_MILLISECONDS);
        
        DataWriter writer = new SequenceWriter(new ElapsedTimeSequenceStrategy(5000), new CsvDataWriterFactory());
        
        Job.run(reader, writer);
    }

    //==================================================
    
    public static class CsvDataWriterFactory implements DataWriterFactory {
        
        @Override
        public DataWriter createDataWriter(Sequence sequence, Record record) {
            return new CSVWriter(new File("example/data/output", "sequence-" + sequence.getIndex() + ".csv"));
        }
    }

    //==================================================
    
    public static class FakeMessageQueueReader extends DataReader {
        
        private final int maxOrders;
        private final long maxTransactions;
        private long nextTransactionId;
        private final long recordDelay;
        
        public FakeMessageQueueReader(int maxOrders, long maxTransactions, long recordDelay) {
            this.maxOrders = maxOrders;
            this.maxTransactions = maxTransactions;
            this.recordDelay = recordDelay;
        }
        
        @Override
        protected Record readImpl() throws Throwable {
            if (nextTransactionId >= maxTransactions) {
                return null;
            }
            
            if (recordDelay > 0) {
                Thread.sleep(recordDelay);
            }
            
            Record record = new Record();
            record.setField("transaction_id", nextTransactionId++);
            record.setField("order_id", "order" + nextTransactionId % maxOrders);
            record.setField("amount", nextTransactionId + 0.01);
            return record;
        }
        
    }

}
