package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;
import java.util.ArrayList;

/**
 * Created by oahmad on 2017-03-06.
 */

public class QueryBuilderTest extends ActivityInstrumentationTestCase2 {

    public QueryBuilderTest() {

        super(MainActivity.class);
    }

    public void testBuildMultiMatch() {

        ArrayList<String> keywords = new ArrayList<String>();
        keywords.add("dog");

        ArrayList<String> fields = new ArrayList<String>();
        fields.add("field1");

        String query = QueryBuilder.buildMultiMatch(keywords, fields);

        String expected = "\"multi_match\": {\n" +
                "\"query\": \"dog\",\n" +
                "\"fields\": [\"field1\"]\n" +
                "}";

        assertEquals(query, expected);

        keywords.add("cat");

        fields.add("field2");
        fields.add("field3");

        query = QueryBuilder.buildMultiMatch(keywords, fields);

        expected = "\"multi_match\": {\n" +
                "\"query\": \"dog&cat\",\n" +
                "\"fields\": [\"field1\", \"field2\", \"field3\"]\n" +
                "}";
    }

    public void testBuildSinceDate() {

        ;
    }
}