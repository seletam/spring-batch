package com.batchservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="customer_info")
public class Customer {

    @Id
    private int id;
    private String firstName;
    private String lastName;
    private String email;
    private String gender;
    private String contact;
    private String country;
    private String dob;
}
