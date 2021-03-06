package com.csahmad.moodcloud;

import android.test.ActivityInstrumentationTestCase2;

import java.util.ArrayList;
import java.util.GregorianCalendar;

/**
 * Created by oahmad on 2017-03-09.
 */

public class ElasticSearchTest extends ActivityInstrumentationTestCase2 {

    private static final int timeout = 5_000;

    public ElasticSearchTest() {

        super(MainActivity.class);
    }

    // FIXME: 2017-03-11 Failing
    public void testDeleteAll() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();

        TestElasticSearchObject object1 = new TestElasticSearchObject();
        TestElasticSearchObject object2 = new TestElasticSearchObject();
        TestElasticSearchObject object3 = new TestElasticSearchObject();

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        elasticSearch.addOrUpdate(object2);
        elasticSearch.waitForTask();
        elasticSearch.addOrUpdate(object3);
        elasticSearch.waitForTask();

        ArrayList<TestElasticSearchObject> results = elasticSearch.getNext(0);
        assertTrue("Actual size: " + results.size(), results.size() >= 3);

        elasticSearch.deleteAll();
        results = elasticSearch.getNext(0);
        assertEquals(results.size(), 0);

        // Passes up to here, but these null checks fail (but testDelete() works fine)
        assertNull(object1.getId());
        assertNull(object2.getId());
        assertNull(object3.getId());
    }

    public void testConstructor() {

        new ElasticSearch<TestElasticSearchObject>(TestElasticSearchObject.class,
                TestElasticSearchObject.typeName);

        new ElasticSearch<Account>(Account.class, Account.typeName);
        new ElasticSearch<Profile>(Profile.class, Profile.typeName);

        boolean exceptionThrown = false;

        try {
            new ElasticSearch<Account>(null, Account.typeName);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            new ElasticSearch<Account>(Account.class, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            new ElasticSearch<Account>(null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testAddOrUpdate() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();
        elasticSearch.deleteAll();

        elasticSearch.addOrUpdate();
        elasticSearch.waitForTask();

        TestElasticSearchObject object1 = new TestElasticSearchObject();
        assertNull(object1.getId());

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        assertNotNull(object1.getId());

        String id1 = object1.getId();

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        assertNull(object2.getId());

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertNotNull(object2.getId());

        String id2 = object2.getId();

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        String message = "Don't break my heart, Mickey";
        object1.setMessage(message);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        SimpleLocation location = new SimpleLocation(1.2d, -0.8d, 0.0d);
        object2.setLocation(location);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        message = "We like to party";
        object2.setMessage(message);

        elasticSearch.addOrUpdate(object1, object2);
        elasticSearch.waitForTask();
        assertEquals(object1.getId(), id1);
        assertEquals(object2.getId(), id2);

        boolean exceptionThrown = false;

        try {
            elasticSearch.addOrUpdate(null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            elasticSearch.addOrUpdate(null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testGetById() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();
        elasticSearch.deleteAll();

        // Should be no chance of this ID existing if above call to deleteAll works properly
        TestElasticSearchObject returned = elasticSearch.getById("ThisIdDoesNotExist");
        assertNull(returned);

        TestElasticSearchObject object1 = new TestElasticSearchObject();

        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();

        String id = object1.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object1, returned);

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        object2.setMessage(
                "He's my best friend / Best of all best friends / Will you be my best friend too?");

        elasticSearch.addOrUpdate(object2);
        elasticSearch.waitForTask();

        id = object2.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object2, returned);

        TestElasticSearchObject object3 = new TestElasticSearchObject();
        object3.setMessage("Change Leopardon! Yeah yeah yeeeaah, WOW!");
        object3.setMood("Happy");
        object3.setDate(new GregorianCalendar(1994, 8, 26));
        object3.setLocation(new SimpleLocation(2.8d, 32.5d, 0.0d));

        elasticSearch.addOrUpdate(object3);
        elasticSearch.waitForTask();

        id = object3.getId();
        returned = elasticSearch.getById(id);
        assertNotNull(returned);
        assertEquals(object3, returned);

        boolean exceptionThrown = false;

        try {
            elasticSearch.getById(null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    public void testDelete() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();
        elasticSearch.deleteAll();

        elasticSearch.delete();

        TestElasticSearchObject object1 = new TestElasticSearchObject();
        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();

        String id = object1.getId();
        elasticSearch.delete(object1);
        elasticSearch.waitForTask();
        assertNull(elasticSearch.getById(id));
        assertNull(object1.getId());

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        object2.setMessage("Have you ever seen a unicorn in the dawn / Shining creature of light");
        elasticSearch.addOrUpdate(object2);
        elasticSearch.waitForTask();

        id = object2.getId();
        elasticSearch.delete(object2);
        elasticSearch.waitForTask();
        assertNull(elasticSearch.getById(id));
        assertNull(object2.getId());

        TestElasticSearchObject object3 = new TestElasticSearchObject();
        object3.setMessage("Have you ever seen a unicorn in the dawn / Shining creature of light");
        object3.setMood("Sad");
        object3.setDate(new GregorianCalendar(2017, 3, 10));
        object3.setLocation(new SimpleLocation(54.4d, 76.3d, 4.8d));
        elasticSearch.addOrUpdate(object3);
        elasticSearch.waitForTask();

        id = object3.getId();
        elasticSearch.delete(object3);
        elasticSearch.waitForTask();
        assertNull(elasticSearch.getById(id));
        assertNull(object3.getId());

        elasticSearch.addOrUpdate(object1, object2, object3);
        elasticSearch.waitForTask();

        String id1 = object1.getId();
        String id2 = object2.getId();
        String id3 = object3.getId();
        elasticSearch.delete(object1, object2, object3);
        elasticSearch.waitForTask();

        assertNull(elasticSearch.getById(id1));
        assertNull(object1.getId());
        assertNull(elasticSearch.getById(id2));
        assertNull(object2.getId());
        assertNull(elasticSearch.getById(id3));
        assertNull(object3.getId());

        boolean exceptionThrown = false;

        try {
            elasticSearch.delete(object1);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            elasticSearch.delete(object2, object3);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            elasticSearch.delete(null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
        exceptionThrown = false;

        try {
            elasticSearch.delete(null, null);
        }

        catch (IllegalArgumentException e) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    // TODO: 2017-03-11 Fix deleteAll so this test passes without having to use curl first
    // TODO: 2017-03-11 Finish
    public void testGetNextNoFilter() throws Exception {

        ElasticSearch<TestElasticSearchObject> elasticSearch = ElasticSearchTest.getElasticSearch();
        elasticSearch.deleteAll();

        ArrayList<TestElasticSearchObject> results = elasticSearch.getNext(0);
        assertEquals(results.size(), 0);

        TestElasticSearchObject object1 = new TestElasticSearchObject();
        elasticSearch.addOrUpdate(object1);
        elasticSearch.waitForTask();
        assertEquals(elasticSearch.getById(object1.getId()), object1);

        results = elasticSearch.getNext(0);
        assertEquals(results.size(), 1);
        assertTrue(results.contains(object1));

        TestElasticSearchObject object2 = new TestElasticSearchObject();
        object2.setMessage("Should you ever see a unicorn in the dawn you will never forget ");
        elasticSearch.addOrUpdate(object2);
        elasticSearch.waitForTask();
        assertEquals(elasticSearch.getById(object1.getId()), object1);
        assertEquals(elasticSearch.getById(object2.getId()), object2);

        results = elasticSearch.getNext(0);
        assertEquals(results.size(), 2);
        assertTrue(results.contains(object1));
        assertTrue(results.contains(object2));

        TestElasticSearchObject object3 = new TestElasticSearchObject();
        object3.setMessage("the vision of liiiiiiiiight!");
        elasticSearch.addOrUpdate(object3);
        elasticSearch.waitForTask();
        assertEquals(elasticSearch.getById(object1.getId()), object1);
        assertEquals(elasticSearch.getById(object2.getId()), object2);
        assertEquals(elasticSearch.getById(object3.getId()), object3);

        results = elasticSearch.getNext(0);
        assertEquals(results.size(), 3);
        assertTrue(results.contains(object1));
        assertTrue(results.contains(object2));
        assertTrue(results.contains(object3));

        TestElasticSearchObject object4 = new TestElasticSearchObject();
        object4.setMessage("Thor / Giants hide in fear / As they feel thy hammer near");
        object4.setMood("angry");
        object4.setDate(new GregorianCalendar(3000, 8, 26));
        object4.setLocation(new SimpleLocation(0.0d, 0.0d, 0.0d));
        elasticSearch.addOrUpdate(object4);
        elasticSearch.waitForTask();
        assertEquals(elasticSearch.getById(object1.getId()), object1);
        assertEquals(elasticSearch.getById(object2.getId()), object2);
        assertEquals(elasticSearch.getById(object3.getId()), object3);
        assertEquals(elasticSearch.getById(object4.getId()), object4);

        results = elasticSearch.getNext(0);
        assertEquals(results.size(), 4);
        assertTrue(results.contains(object1));
        assertTrue(results.contains(object2));
        assertTrue(results.contains(object3));
        assertTrue(results.contains(object4));

        ;
    }

    // TODO: 2017-03-11 Fill
    public void testGetNextWithFilter() throws Exception {

        ;
    }

    private static ElasticSearch<TestElasticSearchObject> getElasticSearch() {

        ElasticSearch<TestElasticSearchObject> elasticSearch =
                new ElasticSearch<TestElasticSearchObject>(TestElasticSearchObject.class,
                        TestElasticSearchObject.typeName);

        elasticSearch.setTimeout(ElasticSearchTest.timeout);

        return elasticSearch;
    }
}

/*
Curl:

Get all test objects:
curl -XGET \
'http://cmput301.softwareprocess.es:8080/cmput301w17t13/testElasticSearchObject/_search?pretty=1'

Delete all test objects:
curl -XDELETE \
'http://cmput301.softwareprocess.es:8080/cmput301w17t13/testElasticSearchObject/_query' -d '{
    "query" : {
        "match_all" : {}
    }
}'
 */