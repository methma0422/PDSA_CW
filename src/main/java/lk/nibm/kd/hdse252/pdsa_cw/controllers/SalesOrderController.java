package lk.nibm.kd.hdse252.pdsa_cw.controllers;

import lk.nibm.kd.hdse252.pdsa_cw.dto.SalesOrderDTO;
import lk.nibm.kd.hdse252.pdsa_cw.services.SalesOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sales-orders")
@CrossOrigin(origins = "*")
public class SalesOrderController {
    
    @Autowired
    private SalesOrderService salesOrderService;
    
    @PostMapping
    public ResponseEntity<SalesOrderDTO> createSalesOrder(@RequestBody SalesOrderDTO salesOrderDTO) {
        try {
            SalesOrderDTO createdOrder = salesOrderService.createSalesOrder(salesOrderDTO);
            return new ResponseEntity<>(createdOrder, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<SalesOrderDTO> updateSalesOrder(@PathVariable Long id, @RequestBody SalesOrderDTO salesOrderDTO) {
        try {
            SalesOrderDTO updatedOrder = salesOrderService.updateSalesOrder(id, salesOrderDTO);
            return new ResponseEntity<>(updatedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{id}/confirm")
    public ResponseEntity<SalesOrderDTO> confirmSalesOrder(@PathVariable Long id) {
        try {
            SalesOrderDTO confirmedOrder = salesOrderService.confirmSalesOrder(id);
            return new ResponseEntity<>(confirmedOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSalesOrder(@PathVariable Long id) {
        try {
            salesOrderService.deleteSalesOrder(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<SalesOrderDTO> getSalesOrderById(@PathVariable Long id) {
        try {
            SalesOrderDTO salesOrder = salesOrderService.getSalesOrderById(id);
            return new ResponseEntity<>(salesOrder, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<SalesOrderDTO>> getAllSalesOrders() {
        List<SalesOrderDTO> salesOrders = salesOrderService.getAllSalesOrders();
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<SalesOrderDTO>> getSalesOrdersByCustomer(@PathVariable Long customerId) {
        List<SalesOrderDTO> salesOrders = salesOrderService.getSalesOrdersByCustomer(customerId);
        return new ResponseEntity<>(salesOrders, HttpStatus.OK);
    }
}


