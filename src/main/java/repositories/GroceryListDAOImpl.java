package repositories;

import models.GroceryList;
import util.ConnectionUtil;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class GroceryListDAOImpl implements GroceryListDAO{

    @Override
    public List<GroceryList> getAllListsGivenUserId(Integer userId) {
        List<GroceryList> lists = new ArrayList<>();

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "select * from lists where user_id_fk = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                lists.add(new GroceryList(rs.getInt(1), rs.getString(2), rs.getInt(3)));
            }

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        return lists;
    }

    @Override
    public GroceryList getOneList(Integer listId){
        GroceryList groceryList = null;

        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "select * from lists where list_id = ?";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);

            ResultSet rs = ps.executeQuery();

            while(rs.next()){
                groceryList = new GroceryList(rs.getInt(1), rs.getString(2), rs.getInt(3));
            }

        }catch (SQLException sqle){
            sqle.printStackTrace();
        }

        return groceryList;
    }

    @Override
    public Boolean createList(GroceryList groceryList) {
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "insert into lists(list_name, user_id_fk) values (?, ?);";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, groceryList.getName());
            ps.setInt(2, groceryList.getUserId());

            return ps.executeUpdate() != 0;

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }

        return false;
    }

    @Override
    public void deleteList(Integer listId) {
        try{
            Connection conn = ConnectionUtil.getConnection();
            String sql = "delete from lists where list_id = ?;";

            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, listId);

            ps.executeUpdate();

        } catch (SQLException sqle){
            sqle.printStackTrace();
        }
    }
}
