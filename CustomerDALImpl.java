package postgres;

import java.sql.*;
import java.util.ArrayList;

import static postgres.EncodeUtils.decodeString;
import static postgres.EncodeUtils.encodeString;

public class CustomerDALImpl implements CustomerDAL {

    @Override
    public void createCustomerSchema() {
        String customerCreateStatement = "create table if not exists customer(Id serial PRIMARY KEY, name varchar(200), user_name varchar(100), " +
                "password varchar(20), email varchar(200), phone varchar(15))";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();
        try {
            Statement statement = connection.createStatement();
            statement.execute(customerCreateStatement);
            statement.close();
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer createCustomer(Customer customer) {

        String insertStatement = "insert into customer(name, user_name, password, email, phone) values (?, ?, ?, ?, ?);";

        String nativeInsertSt = "insert into customer(name, user_name, password, email, phone) values ('" + customer.getName() + "', " +
                "'" + customer.getUserName() + "', " +
                "'" + encodeString(customer.getPassword()) + "', " +
                "'" + customer.getEmail() + "', " +
                "'" + customer.getPhone() + "');";

        String selectStatement = "select id, name, user_name, password, email, phone from customer where user_name = ?";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();
        try {
            /*PreparedStatement insertST = connection.prepareStatement(insertStatement);
            insertST.setString(1, customer.getName());
            insertST.setString(2, customer.getUserName());
            insertST.setString(3, encodeString(customer.getPassword()));
            insertST.setString(4, customer.getEmail());
            insertST.setString(5, customer.getPhone());
            insertST.execute();
            insertST.close();*/


            int i = connection.createStatement().executeUpdate(nativeInsertSt);
            System.out.println("Number of records inserted: " + i);

            PreparedStatement selectST = connection.prepareStatement(selectStatement);
            selectST.setString(1, customer.getUserName());
            ResultSet resultSet = selectST.executeQuery();
            while (resultSet.next()) {
                customer.setId(resultSet.getString("id"));
                break;
            }
            selectST.close();
            connection.close();
            return customer;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer updateCustomer(Customer customer) {
        String updateStatement = "update customer set name=?, password=?, email=?, phone=? where user_name=?";
        //String updateStatement = "update customer set email=? where id=?";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();

        try {
            PreparedStatement updateST = connection.prepareStatement(updateStatement);
            updateST.setString(1, customer.getName());
            updateST.setString(2, encodeString(customer.getPassword()));
            updateST.setString(3, customer.getEmail());
            updateST.setString(4, customer.getPhone());
            updateST.setString(5, customer.getUserName());

            int rowsUpdated = updateST.executeUpdate();

            if (rowsUpdated > 0) {
                System.out.println("Customer updated successfully.");
            } else {
                System.out.println("No customer found with the given username to update.");
            }

            customer = getCustomerByName(customer.getUserName());

            updateST.close();
            connection.close();
            return customer;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public boolean deleteCustomer(Integer customerId) {
        String deleteStatement = "delete from customer where id = ?";

        DBConnector dbConnector = new DBConnector();


        try {
            Connection connection = dbConnector.getConnection();
            PreparedStatement deleteST = connection.prepareStatement(deleteStatement);
            deleteST.setInt(1, customerId);
            int rowsDeleted = deleteST.executeUpdate();
            deleteST.close();
            connection.close();

            return rowsDeleted > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Customer> getCustomers() {

        String selectStatement = "select id, name, user_name, password, email, phone from customer limit 100";

        DBConnector dbConnector = new DBConnector();
        try {
            Connection connection = dbConnector.getConnection();

            Statement selectSt = connection.createStatement();
            ArrayList<Customer> customersList = new ArrayList<>();

            ResultSet rs = selectSt.executeQuery(selectStatement);
            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getString("id"));
                customer.setName(rs.getString("name"));
                customer.setUserName(rs.getString("user_name"));
                customer.setEmail(rs.getString("email"));
                customer.setPhone(rs.getString("phone"));
                customer.setPassword(decodeString(rs.getString("password")));
                customersList.add(customer);
            }
            selectSt.close();
            connection.close();
            return customersList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public ArrayList<Customer> createCustomers(ArrayList<Customer> customersList) {
        String insertStatement = "insert into customer(name, user_name, password, email, phone) values (?, ?, ?, ?, ?);";

        String nativeInsertSt = "insert into customer(name, user_name, password, email, phone) values";
        ArrayList<String> recordsString = new ArrayList<>();
        ArrayList<String> userNames = new ArrayList<>();

        for (Customer customer : customersList) {
            String recordString = "('" + customer.getName() + "', " +
                    "'" + customer.getUserName() + "', " +
                    "'" + encodeString(customer.getPassword()) + "', " +
                    "'" + customer.getEmail() + "', " +
                    "'" + customer.getPhone() + "')";
            recordsString.add(recordString);
            userNames.add("user_name = ?");
        }
        nativeInsertSt += String.join(", ", recordsString) + ";";
        System.out.println("Final insert statement: " + nativeInsertSt);

        String selectStatement = "select id, name, user_name, password, email, phone from customer where ";
        //String selectStatement = "select id, name, user_name, password, email, phone from customer where user_name IN ('kilari.hari','kilari.vishnu')";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();
        try {
            /*PreparedStatement insertST = connection.prepareStatement(insertStatement);
            insertST.setString(1, customer.getName());
            insertST.setString(2, customer.getUserName());
            insertST.setString(3, encodeString(customer.getPassword()));
            insertST.setString(4, customer.getEmail());
            insertST.setString(5, customer.getPhone());
            insertST.execute();
            insertST.close();*/


            int i = connection.createStatement().executeUpdate(nativeInsertSt);
            //System.out.println("Number of records inserted: "+ i);
            selectStatement += " " + String.join(" OR ", userNames);

            PreparedStatement selectST = connection.prepareStatement(selectStatement);
            //String joinString = "\""+String.join("\", \"", userNames)+"\"";
            int index = 1;
            for (Customer customer : customersList) {
                selectST.setString(index, customer.getUserName());
                index++;
            }

            ResultSet resultSet = selectST.executeQuery();
            while (resultSet.next()) {
                String userName = resultSet.getString("user_name");
                setCustomerIdByUserName(customersList, resultSet.getString("id"), userName);

            }
            selectST.close();
            connection.close();
            return customersList;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void setCustomerIdByUserName(ArrayList<Customer> customers, String id, String userName) {
        for (Customer customer : customers) {
            if (customer.getUserName().equalsIgnoreCase(userName)) {
                customer.setId(id);
                break;
            }
        }
    }

    @Override
    public ArrayList<Customer> updateCustomers(ArrayList<Customer> customersList) {
        String updateStatement = "update customer set name=?, password=?, email=?, phone=? where user_name=?";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();

        try {
            PreparedStatement updateST = connection.prepareStatement(updateStatement);

            for (Customer customer : customersList) {
                updateST.setString(1, customer.getName());
                updateST.setString(2, encodeString(customer.getPassword()));
                updateST.setString(3, customer.getEmail());
                updateST.setString(4, customer.getPhone());
                updateST.setString(5, customer.getUserName());

                int rowsUpdated = updateST.executeUpdate();

                if (rowsUpdated > 0) {
                    System.out.println("Customer with username " + customer.getUserName() + " updated successfully.");
                } else {
                    System.out.println("No customer found with the username " + customer.getUserName() + " to update.");
                }
            }

            updateST.close();
            connection.close();
            return customersList;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int deleteCustomersByUserName(ArrayList<String> userNames) {
        String deletePSTStr = "delete from customer where user_name IN (?)";
        String deleteSTStr = "delete from customer where user_name IN ";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();

        try {
            /*PreparedStatement deleteST = connection.prepareStatement(deletePSTStr);
            Array userNameArray = deleteST.getConnection().createArrayOf("VARCHAR", userNames.toArray());
            deleteST.setArray(1, userNameArray);
            int i = deleteST.executeUpdate();
            deleteST.close();*/

            deleteSTStr += "('"+String.join("', '", userNames)+"')";
            int i = connection.createStatement().executeUpdate(deleteSTStr);



            connection.close();
            return i;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    @Override
    public Customer getCustomerById(Integer id) {

        //String query = "select id, name, user_name, password, email, phone from customer where id = "+id;
        String query = "select id, name, user_name, password, email, phone from customer where id = ?";
        try {

            DBConnector dbConnector = new DBConnector();
            Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getString("id"));
                customer.setName(resultSet.getString("name"));
                customer.setUserName(resultSet.getString("user_name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setPassword(EncodeUtils.decodeString(resultSet.getString("password")));
                customers.add(customer);
            }
            //statement.close();
            preparedStatement.close();
            connection.close();
            return customers.size() > 0 ? customers.get(0) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Customer getCustomerByName(String customerName) {
        String query = "select id, name, user_name, password, email, phone from customer where user_name = ?";
        try {

            DBConnector dbConnector = new DBConnector();
            Connection connection = dbConnector.getConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, customerName);
            ResultSet resultSet = preparedStatement.executeQuery();
            //Statement statement = connection.createStatement();
            //ResultSet resultSet = statement.executeQuery(query);
            ArrayList<Customer> customers = new ArrayList<>();
            while (resultSet.next()) {
                Customer customer = new Customer();
                customer.setId(resultSet.getString("id"));
                customer.setName(resultSet.getString("name"));
                customer.setUserName(resultSet.getString("user_name"));
                customer.setEmail(resultSet.getString("email"));
                customer.setPhone(resultSet.getString("phone"));
                customer.setPassword(EncodeUtils.decodeString(resultSet.getString("password")));
                customers.add(customer);
            }
            //statement.close();
            preparedStatement.close();
            connection.close();
            return customers.size() > 0 ? customers.get(0) : null;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateCustomerEmail(int customerId, String newEmail) {
        String updateStatement = "UPDATE customer SET email = ? WHERE id = ?";

        DBConnector dbConnector = new DBConnector();
        Connection connection = dbConnector.getConnection();

        try {
            PreparedStatement updateST = connection.prepareStatement(updateStatement);
            updateST.setString(1, newEmail);
            updateST.setInt(2, customerId);

            int rowsUpdated = updateST.executeUpdate();

            updateST.close();
            connection.close();

            return rowsUpdated > 0;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getDummyString() {
        return null;
    }

}
