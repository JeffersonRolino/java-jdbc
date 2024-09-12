package application;

import model.dao.DaoFactory;
import model.dao.SellerDao;
import model.entities.Department;
import model.entities.Seller;

import java.util.List;

public class Program {
    public static void main(String[] args) {
        SellerDao sellerDao = DaoFactory.createSellerDao();

        System.out.println("***** TEST 01: Seller findById() *****");
        Seller seller = sellerDao.findById(3);
        System.out.println(seller);

        System.out.println("\n***** TEST 02: Sellers findByDepartment() *****");
        Department department = new Department(2, "Electronics");
        List<Seller> sellers = sellerDao.findByDepartment(department);
        sellers.forEach(System.out::println);

        System.out.println("\n***** TEST 03: Sellers findAll() *****");
        List<Seller> sellers2 = sellerDao.findAll();
        sellers2.forEach(System.out::println);
    }
}
