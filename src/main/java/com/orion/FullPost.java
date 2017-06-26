package com.orion;

import java.util.ArrayList;
import java.util.Date;

public class FullPost {
    private int postId;
    private String userName;
    private String title;
    private String body;
    private Date timeStamp;
    private ArrayList<Comment> comments;
    private int pokes;

    public FullPost(Post post, ArrayList<Comment> comments, ArrayList<Poke> pokes) {
        userName = post.getUserName();
        title = post.getTitle();
        body = post.getBody();
        timeStamp = post.getTimeStamp();
        postId = post.getId();
        this.pokes = pokes.size();
        this.comments = comments;
    }

    public int numberOfComments() {
        return comments.size();
    }
    public int getPostId() {
        return postId;
    }

    public String getUserName() {
        return userName;
    }

    public String getTitle() {
        return title;
    }

    public String getBody() {
        return body;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public ArrayList<Comment> getComments() {
        return comments;
    }

    public int getPokes() {
        return pokes;
    }
}
