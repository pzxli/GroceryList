package controllers;

import io.javalin.http.Context;
import models.GroceryItem;
import models.JsonResponse;
import services.GroceryItemService;

import java.util.List;

public class GroceryItemController {
    private GroceryItemService groceryItemService;

    public GroceryItemController(){
        this.groceryItemService = new GroceryItemService();
    }

    public GroceryItemController(GroceryItemService groceryItemService){
        this.groceryItemService = groceryItemService;
    }


    public void getAllItemsGivenListId(Context context) {
        try {
            Integer listId = Integer.parseInt(context.queryParam("listId"));
            System.out.println("Fetching grocery items for listId: " + listId);
            List<GroceryItem> items = groceryItemService.getAllGivenListId(listId);
            context.json(new JsonResponse(true, "Items retrieved", items));
        } catch (Exception e) {
            e.printStackTrace();
            context.json(new JsonResponse(false, "Error fetching items: " + e.getMessage(), null));
        }
    }

    public void markItemInCart(Context context) {
        Integer itemId = Integer.parseInt(context.pathParam("itemId"));
        groceryItemService.markItemInCart(itemId);

        context.json(new JsonResponse(true, "item marked in cart if exists", null));
    }

    public void deleteItem(Context context) {
        Integer itemId = Integer.parseInt(context.pathParam("itemId"));
        groceryItemService.deleteItemInCart(itemId);

        context.json(new JsonResponse(true, "item " + itemId + " deleted if exists", null));
    }

    public void createItem(Context context) {
        GroceryItem item = context.bodyAsClass(GroceryItem.class);
        groceryItemService.createItem(item);

        context.json(new JsonResponse(true, "item created", null));
    }
}
