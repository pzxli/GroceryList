package repositories;

import models.GroceryList;

import java.util.List;

public interface GroceryListDAO {
    List<GroceryList> getAllListsGivenUserId(Integer userId);
    GroceryList getOneList(Integer listId);
    Boolean createList(GroceryList groceryList);
    void deleteList(Integer listId);
}
