package repositories;

import models.GroceryItem;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroceryItemDAOImpl implements GroceryItemDAO{

    @Override
    public List<GroceryItem> getAllItemsGivenListId(Integer listId) {
        List<GroceryItem> items = new ArrayList<>();

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "select * from items where list_id_fk = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);

            ResultSet rs = ps.executeQuery();

            while (rs.next()){
                items.add(new GroceryItem(
                        rs.getInt(1),
                        rs.getString(2),
                        rs.getInt(3),
                        rs.getBoolean(4),
                        rs.getInt(5)
                        ));
            }

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
        return items;
    }

    @Override
    public void markItemInCart(Integer itemId) {

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "update items set item_in_cart = true where item_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);

            ps.executeUpdate();

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void createItemForListWithQuantity(GroceryItem item) {

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "insert into items (item_name, item_quantity, list_id_fk) values (?, ?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, item.getName());
            ps.setInt(2, item.getQuantity());
            ps.setInt(3, item.getListId());
            ;

            ps.executeUpdate();

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void createItemForListWithoutQuantity(GroceryItem item) {

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "insert into items (item_name, list_id_fk) values (?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, item.getName());
            ps.setInt(2, item.getListId());
            ;

            ps.executeUpdate();

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }

    @Override
    public void deleteItemFromList(Integer itemId) {
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "delete from items where item_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, itemId);

            ps.executeUpdate();

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
