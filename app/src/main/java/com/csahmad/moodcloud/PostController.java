package com.csahmad.moodcloud;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeoutException;

/**
 * Created by oahmad on 2017-03-01.
 */

public class PostController {

    private ElasticSearch<Post> elasticSearch =
            new ElasticSearch<Post>(Post.class, Post.typeName);

    public Integer getTimeout() {

        return this.elasticSearch.getTimeout();
    }

    public void setTimeout(Integer timeout) {

        this.elasticSearch.setTimeout(timeout);
    }

    // Note: stores post in LocoalData.homeProfile AND sends the post to the internets
    public void addOrUpdatePosts(Post... posts) {

        this.elasticSearch.addOrUpdate(posts);
    }

    public void waitForTask() throws InterruptedException, ExecutionException, TimeoutException {

        this.elasticSearch.waitForTask();
    }

    public Post getPostFromId(String id) throws TimeoutException {

        return this.elasticSearch.getById(id);
    }

    public ArrayList<Post> getPosts(SearchFilter filter, int from) throws TimeoutException {

        this.elasticSearch.setFilter(filter);
        ArrayList<Post> result = this.elasticSearch.getNext(from);
        this.elasticSearch.setFilter(null);
        return result;
    }

    // Note: latest posts only
    // TODO: 2017-03-12 Find better way
    public ArrayList<Post> getFolloweePosts(Profile follower,
                                            SearchFilter filter, int from) throws TimeoutException {

        ProfileController controller = new ProfileController();
        return PostController.getLatestPosts(controller.getFollowees(follower, from), filter);
    }

    // Note: latest posts only
    // TODO: 2017-03-12 Find better way
    public static ArrayList<Post> getFollowerPosts(Profile followee, SearchFilter filter,
                                                   int from) throws TimeoutException {

        ProfileController controller = new ProfileController();
        return PostController.getLatestPosts(controller.getFollowers(followee, from), filter);
    }

    public static ArrayList<Post> getLatestPosts(ArrayList<Profile> profiles, SearchFilter filter) {

        ArrayList<Post> latestPosts = new ArrayList<Post>();
        Post post;

        for (Profile profile: profiles) {
            post = PostController.getLatestPost(profile.getPosts(), filter);
            if (post != null) latestPosts.add(post);
        }

        return latestPosts;
    }

    // TODO: 2017-03-11 Ignores filter
    public static Post getLatestPost(ArrayList<Post> posts, SearchFilter filter) {

        if (posts == null)
            throw new IllegalArgumentException("Cannot pass null.");

        if (posts.size() == 0)
            return null;

        Post post;
        Calendar postDate;

        Post latestPost = posts.get(0);
        Calendar latestPostDate;

        for (int i = 0; i < posts.size(); i++) {

            post = posts.get(i);
            postDate = post.getDate();
            latestPostDate = latestPost.getDate();

            // If any posts at same time, assume post that appears later in list is newer
            if (postDate.compareTo(latestPostDate) >= 0)
                latestPost = post;
        }

        return latestPost;
    }

    public void deletePosts(Post... posts) {

        this.elasticSearch.delete(posts);
    }
}