package controllers;

import io.javalin.http.Context;
import models.JsonResponse;
import models.User;
import services.UserService;


public class UserController {
    private UserService userService;

    public UserController(){
        this.userService = new UserService();
    }

    public UserController(UserService userService){
        this.userService = userService;
    }

    public void createUser(Context context){
        //get user from json string
        User user = context.bodyAsClass(User.class);
        userService.createUser(user);

        JsonResponse jsonResponse = new JsonResponse(true, "user has been created", null);
        context.json(jsonResponse);
    }


}
