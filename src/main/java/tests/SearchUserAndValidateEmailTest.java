package tests;

import common.Headers;
import functions.ValidEmail;
import io.restassured.response.Response;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import java.util.List;

public class SearchUserAndValidateEmailTest {

    SoftAssert softAssert = new SoftAssert();


    @Test(priority = 1, description = "GetUser", alwaysRun = true)
    public void getUser() {

        Response response = Headers.GetHeader("https://jsonplaceholder.typicode.com/users");


       List<String> jsonResponse = response.jsonPath().getList("username");
       List<String> jsonResponse1 = response.jsonPath().getList("id");
       List<String> jsonResponse2 = response.jsonPath().getList("name");
       System.out.println(jsonResponse);
       System.out.println(jsonResponse1);
       System.out.println(jsonResponse2);

        Response response1 = Headers.GetHeader("https://jsonplaceholder.typicode.com/users?id=3");
        softAssert.assertTrue(String.valueOf(response1.getStatusCode()).equals("200"));
        softAssert.assertEquals(response1.getBody().jsonPath().get("username[0]"),"Samantha", "success message not displayed");
        softAssert.assertEquals(response1.getBody().jsonPath().get("name[0]"),"Clementine Bauch", "success message not displayed");
        softAssert.assertAll();


    }


    @Test(priority = 2, description = "getPostsAndCommentsForTheUserAndValidateEmails", alwaysRun = true)
    public void getPostsAndCommentsEmails(){
        Response response = Headers.GetHeader("https://jsonplaceholder.typicode.com/posts/?userId=3");
        softAssert.assertTrue(String.valueOf(response.getStatusCode()).equals("200"));
        List<Integer> ids = response.jsonPath().getList("id");
        System.out.println(ids);
        for (int i=0;i<ids.size();i++){
            Response response1 = Headers.GetHeader("https://jsonplaceholder.typicode.com/comments?postId="+ ids.get(i));
            softAssert.assertTrue(String.valueOf(response1.getStatusCode()).equals("200"));
            List<String> email = response1.jsonPath().getList("email");
            for(int y=0;y<email.size();y++){
            System.out.println(email.get(y));
            System.out.println("Is the above E-mail ID valid? " + ValidEmail.isValid(email.get(y)));}
        }
        softAssert.assertAll();
    }
}
