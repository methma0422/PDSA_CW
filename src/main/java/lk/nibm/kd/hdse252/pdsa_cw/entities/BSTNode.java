package lk.nibm.kd.hdse252.pdsa_cw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Binary Search Tree Node for efficient product searching and sorting
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BSTNode {
    private Product product;
    private BSTNode left;
    private BSTNode right;
    
    public BSTNode(Product product) {
        this.product = product;
        this.left = null;
        this.right = null;
    }
}


