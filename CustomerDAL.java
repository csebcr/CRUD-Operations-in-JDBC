package postgres;

import java.util.ArrayList;

public interface CustomerDAL {
    public void createCustomerSchema();

    public Customer createCustomer(Customer customer);

    public Customer updateCustomer(Customer customer);

    public abstract String getDummyString();

    public default ArrayList<Customer> updateCustomers(ArrayList<Customer> customers) {
        return null;
    }

    public boolean deleteCustomer(Integer customerId);

    public default int deleteCustomersByUserName(ArrayList<String> userNames) {
        return 0;
    }

    public ArrayList<Customer> getCustomers();
    public ArrayList<Customer> createCustomers(ArrayList<Customer> customersList);
    public Customer getCustomerById(Integer customerId);

    public Customer getCustomerByName(String customerName);

    public boolean updateCustomerEmail(int customerId, String newEmail);

}
