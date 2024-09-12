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
    public void insert(Seller seller) {
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(
            "INSERT INTO seller " +
                "(Name, Email, BirthDate, BaseSalary, DepartmentId) " +
                "VALUES " +
                "(?, ?, ?, ?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS
            );

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());

            int rowAffected = preparedStatement.executeUpdate();

            if(rowAffected > 0){
                ResultSet resultSet = preparedStatement.getGeneratedKeys();
                if(resultSet.next()){
                    int id = resultSet.getInt(1);
                    seller.setId(id);
                }
                DB.closeResultSet(resultSet);
            }
            else {
                throw new DbException("Unexpected error! No rows affected!");
            }
        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
    }

    @Override
    public void update(Seller seller) {
        PreparedStatement preparedStatement = null;

        try{
            preparedStatement = connection.prepareStatement(
                    "UPDATE seller " +
                        "SET Name = ?, Email = ?, BirthDate = ?, BaseSalary = ?, DepartmentId = ? " +
                        "WHERE Id = ?"
            );

            preparedStatement.setString(1, seller.getName());
            preparedStatement.setString(2, seller.getEmail());
            preparedStatement.setDate(3, new java.sql.Date(seller.getBirthDate().getTime()));
            preparedStatement.setDouble(4, seller.getBaseSalary());
            preparedStatement.setInt(5, seller.getDepartment().getId());
            preparedStatement.setInt(6, seller.getId());

            preparedStatement.executeUpdate();

        } catch (SQLException e) {
            throw new DbException(e.getMessage());
        }
        finally {
            DB.closeStatement(preparedStatement);
        }
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
        PreparedStatement preparedStatement = null;
        ResultSet resultset = null;

        try {
            preparedStatement = connection.prepareStatement(
                    "SELECT seller.*,department.Name as DepName " +
                            "FROM seller INNER JOIN department " +
                            "ON seller.DepartmentId = department.Id " +
                            "ORDER BY Name"
            );

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
