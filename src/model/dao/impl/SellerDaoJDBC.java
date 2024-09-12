package model.dao.impl;

import db.DB;
import db.DbException;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SellerDaoJDBC implements SellerDao {

    private Connection connection;

    public SellerDaoJDBC() {
    }

    public SellerDaoJDBC(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void insert(Seller department) {

    }

    @Override
    public void update(Seller department) {

    }

    @Override
    public void deleteById(Integer id) {

    }

    @Override
    public Seller findById(Integer id) {
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;

        try {
            preparedStatement = connection.prepareStatement(
            "SELECT seller.*,department.Name as DepName " +
                "FROM seller INNER JOIN department " +
                "ON seller.DepartmentId = department.Id " +
                "WHERE seller.Id = ?"
            );

            preparedStatement.setInt(1, id);
            resultset = preparedStatement.executeQuery();

            if(resultset.next()){
                Department department = instantiateDepartment(resultset);

                Seller seller = instantiateSeller(resultset, department);

                return seller;
            }
            return null;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultset);
        }
    }

    @Override
    public List<Seller> findAll() {
        return List.of();
    }

    @Override
    public List<Seller> findByDepartment(Department department) {
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                        "FROM seller INNER JOIN department " +
                        "ON seller.DepartmentId = department.Id " +
                        "WHERE DepartmentId = ? " +
                        "ORDER BY Name"
            );

            preparedStatement.setInt(1, department.getId());
            resultset = preparedStatement.executeQuery();

            List<Seller> sellers = new ArrayList<>();
            Map<Integer, Department> map = new HashMap<>();

            while (resultset.next()){

                Department dep = map.get(resultset.getInt("DepartmentId"));

                if(dep == null){
                    dep = instantiateDepartment(resultset);
                    map.put(resultset.getInt("DepartmentId"), dep);
                }

                Seller seller = instantiateSeller(resultset, dep);
                sellers.add(seller);
            }
            return sellers;

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
            DB.closeResultSet(resultset);
        }
    }


    private Seller instantiateSeller(ResultSet resultset, Department department) throws SQLException {
        Seller seller = new Seller();

        seller.setId(resultset.getInt("Id"));
        seller.setName(resultset.getString("Name"));
        seller.setEmail(resultset.getString("Email"));
        seller.setBirthDate(resultset.getDate("BirthDate"));
        seller.setBaseSalary(resultset.getDouble("BaseSalary"));
        seller.setDepartment(department);

        return seller;
    }

    private Department instantiateDepartment(ResultSet resultset) throws SQLException {
        Department department = new Department();

        department.setId(resultset.getInt("DepartmentId"));
        department.setName(resultset.getString("DepName"));

        return department;
    }
}
