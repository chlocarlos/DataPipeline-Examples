/*
 * Copyright (c) 2006-2022 North Concepts Inc.  All rights reserved.
 * Proprietary and Confidential.  Use is subject to license terms.
 * 
 * https://northconcepts.com/data-pipeline/licensing/
 */
package com.northconcepts.datapipeline.examples.cookbook;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.northconcepts.datapipeline.core.DataReader;
import com.northconcepts.datapipeline.core.DataWriter;
import com.northconcepts.datapipeline.csv.CSVReader;
import com.northconcepts.datapipeline.job.Job;
import com.northconcepts.datapipeline.memory.MapWriter;

public class WriteKeyValueFieldsToMapWriter {

	public static void main(String[] args) {
        DataReader reader = new CSVReader(new File("example/data/input/countries_with_country-code.csv"))
        		.setFieldNamesInFirstRow(true);
        
        Map<String, String> countryCodeMap = new HashMap<>();
        
        //Create MapWriter, specify key, value and map
        DataWriter writer = new MapWriter("Country", "Country code", countryCodeMap);
        
        Job.run(reader, writer);
        
        for (Map.Entry<String, String> entry : countryCodeMap.entrySet()) {
            System.out.println(entry.getKey() + " --> " + entry.getValue());
        }
	}
}
