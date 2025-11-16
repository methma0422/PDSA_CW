package lk.nibm.kd.hdse252.pdsa_cw.controllers;

import lk.nibm.kd.hdse252.pdsa_cw.dto.InvoiceDTO;
import lk.nibm.kd.hdse252.pdsa_cw.services.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/invoices")
@CrossOrigin(origins = "*")
public class InvoiceController {
    
    @Autowired
    private InvoiceService invoiceService;
    
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        try {
            InvoiceDTO createdInvoice = invoiceService.createInvoice(invoiceDTO);
            return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/from-sales-order/{salesOrderId}")
    public ResponseEntity<InvoiceDTO> createInvoiceFromSalesOrder(@PathVariable Long salesOrderId, @RequestBody InvoiceDTO invoiceDTO) {
        try {
            InvoiceDTO createdInvoice = invoiceService.createInvoiceFromSalesOrder(salesOrderId, invoiceDTO);
            return new ResponseEntity<>(createdInvoice, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(@PathVariable Long id, @RequestBody InvoiceDTO invoiceDTO) {
        try {
            InvoiceDTO updatedInvoice = invoiceService.updateInvoice(id, invoiceDTO);
            return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{id}/payment")
    public ResponseEntity<InvoiceDTO> recordPayment(@PathVariable Long id, @RequestParam Double paymentAmount) {
        try {
            InvoiceDTO updatedInvoice = invoiceService.recordPayment(id, paymentAmount);
            return new ResponseEntity<>(updatedInvoice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PostMapping("/{id}/issue")
    public ResponseEntity<InvoiceDTO> issueInvoice(@PathVariable Long id) {
        try {
            InvoiceDTO issuedInvoice = invoiceService.issueInvoice(id);
            return new ResponseEntity<>(issuedInvoice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable Long id) {
        try {
            invoiceService.deleteInvoice(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable Long id) {
        try {
            InvoiceDTO invoice = invoiceService.getInvoiceById(id);
            return new ResponseEntity<>(invoice, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<InvoiceDTO>> getAllInvoices() {
        List<InvoiceDTO> invoices = invoiceService.getAllInvoices();
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }
    
    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesByCustomer(@PathVariable Long customerId) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesByCustomer(customerId);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }
    
    @GetMapping("/sales-order/{salesOrderId}")
    public ResponseEntity<List<InvoiceDTO>> getInvoicesBySalesOrder(@PathVariable Long salesOrderId) {
        List<InvoiceDTO> invoices = invoiceService.getInvoicesBySalesOrder(salesOrderId);
        return new ResponseEntity<>(invoices, HttpStatus.OK);
    }
}

