package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

/**
 * Get {@link FollowRequest}s from elastic search or add/update {@link FollowRequest}s using
 * elasticsearch.
 *
 * @see ElasticSearch
 */
public class FollowRequestController {

    private ElasticSearch<FollowRequest> elasticSearch =
            new ElasticSearch<FollowRequest>(FollowRequest.class, FollowRequest.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    /**
     * Wait for the last task to finish executing.
     *
     * @throws InterruptedException
     * @throws ExecutionException
     * @throws TimeoutException
     */
    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    /**
     * Return whether the given {@link FollowRequest} relationship exists.
     *
     * @param follower the follower in the {@link FollowRequest} relationship
     * @param followee the followee in the {@link FollowRequest} relationship
     * @return whether the given {@link FollowRequest} relationship exists
     */
    public boolean requestExists(Profile follower, Profile followee) {

        try {

            SearchFilter filter = new SearchFilter()
                    .addFieldValue(new FieldValue("followerId", follower.getId()))
                    .addFieldValue(new FieldValue("followeeId", followee.getId()));

            this.elasticSearch.setFilter(filter);

            ArrayList<FollowRequest> results = this.getFollowRequests(filter, 0);

            if (results.size() > 0) return true;
            return false;
        }

        catch (TimeoutException e) {}
        return false;
    }

    /**
     * Return {@link FollowRequest}s where the given profile is the followee.
     *
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link FollowRequest}s where the given profile is the followee
     * @throws TimeoutException
     */
    public ArrayList<FollowRequest> getFollowRequests(Profile followee, int from)
            throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

        this.elasticSearch.setFilter(filter);

        ArrayList<FollowRequest> results = this.elasticSearch.getNext(0);
        this.elasticSearch.setFilter(null);

        return results;
    }

    /**
     * Return the number of follow requests for the given {@link Profile}.
     *
     * @param followee the {@link Profile} to get the number of follow requests for
     * @return the number of follow requests for the given {@link Profile}
     * @throws TimeoutException
     */
    public Double getFollowRequestCount(Profile followee) throws TimeoutException {

        SearchFilter filter = new SearchFilter().addFieldValue(new FieldValue("followeeId",
                followee.getId()));

        this.elasticSearch.setFilter(filter);

        Double count = this.elasticSearch.getCount();
        this.elasticSearch.setFilter(null);

        return count;
    }

    /**
     * Return the {@link FollowRequest} that has the given ID.
     *
     * <p>
     * Return null if no {@link FollowRequest} has the given ID.
     *
     * @param id the ID of the desired {@link FollowRequest}
     * @return the {@link FollowRequest} that has the given ID
     * @throws TimeoutException
     */
    public FollowRequest getFollowRequestFromID(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    /**
     * Return {@link FollowRequest}s that match the given filter.
     *
     * <p>
     * If filter is null or has no restrictions, return all {@link FollowRequest}s.
     *
     * @param from set to 0 to get the first x number of results, set to x to get the next x number
     *             of results, set to 2x to get the next x number of results after that, and so on
     * @return {@link FollowRequest}s from the elasticsearch index
     * @throws TimeoutException
     */
    public ArrayList<FollowRequest> getFollowRequests(SearchFilter filter, int from)
            throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<FollowRequest> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    /**
     * Return whether the given {@link Follow} relationship exists.
     *
     * @param follower the follower in the {@link Follow} relationship
     * @param followee the followee in the {@link Follow} relationship
     * @return whether the given {@link Follow} relationship exists
     */
    public boolean followRequestExists(Profile follower, Profile followee) {

        SearchFilter filter = new SearchFilter()
                .addFieldValue(new FieldValue("followerId", follower.getId()))
                .addFieldValue(new FieldValue("followeeId", followee.getId()));

        this.elasticSearch.setFilter(filter);

        try {
            FollowRequest result = this.elasticSearch.getSingleResult();
            this.elasticSearch.setFilter(null);
            return result != null;
        }

        catch (TimeoutException e) {
            this.elasticSearch.setFilter(null);
            return false;
        }
    }

    /**
     * Add or update the given {@link FollowRequest}s via elasticsearch.
     *
     * <p>
     * If a {@link FollowRequest} has a null ID, add it. If a
     * {@link FollowRequest} has a non-null ID, update it.
     *
     * @param followRequests the {@link FollowRequest}s to add or update
     */
    public void addOrUpdateFollows(FollowRequest... followRequests) {

        for (FollowRequest followRequest: followRequests) {

            if (!this.followRequestExists(followRequest.getFollower(), followRequest.getFollowee()))
                this.elasticSearch.addOrUpdate(followRequest);
        }
    }

    /**
     * Delete the given {@link FollowRequest}s via elasticsearch.
     *
     * @param followRequests
     */
    public void deleteFollowRequests(FollowRequest... followRequests) {

        this.elasticSearch.delete(followRequests);
    }
}
