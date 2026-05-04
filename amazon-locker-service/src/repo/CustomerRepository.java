package repo;

import models.Customer;

import java.util.HashMap;
import java.util.Map;

public class CustomerRepository {
    private final Map<String, Customer> customerMap = new HashMap<>();

    public void save(Customer customer) {
        customerMap.put(customer.getId(), customer);
    }

    public Customer getById(String customerId) {
        return customerMap.get(customerId);
    }
}
