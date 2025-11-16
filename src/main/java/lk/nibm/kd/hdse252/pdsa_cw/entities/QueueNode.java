package lk.nibm.kd.hdse252.pdsa_cw.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Queue Node for managing restock requests
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class QueueNode {
    private RestockRequest request;
    private QueueNode next;
    
    public QueueNode(RestockRequest request) {
        this.request = request;
        this.next = null;
    }
}

