import models.GroceryItem;
import models.GroceryList;
import models.User;
import repositories.*;

public class MainDriver {
    public static void main(String[] args) {
        UserDAO userDAO = new UserDAOImpl();
        GroceryListDAO groceryListDAO = new GroceryListDAOImpl();
        GroceryItemDAO groceryItemDAO = new GroceryItemDAOImpl();

        //userDAO.createUser(new User("kev123", "pass123", "Kevin", "Childs"));

        //System.out.println(userDAO.getUserGivenUsername("kev123"));

        //groceryListDAO.createList(new GroceryList("Costco", 10));
        //groceryListDAO.deleteList(9);
        //System.out.println(groceryListDAO.getAllListsGivenUserID(10));

        //groceryItemDAO.createItemForListWithQuantity(new GroceryItem("Banana", 5, 10));

        //groceryItemDAO.createItemForListWithoutQuantity(new GroceryItem("banana", 5, 10));
        //System.out.println(groceryItemDAO.getAllItemsGivenGroceryListId(10));

        //groceryItemDAO.markItemInCart(4);
        //groceryItemDAO.deleteItemFromList(8);
        System.out.println(groceryItemDAO.getAllItemsGivenGroceryListId(10));
    }
}
