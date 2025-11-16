package lk.nibm.kd.hdse252.pdsa_cw.controllers;

import lk.nibm.kd.hdse252.pdsa_cw.dto.GRNDTO;
import lk.nibm.kd.hdse252.pdsa_cw.services.GRNService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/grns")
@CrossOrigin(origins = "*")
public class GRNController {
    
    @Autowired
    private GRNService grnService;
    
    @PostMapping
    public ResponseEntity<GRNDTO> createGRN(@RequestBody GRNDTO grnDTO) {
        try {
            GRNDTO createdGRN = grnService.createGRN(grnDTO);
            return new ResponseEntity<>(createdGRN, HttpStatus.CREATED);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<GRNDTO> updateGRN(@PathVariable Long id, @RequestBody GRNDTO grnDTO) {
        try {
            GRNDTO updatedGRN = grnService.updateGRN(id, grnDTO);
            return new ResponseEntity<>(updatedGRN, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @PostMapping("/{id}/complete")
    public ResponseEntity<GRNDTO> completeGRN(@PathVariable Long id) {
        try {
            GRNDTO completedGRN = grnService.completeGRN(id);
            return new ResponseEntity<>(completedGRN, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGRN(@PathVariable Long id) {
        try {
            grnService.deleteGRN(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<GRNDTO> getGRNById(@PathVariable Long id) {
        try {
            GRNDTO grn = grnService.getGRNById(id);
            return new ResponseEntity<>(grn, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping
    public ResponseEntity<List<GRNDTO>> getAllGRNs() {
        List<GRNDTO> grns = grnService.getAllGRNs();
        return new ResponseEntity<>(grns, HttpStatus.OK);
    }
    
    @GetMapping("/purchase-order/{purchaseOrderId}")
    public ResponseEntity<List<GRNDTO>> getGRNsByPurchaseOrder(@PathVariable Long purchaseOrderId) {
        List<GRNDTO> grns = grnService.getGRNsByPurchaseOrder(purchaseOrderId);
        return new ResponseEntity<>(grns, HttpStatus.OK);
    }
}


