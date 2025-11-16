package lk.nibm.kd.hdse252.pdsa_cw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Linked List Node for managing recently added items
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LinkedListNode {
    private Product product;
    private LinkedListNode next;
    
    public LinkedListNode(Product product) {
        this.product = product;
        this.next = null;
    }
}


