import controllers.GroceryItemController;
import controllers.GroceryListController;
import controllers.SessionController;
import controllers.UserController;
import io.javalin.Javalin;
import io.javalin.http.staticfiles.Location;


import static io.javalin.apibuilder.ApiBuilder.*;

public class MainDriver {
    public static void main(String[] args) {
        Javalin app = Javalin.create(config -> {
            config.addStaticFiles("/public", Location.CLASSPATH);
        }).start(9001);

        UserController userController = new UserController();
        GroceryListController groceryListController = new GroceryListController();
        GroceryItemController groceryItemController = new GroceryItemController();
        SessionController sessionController = new SessionController();


        app.routes(() ->{
            path("list", () -> {
                get(groceryListController:: displayAllListForUser);
                post(groceryListController::createList);
                path("{listId}", () -> {
                   delete(groceryListController::deleteList);
                });
            });

            path("item", () ->{
               get(groceryItemController::getAllItemsGivenListId);
               post(groceryItemController::createItem);
               path("{itemId}", () -> {
                   patch(groceryItemController::markItemInCart);
                   delete(groceryItemController::deleteItem);
               });
            });

            path("session", () -> {
                post(sessionController::login);
                get(sessionController::checkSession);
                delete(sessionController::logout);
            });

            post("/user", userController::createUser);
        });
    }
}
