package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.dto.SupplierDTO;
import lk.nibm.kd.hdse252.pdsa_cw.entities.Supplier;
import lk.nibm.kd.hdse252.pdsa_cw.repositories.SupplierRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierService {
    
    @Autowired
    private SupplierRepository supplierRepository;
    
    @Autowired
    private ModelMapper modelMapper;
    
    public SupplierDTO createSupplier(SupplierDTO supplierDTO) {
        if (supplierRepository.existsBySupplierCode(supplierDTO.getSupplierCode())) {
            throw new RuntimeException("Supplier code already exists");
        }
        
        Supplier supplier = modelMapper.map(supplierDTO, Supplier.class);
        Supplier savedSupplier = supplierRepository.save(supplier);
        return modelMapper.map(savedSupplier, SupplierDTO.class);
    }
    
    public SupplierDTO updateSupplier(Long id, SupplierDTO supplierDTO) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        
        supplier.setName(supplierDTO.getName());
        supplier.setContactPerson(supplierDTO.getContactPerson());
        supplier.setEmail(supplierDTO.getEmail());
        supplier.setPhone(supplierDTO.getPhone());
        supplier.setAddress(supplierDTO.getAddress());
        supplier.setCity(supplierDTO.getCity());
        supplier.setCountry(supplierDTO.getCountry());
        supplier.setStatus(supplierDTO.getStatus());
        
        Supplier updatedSupplier = supplierRepository.save(supplier);
        return modelMapper.map(updatedSupplier, SupplierDTO.class);
    }
    
    public void deleteSupplier(Long id) {
        supplierRepository.deleteById(id);
    }
    
    public SupplierDTO getSupplierById(Long id) {
        Supplier supplier = supplierRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier not found"));
        return modelMapper.map(supplier, SupplierDTO.class);
    }
    
    public List<SupplierDTO> getAllSuppliers() {
        return supplierRepository.findAll().stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
                .collect(Collectors.toList());
    }
    
    public List<SupplierDTO> getSuppliersByStatus(String status) {
        return supplierRepository.findByStatus(status).stream()
                .map(supplier -> modelMapper.map(supplier, SupplierDTO.class))
                .collect(Collectors.toList());
    }
}

