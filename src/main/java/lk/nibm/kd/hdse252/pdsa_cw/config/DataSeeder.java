package lk.nibm.kd.hdse252.pdsa_cw.config;

import lk.nibm.kd.hdse252.pdsa_cw.entities.*;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataSeeder implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private CustomerRepository customerRepository;
    
    @Autowired
    private ProductRepository productRepository;
    
    @Override
    public void run(String... args) throws Exception {
        seedUsers();
        seedSuppliers();
        seedCustomers();
        seedProducts();
    }
    
    private void seedUsers() {
        if (userRepository.count() == 0) {
            // Create default admin user
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword("admin123");
            admin.setEmail("admin@woodcarving.com");
            admin.setFullName("Administrator");
            admin.setRole("ADMIN");
            admin.setPermissions("READ,CREATE,UPDATE,DELETE");
            admin.setIsActive(true);
            admin.setCreatedAt(LocalDateTime.now());
            admin.setUpdatedAt(LocalDateTime.now());
            userRepository.save(admin);
            
            // Create default manager user
            User manager = new User();
            manager.setUsername("manager");
            manager.setPassword("manager123");
            manager.setEmail("manager@woodcarving.com");
            manager.setFullName("Manager");
            manager.setRole("MANAGER");
            manager.setPermissions("READ,CREATE,UPDATE,DELETE");
            manager.setIsActive(true);
            manager.setCreatedAt(LocalDateTime.now());
            manager.setUpdatedAt(LocalDateTime.now());
            userRepository.save(manager);
            
            // Create default staff user
            User staff = new User();
            staff.setUsername("staff");
            staff.setPassword("staff123");
            staff.setEmail("staff@woodcarving.com");
            staff.setFullName("Staff Member");
            staff.setRole("STAFF");
            staff.setPermissions("READ,CREATE");
            staff.setIsActive(true);
            staff.setCreatedAt(LocalDateTime.now());
            staff.setUpdatedAt(LocalDateTime.now());
            userRepository.save(staff);
            
            System.out.println("✓ Default users created:");
            System.out.println("  - Admin: admin / admin123");
            System.out.println("  - Manager: manager / manager123");
            System.out.println("  - Staff: staff / staff123");
        }
    }
    
    private void seedSuppliers() {
        if (supplierRepository.count() == 0) {
            Supplier supplier1 = new Supplier();
            supplier1.setSupplierCode("SUP001");
            supplier1.setName("ABC Wood Supplies");
            supplier1.setContactPerson("John Doe");
            supplier1.setEmail("contact@abcwood.com");
            supplier1.setPhone("0112345678");
            supplier1.setAddress("123 Main Street");
            supplier1.setCity("Colombo");
            supplier1.setCountry("Sri Lanka");
            supplier1.setStatus("ACTIVE");
            supplierRepository.save(supplier1);
            
            Supplier supplier2 = new Supplier();
            supplier2.setSupplierCode("SUP002");
            supplier2.setName("Premium Timber Co.");
            supplier2.setContactPerson("Jane Smith");
            supplier2.setEmail("info@premiumtimber.com");
            supplier2.setPhone("0112345679");
            supplier2.setAddress("456 Oak Avenue");
            supplier2.setCity("Kandy");
            supplier2.setCountry("Sri Lanka");
            supplier2.setStatus("ACTIVE");
            supplierRepository.save(supplier2);
            
            System.out.println("✓ Sample suppliers created");
        }
    }
    
    private void seedCustomers() {
        if (customerRepository.count() == 0) {
            Customer customer1 = new Customer();
            customer1.setCustomerCode("CUST001");
            customer1.setName("Methma Perera");
            customer1.setEmail("methma@example.com");
            customer1.setPhone("0764257342");
            customer1.setAddress("37/2, Katugasthota, Kandy");
            customer1.setCity("Kandy");
            customer1.setCountry("Sri Lanka");
            customer1.setCustomerType("INDIVIDUAL");
            customer1.setStatus("ACTIVE");
            customerRepository.save(customer1);
            
            Customer customer2 = new Customer();
            customer2.setCustomerCode("CUST002");
            customer2.setName("Retail Store ABC");
            customer2.setEmail("info@retailabc.com");
            customer2.setPhone("0112345680");
            customer2.setAddress("789 Business Road");
            customer2.setCity("Colombo");
            customer2.setCountry("Sri Lanka");
            customer2.setCustomerType("BUSINESS");
            customer2.setStatus("ACTIVE");
            customerRepository.save(customer2);
            
            System.out.println("✓ Sample customers created");
        }
    }
    
    private void seedProducts() {
        if (productRepository.count() == 0) {
            Product product1 = new Product();
            product1.setProductCode("WC001");
            product1.setName("Hand Carved Buddha Statue");
            product1.setDescription("Beautiful hand-carved wooden Buddha statue");
            product1.setCategory("Statues");
            product1.setItemType("Statue");
            product1.setWoodType("Teak");
            product1.setFinishedType("Polished");
            product1.setIsCarved(true);
            product1.setCost(10000.0);
            product1.setSellingPrice(15000.0);
            product1.setStock(25);
            product1.setMinStockLevel(10);
            product1.setDimensions("12x8x20 inches");
            product1.setWeight(2.5);
            product1.setCreatedAt(LocalDateTime.now());
            product1.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product1);
            
            Product product2 = new Product();
            product2.setProductCode("WC002");
            product2.setName("Mahogany Bed");
            product2.setDescription("A polished mahogany bed");
            product2.setCategory("Furniture");
            product2.setItemType("Furniture");
            product2.setWoodType("Mahogany");
            product2.setFinishedType("Polished");
            product2.setIsCarved(true);
            product2.setCost(15000.0);
            product2.setSellingPrice(35000.0);
            product2.setStock(5);
            product2.setMinStockLevel(5);
            product2.setDimensions("26*10");
            product2.setWeight(200.0);
            product2.setCreatedAt(LocalDateTime.now());
            product2.setUpdatedAt(LocalDateTime.now());
            productRepository.save(product2);
            
            System.out.println("✓ Sample products created");
        }
    }
}

