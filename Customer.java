package postgres;

import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import static postgres.EncodeUtils.encodeString;

public class Customer {

    private String id;
    private String userName;
    private String password;
    private String email;
    private String phone;
    private String name;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Customer(){}

    @Override
    public String toString() {
        return "Customer{" +
                "id='" + id + '\'' +
                ", userName='" + userName + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", name='" + name + '\'' +
                '}';
    }

    public static void main(String[] args) {
        CustomerDALImpl customerDAL = new CustomerDALImpl();
        /*ArrayList<Customer> customersList = new ArrayList<>();
        Customer customer1 = new Customer();
        customer1.setName("Hari krishna K");
        customer1.setEmail("kilari.hari@gmail.com");
        customer1.setUserName("kilari.hari");
        customer1.setPassword("KHK@2021");
        customer1.setPhone("9642411961");
        customersList.add(customer1);
        Customer customer = new Customer();
        customer.setName("Dummy Kilari");
        customer.setEmail("kilari.dummy@gmail.com");
        customer.setUserName("kilari.dummy");
        customer.setPassword("KD@2021");
        customer.setPhone("9642411961");
        customersList.add(customer);


        customerDAL.createCustomerSchema();

        customerDAL.createCustomers(customersList);

        ArrayList<Customer> customers = customerDAL.getCustomers();
        for(Customer cust : customers) {
            System.out.println(cust.toString());
        }



        //ArrayList<String> userNames = new ArrayList<>();
        //userNames.add("kilari.hari");

        ArrayList<String> userNames = new ArrayList<>(customersList.stream().map(customer2 -> customer2.getUserName()).toList());
        int deletedCount = customerDAL.deleteCustomersByUserName(userNames);
        System.out.println("Delected Count: "+ deletedCount);
        System.out.println("**** After delete******");
        customers = customerDAL.getCustomers();
        for(Customer cust : customers) {
            System.out.println(cust.toString());
        }*/

        Scanner scanner = new Scanner(System.in);
        //CustomerDALImpl customerDAL1 = new CustomerDALImpl();
        String choice;

        do {
            System.out.println("*************************************");
            System.out.println("Menu:");
            System.out.println("1. Retrieve customer details by ID");
            System.out.println("2. Insert new customer");
            System.out.println("3. Update customer's email by ID");
            System.out.println("4. Delete Customer");
            System.out.println("5. List Customers");
            System.out.println("6. Exit");
            System.out.println("*************************************");
            System.out.print("Enter your choice: ");

            choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Enter customer ID: ");
                    String customerId = scanner.nextLine();
                    Customer customerById = customerDAL.getCustomerById(Integer.parseInt(customerId));
                    if (customerById != null) {
                        System.out.println("Customer details:");
                        System.out.println(customerById.toString());
                    } else {
                        System.out.println("Customer not found with the given ID: " + customerId);
                    }
                    break;

                case "2":
                    System.out.println("Enter customer details:");
                    Customer newCustomer = new Customer();
                    System.out.print("Name: ");
                    newCustomer.setName(scanner.nextLine());
                    System.out.print("Email: ");
                    newCustomer.setEmail(scanner.nextLine());
                    System.out.print("Username: ");
                    newCustomer.setUserName(scanner.nextLine());
                    System.out.print("Password: ");
                    newCustomer.setPassword(scanner.nextLine());
                    System.out.print("Phone: ");
                    newCustomer.setPhone(scanner.nextLine());

                    customerDAL.createCustomerSchema();
                    customerDAL.createCustomer(newCustomer);
                    break;

                case "3":
                    System.out.print("Enter customer ID to update: ");
                    String customerIdToUpdate = scanner.nextLine();
                    System.out.print("Enter new email: ");
                    String newEmail = scanner.nextLine();
                    boolean updated = customerDAL.updateCustomerEmail(Integer.parseInt(customerIdToUpdate), newEmail);
                    if (updated) {
                        System.out.println("Email for customer with ID " + customerIdToUpdate + " updated successfully.");
                        System.out.println("Updated Customer Details: ");
                        System.out.println(customerDAL.getCustomerById(Integer.parseInt(customerIdToUpdate)).toString());
                    } else {
                        System.out.println("No customer found with the given ID.");
                    }
                    break;

                case "4":
                    System.out.print("Enter customer ID to delete: ");
                    String customerIdToDelete = scanner.nextLine();
                    boolean deleted = customerDAL.deleteCustomer(Integer.parseInt(customerIdToDelete));
                    if (deleted) {
                        System.out.println("Customer with ID " + customerIdToDelete + " deleted successfully.");
                    } else {
                        System.out.println("No customer found with the given ID.");
                    }
                    break;

                case "5":
                    ArrayList<Customer> customers = customerDAL.getCustomers();
                    if (customers.size() > 0) {
                        System.out.println("Customer List below :");
                        for (Customer customer : customers) {
                            System.out.println(customer.toString());
                        }
                    } else {
                        System.out.println("No Customers found, start adding.....");
                    }

                    break;
                case "6":
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a valid option.");
            }

        } while (!choice.equals("6"));

        scanner.close();
    }
}
