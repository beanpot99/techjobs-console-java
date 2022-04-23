package org.launchcode.techjobs.console;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.*;

/**
 * Created by LaunchCode
 */
public class JobData {

    private static final String DATA_FILE = "resources/job_data.csv";
    private static Boolean isDataLoaded = false;

    private static ArrayList<HashMap<String, String>> allJobs;

    /**
     * Fetch list of all values from loaded data,
     * without duplicates, for a given column.
     *
     * @param field The column to retrieve values from
     * @return List of all of the values of the given field
     */
    public static ArrayList<String> findAll(String field) {

        // load data, if not already loaded
        loadData();

        ArrayList<String> values = new ArrayList<>();

        for (HashMap<String, String> row : allJobs) {
            String aValue = row.get(field);

            if (!values.contains(aValue)) {
                values.add(aValue);
            }
        }

        return values;

    }

    public static ArrayList<HashMap<String, String>> findAll() {

        // load data, if not already loaded
        loadData();

        return allJobs;
    }

    /**
     * Returns results of search the jobs data by key/value, using
     * inclusion of the search term.
     * <p>
     * For example, searching for employer "Enterprise" will include results
     * with "Enterprise Holdings, Inc".
     *
     * @param column Column that should be searched.
     * @param value  Value of teh field to search for
     * @return List of all jobs matching the criteria
     */
    public static ArrayList<HashMap<String, String>> findByColumnAndValue(String column, String value) {
        //column is equal to user input and it outputs the value that corresponds
        // load data, if not already loaded
        loadData();

        ArrayList<HashMap<String, String>> jobs = new ArrayList<>();
        Boolean noResults;
        for (HashMap<String, String> row : allJobs) {

            String aValue = row.get(column);

            if (aValue.contains(value)) {
                jobs.add(row);
            } else {
                noResults = true;
            }
        }
        if (noResults = true) {
            System.out.println("No results found!");
        }
        //System.out.println(jobs);
        return jobs;
    }

    public static Boolean getIsDataLoaded() {
        return isDataLoaded;
    }

    /**
     * Read in data from a CSV file and store it in a list
     */
    private static void loadData() {

        // Only load data once
        if (isDataLoaded) {
            return;
        }

        try {

            // Open the CSV file and set up pull out column header info and records
            Reader in = new FileReader(DATA_FILE);
            CSVParser parser = CSVFormat.RFC4180.withFirstRecordAsHeader().parse(in);
            //says to make a new parser that specifies the format, makes the first line the header and it takes in the new fiel reader as the input to parse
            List<CSVRecord> records = parser.getRecords();
            Integer numberOfColumns = records.get(0).size();
            String[] headers = parser.getHeaderMap().keySet().toArray(new String[numberOfColumns]);
            //array containing each header in CSV [name,employer,location,position type,core competency]

            allJobs = new ArrayList<>();
            //all jobs contains the hashmaps which correspond to each row of csv
            // Put the records into a more friendly format
            for (CSVRecord record : records) {
                HashMap<String, String> newJob = new HashMap<>();
                //puts each row into new hashmap
                for (String headerLabel : headers) {
                    newJob.put(headerLabel, record.get(headerLabel));
                }
                //iterates through header array has key as header label and value as the item in that row)
                allJobs.add(newJob);
            }

            // flag the data as loaded, so we don't do it twice
            isDataLoaded = true;

        } catch (IOException e) {
            System.out.println("Failed to load job data");
            e.printStackTrace();
        }

    }

    public static ArrayList<HashMap<String, String>> findByValue(String searchWord) {
        //returning array lists with hashmaps inside
        loadData();
        ArrayList<HashMap<String, String>> findJobs = new ArrayList<>();
        for(HashMap<String,String> map: allJobs){
            for(String key: map.keySet()){
                String value = map.get(key);
                String[] searchArr = searchWord.split(" \\s");
                String[] valueArr = value.split(" \\s");
                for(int i=0;i<searchArr.length;i++){
                    for(int j=0;j<valueArr.length;j++){
                        if(searchArr[i]==valueArr[j]){
                            if (!findJobs.contains(map)) {
                                findJobs.add(map);
                            }
                        }
                    }
                }

            }
        }

        return findJobs;
    }
//findAll()
}
