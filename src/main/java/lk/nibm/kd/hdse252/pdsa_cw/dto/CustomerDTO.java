package lk.nibm.kd.hdse252.pdsa_cw.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDTO {
    private Long id;
    private String customerCode;
    private String name;
    private String email;
    private String phone;
    private String address;
    private String city;
    private String country;
    private String customerType;
    private String status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

