package lk.nibm.kd.hdse252.pdsa_cw.services;

import lk.nibm.kd.hdse252.pdsa_cw.entities.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class to manage custom data structures
 */
@Service
public class DataStructureService {
    
    // Array to store all products
    private final List<Product> productArray = new ArrayList<>();
    
    // Binary Search Tree root for fast searching and sorting
    private BSTNode bstRoot = null;
    
    // Linked List head for recently added items
    private LinkedListNode recentItemsHead = null;
    private int recentItemsCount = 0;
    private static final int MAX_RECENT_ITEMS = 10;
    
    // Queue for restock requests
    private QueueNode restockQueueFront = null;
    private QueueNode restockQueueRear = null;
    private int restockQueueSize = 0;
    
    /**
     * Add product to array
     */
    public void addToArray(Product product) {
        productArray.add(product);
    }
    
    /**
     * Get all products from array
     */
    public List<Product> getAllFromArray() {
        return new ArrayList<>(productArray);
    }
    
    /**
     * Remove product from array
     */
    public void removeFromArray(Long productId) {
        productArray.removeIf(p -> p.getId().equals(productId));
    }
    
    /**
     * Update product in array
     */
    public void updateInArray(Product product) {
        for (int i = 0; i < productArray.size(); i++) {
            if (productArray.get(i).getId().equals(product.getId())) {
                productArray.set(i, product);
                break;
            }
        }
    }
    
    /**
     * Insert product into Binary Search Tree (sorted by product code)
     */
    public void insertIntoBST(Product product) {
        bstRoot = insertBSTNode(bstRoot, product);
    }
    
    private BSTNode insertBSTNode(BSTNode root, Product product) {
        if (root == null) {
            return new BSTNode(product);
        }
        
        if (product.getProductCode().compareToIgnoreCase(root.getProduct().getProductCode()) < 0) {
            root.setLeft(insertBSTNode(root.getLeft(), product));
        } else if (product.getProductCode().compareToIgnoreCase(root.getProduct().getProductCode()) > 0) {
            root.setRight(insertBSTNode(root.getRight(), product));
        }
        
        return root;
    }
    
    /**
     * Search product in BST by product code
     */
    public Product searchInBST(String productCode) {
        return searchBSTNode(bstRoot, productCode);
    }
    
    private Product searchBSTNode(BSTNode root, String productCode) {
        if (root == null) {
            return null;
        }
        
        int comparison = productCode.compareToIgnoreCase(root.getProduct().getProductCode());
        if (comparison == 0) {
            return root.getProduct();
        } else if (comparison < 0) {
            return searchBSTNode(root.getLeft(), productCode);
        } else {
            return searchBSTNode(root.getRight(), productCode);
        }
    }
    
    /**
     * Get sorted products from BST (in-order traversal)
     */
    public List<Product> getSortedFromBST() {
        List<Product> sortedList = new ArrayList<>();
        inOrderTraversal(bstRoot, sortedList);
        return sortedList;
    }
    
    private void inOrderTraversal(BSTNode root, List<Product> result) {
        if (root != null) {
            inOrderTraversal(root.getLeft(), result);
            result.add(root.getProduct());
            inOrderTraversal(root.getRight(), result);
        }
    }
    
    /**
     * Remove product from BST
     */
    public void removeFromBST(String productCode) {
        bstRoot = removeBSTNode(bstRoot, productCode);
    }
    
    private BSTNode removeBSTNode(BSTNode root, String productCode) {
        if (root == null) {
            return null;
        }
        
        int comparison = productCode.compareToIgnoreCase(root.getProduct().getProductCode());
        if (comparison < 0) {
            root.setLeft(removeBSTNode(root.getLeft(), productCode));
        } else if (comparison > 0) {
            root.setRight(removeBSTNode(root.getRight(), productCode));
        } else {
            // Node to be deleted found
            if (root.getLeft() == null) {
                return root.getRight();
            } else if (root.getRight() == null) {
                return root.getLeft();
            }
            
            // Node with two children: Get the inorder successor
            BSTNode minNode = findMin(root.getRight());
            root.setProduct(minNode.getProduct());
            root.setRight(removeBSTNode(root.getRight(), minNode.getProduct().getProductCode()));
        }
        
        return root;
    }
    
    private BSTNode findMin(BSTNode root) {
        while (root.getLeft() != null) {
            root = root.getLeft();
        }
        return root;
    }
    
    /**
     * Add product to recently added items (Linked List)
     */
    public void addToRecentItems(Product product) {
        LinkedListNode newNode = new LinkedListNode(product);
        newNode.setNext(recentItemsHead);
        recentItemsHead = newNode;
        recentItemsCount++;
        
        // Keep only the most recent items
        if (recentItemsCount > MAX_RECENT_ITEMS) {
            LinkedListNode current = recentItemsHead;
            for (int i = 0; i < MAX_RECENT_ITEMS - 1; i++) {
                current = current.getNext();
            }
            current.setNext(null);
            recentItemsCount = MAX_RECENT_ITEMS;
        }
    }
    
    /**
     * Get recently added items from Linked List
     */
    public List<Product> getRecentItems() {
        List<Product> recentList = new ArrayList<>();
        LinkedListNode current = recentItemsHead;
        while (current != null) {
            recentList.add(current.getProduct());
            current = current.getNext();
        }
        return recentList;
    }
    
    /**
     * Enqueue restock request (Queue)
     */
    public void enqueueRestockRequest(RestockRequest request) {
        QueueNode newNode = new QueueNode(request);
        
        if (restockQueueRear == null) {
            restockQueueFront = restockQueueRear = newNode;
        } else {
            restockQueueRear.setNext(newNode);
            restockQueueRear = newNode;
        }
        restockQueueSize++;
    }
    
    /**
     * Dequeue restock request (Queue)
     */
    public RestockRequest dequeueRestockRequest() {
        if (restockQueueFront == null) {
            return null;
        }
        
        RestockRequest request = restockQueueFront.getRequest();
        restockQueueFront = restockQueueFront.getNext();
        
        if (restockQueueFront == null) {
            restockQueueRear = null;
        }
        restockQueueSize--;
        
        return request;
    }
    
    /**
     * Peek at front of restock queue
     */
    public RestockRequest peekRestockRequest() {
        if (restockQueueFront == null) {
            return null;
        }
        return restockQueueFront.getRequest();
    }
    
    /**
     * Get all restock requests from queue
     */
    public List<RestockRequest> getAllRestockRequests() {
        List<RestockRequest> requests = new ArrayList<>();
        QueueNode current = restockQueueFront;
        while (current != null) {
            requests.add(current.getRequest());
            current = current.getNext();
        }
        return requests;
    }
    
    /**
     * Get queue size
     */
    public int getRestockQueueSize() {
        return restockQueueSize;
    }
    
    /**
     * Clear all data structures
     */
    public void clearAll() {
        productArray.clear();
        bstRoot = null;
        recentItemsHead = null;
        recentItemsCount = 0;
        restockQueueFront = null;
        restockQueueRear = null;
        restockQueueSize = 0;
    }
    
    /**
     * Rebuild BST from array
     */
    public void rebuildBST() {
        bstRoot = null;
        for (Product product : productArray) {
            insertIntoBST(product);
        }
    }
}

