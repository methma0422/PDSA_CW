package lk.nibm.kd.hdse252.pdsa_cw.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ViewController {
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/index")
    public String indexPage() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login-index";
    }
    
    @GetMapping("/login-index")
    public String loginIndex() {
        return "login-index";
    }
    
    @GetMapping("/user-Index")
    public String userIndex() {
        return "user-Index";
    }
    
    @GetMapping("/supplier-index")
    public String supplierIndex() {
        return "supplier-index";
    }
    
    @GetMapping("/customer-index")
    public String customerIndex() {
        return "customer-index";
    }
    
    @GetMapping("/item-index")
    public String itemIndex() {
        return "item-index";
    }
    
    @GetMapping("/purchaseOrder-index")
    public String purchaseOrderIndex() {
        return "purchaseOrder-index";
    }
    
    @GetMapping("/grn-index")
    public String grnIndex() {
        return "grn-index";
    }
    
    @GetMapping("/salesOrder-index")
    public String salesOrderIndex() {
        return "salesOrder-index";
    }
    
    @GetMapping("/invoice-index")
    public String invoiceIndex() {
        return "invoice-index";
    }
}

