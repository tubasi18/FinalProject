package edu.najah.cap.data;

import edu.najah.cap.activity.IUserActivityService;
import edu.najah.cap.activity.UserActivity;
import edu.najah.cap.activity.UserActivityService;
import edu.najah.cap.data.Helpers.DeletedUsernamesTracker;
import edu.najah.cap.data.deletedatafeature.MangerDeletion;
import edu.najah.cap.data.deletedatafeature.strategy.FactoryContext;
import edu.najah.cap.data.exportdatafeature.ExportData;
import edu.najah.cap.data.exportdatafeature.strategy.enumaction.EnumAction;
import edu.najah.cap.exceptions.BadRequestException;
import edu.najah.cap.exceptions.FileFiledException;
import edu.najah.cap.exceptions.SystemBusyException;
import edu.najah.cap.exceptions.Util;
import edu.najah.cap.iam.IUserService;
import edu.najah.cap.iam.UserProfile;
import edu.najah.cap.iam.UserService;
import edu.najah.cap.iam.UserType;
import edu.najah.cap.payment.IPayment;
import edu.najah.cap.payment.PaymentService;
import edu.najah.cap.payment.Transaction;
import edu.najah.cap.posts.IPostService;
import edu.najah.cap.posts.Post;
import edu.najah.cap.posts.PostService;

import java.time.Instant;
import java.util.Map;
import java.util.Scanner;

public class Application {

    private static final IUserActivityService userActivityService = new UserActivityService();
    private static final IPayment paymentService = new PaymentService();
    private static final IUserService userService = new UserService();
    private static final IPostService postService = new PostService();
    private static String loginUserName;

    public static void main(String[] args) {
        generateRandomData();
        Instant start = Instant.now();
        System.out.println("Application Started: " + start);

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your username: ");
        System.out.println("Note: You can use any of the following usernames: user0, user1, user2, user3, .... user99");
        String userName = scanner.nextLine();
        //TODO Your application starts here. Do not Change the existing code
        try {
            Util.validateUserName(userName);
            setLoginUserName(userName);
            UserProfile user = userService.getUser(loginUserName);
            System.out.println(user.getUserType());
//              ExportData exportData = new Exp7ortData(user1, EnumAction.DOWNLOAD_DIRECTLY);
//            exportData.exportData();

            System.out.println( "Activities Before: " + Services.getUserActivityServiceInstance().getUserActivity(loginUserName).size());
            System.out.println( "Posts Before:" + Services.getUserPostServiceInstance().getPosts(loginUserName).size());
            System.out.println( "Transactions Before:" + Services.getUserPaymentServiceInstance().getBalance(loginUserName));
            System.out.println( "Users Before:" + Services.getUserServiceInstance().getUsers().size());


            MangerDeletion mangerDeletion = new MangerDeletion(user,true);
            mangerDeletion.delete();


            System.out.println( "Activities After: " + Services.getUserActivityServiceInstance().getUserActivity(loginUserName).size());
            System.out.println( "Posts After:" + Services.getUserPostServiceInstance().getPosts(loginUserName).size());
            System.out.println( "Transactions After:" + Services.getUserPaymentServiceInstance().getBalance(loginUserName));
            System.out.println( "Users After:" + Services.getUserServiceInstance().getUsers().size());

        } catch (SystemBusyException | BadRequestException  e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }



        //TODO Your application ends here. Do not Change the existing code
        Instant end = Instant.now();
        System.out.println("Application Ended: " + end);
    }


    private static void generateRandomData() {
        for (int i = 0; i < 100; i++) {
            generateUser(i);
            generatePost(i);
            generatePayment(i);
            generateActivity(i);
        }
        System.out.println("Data Generation Completed");
    }

    private static void generateActivity(int i) {
        for (int j = 0; j < 100; j++) {
            try {
                if(UserType.NEW_USER.equals(userService.getUser("user" + i).getUserType())) {
                    continue;
                }
            } catch (Exception e) {
                System.err.println("Error while generating activity for user" + i);
            }
            userActivityService.addUserActivity(new UserActivity("user" + i, "activity" + i + "." + j, Instant.now().toString()));
        }
    }
    private static void generatePayment(int i) {
        for (int j = 0; j < 100; j++) {
            try {
                if (userService.getUser("user" + i).getUserType() == UserType.PREMIUM_USER) {
                    paymentService.pay(new Transaction("user" + i, i * j, "description" + i + "." + j));
                }
            } catch (Exception e) {
                System.err.println("Error while generating post for user" + i);
            }
        }
    }

    private static void generatePost(int i) {
        for (int j = 0; j < 100; j++) {
            postService.addPost(new Post("title" + i + "." + j, "body" + i + "." + j, "user" + i, Instant.now().toString()));
        }
    }

    private static void generateUser(int i) {
        UserProfile user = new UserProfile();
        user.setUserName("user" + i);
        user.setFirstName("first" + i);
        user.setLastName("last" + i);
        user.setPhoneNumber("phone" + i);
        user.setEmail("email" + i);
        user.setPassword("pass" + i);
        user.setRole("role" + i);
        user.setDepartment("department" + i);
        user.setOrganization("organization" + i);
        user.setCountry("country" + i);
        user.setCity("city" + i);
        user.setStreet("street" + i);
        user.setPostalCode("postal" + i);
        user.setBuilding("building" + i);
        user.setUserType(getRandomUserType(i));
        userService.addUser(user);
    }

    private static UserType getRandomUserType(int i) {
        if (i > 0 && i < 3) {
            return UserType.NEW_USER;
        } else if (i > 3 && i < 7) {
            return UserType.REGULAR_USER;
        } else {
            return UserType.PREMIUM_USER;
        }
    }
    private static void setLoginUserName(String loginUserName) {
        Application.loginUserName = loginUserName;
    }
}