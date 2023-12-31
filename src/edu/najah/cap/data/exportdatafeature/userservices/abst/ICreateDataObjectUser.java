package edu.najah.cap.data.exportdatafeature.userservices.abst;

import edu.najah.cap.data.helpers.Services;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.NotFoundException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public abstract class ICreateDataObjectUser {
    String userName;
    IUserService userService;
    IPostService postService;

    private static final Logger logger = LogManager.getLogger(ICreateDataObjectUser.class);
    protected ICreateDataObjectUser(String userName, IUserService userService, IPostService postService) {
        this.userName = userName;
        this.userService = userService;
        this.postService = postService;
    }

    public UserProfile getUserProfile() throws SystemBusyException, NotFoundException, BadRequestException {
        return Services.getUserServiceInstance().getUser(this.userName);
    }

    public IPostService getPostService() {
        return postService;
    }


    public String getPostsDetails() throws SystemBusyException, BadRequestException, NotFoundException {
        List<Post> posts = getPostService()
                .getPosts(getUserProfile()
                        .getUserName());

        StringBuilder result = new StringBuilder();
        for (Post post : posts) {
            result.append("Post ID: ").append(post.getId()).append("\n")
                    .append("Title: ").append(post.getTitle()).append("\n")
                    .append("Body: ").append(post.getBody()).append("\n\n");
        }
        logger.info(String.format("Data PostsDetails generated Successfully for user:  %s", getUserName()));
        return result.toString();
    }

    public String getDataProfile() throws SystemBusyException, NotFoundException, BadRequestException {
        logger.info(String.format("Profile data generated Successfully for user:  %s", getUserName()));
        return "Name: " + getUserProfile().getUserName()
                + " " + getUserProfile().getLastName()
                + "\n" + "City: " + getUserProfile().getCity() + "\n";
    }

    public String getUserName() throws SystemBusyException, NotFoundException, BadRequestException {
        return getUserProfile().getUserName();
    }

    public abstract String getDataUser() throws SystemBusyException, BadRequestException, NotFoundException;
}
