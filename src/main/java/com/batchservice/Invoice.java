package com.batchservice;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.io.Serializable;
import java.util.UUID;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name="invoice_info")
public class Invoice implements Serializable {

    @Id
    private int id;
    private int customerId;
    private String amount;
    private String total;
    private UUID referenceNumber;

    @Override
    public String toString() {
        return  id +
                ", " + customerId +
                ", " + amount +
                ", " + total +
                ", " + referenceNumber +
                "\r\n";
    }
}
