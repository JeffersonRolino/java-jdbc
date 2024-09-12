package application;

import model.dao.DaoFactory;
import model.dao.DepartmentDao;
import model.entities.Department;

import java.util.List;

public class Program2 {
    public static void main(String[] args) {
        DepartmentDao departmentDao = DaoFactory.createDepartmentDao();

        System.out.println("***** TEST 01: Department insert() *****");
        Department department = new Department(null, "Food");
        departmentDao.insert(department);
        System.out.println("Inserted! New Id: " + department.getId());

        System.out.println("***** TEST 02: Department findById() *****");
        Department department2 = departmentDao.findById(8);
        System.out.println(department2);

        System.out.println("\n***** TEST 03: Department update() *****");
        Department departmentToUpdate = departmentDao.findById(6);
        departmentToUpdate.setName("Music");
        departmentDao.update(departmentToUpdate);
        System.out.println("Update completed!");

        System.out.println("\n***** TEST 04: Department deleteById() *****");
        departmentDao.deleteById(7);
        System.out.println("Delete completed!");

        System.out.println("\n***** TEST 05: Department findAll() *****");
        List<Department> departments = departmentDao.findAll();
        departments.forEach(System.out::println);
    }
}
