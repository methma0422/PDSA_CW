// API Configuration
const API_BASE_URL = 'http://localhost:8080/api';

// API Utility Functions
const api = {
    // Generic fetch wrapper
    async request(endpoint, options = {}) {
        const url = `${API_BASE_URL}${endpoint}`;
        const currentUserStr = typeof sessionStorage !== 'undefined' ? sessionStorage.getItem('currentUser') : null;
        const currentUser = currentUserStr ? JSON.parse(currentUserStr) : null;
        const defaultOptions = {
            headers: {
                'Content-Type': 'application/json',
                'X-User-Role': currentUser && currentUser.role ? currentUser.role : ''
            },
        };
        
        const config = { ...defaultOptions, ...options };
        
        try {
            const response = await fetch(url, config);
            
            // Handle empty responses (like 204 No Content for DELETE)
            if (response.status === 204) {
                return { success: true, data: null };
            }
            
            // Check if response has content
            const contentType = response.headers.get('content-type');
            let data = null;
            
            if (contentType && contentType.includes('application/json')) {
                const text = await response.text();
                if (text && text.trim()) {
                    try {
                        data = JSON.parse(text);
                    } catch (e) {
                        // If JSON parsing fails, return the text
                        console.error('JSON parse error:', e);
                        data = { message: text };
                    }
                }
            } else {
                // For non-JSON responses, get text
                const text = await response.text();
                if (text && text.trim()) {
                    data = { message: text };
                }
            }
            
            if (!response.ok) {
                const errorMessage = data?.message || data?.error || data || `HTTP error! status: ${response.status}`;
                throw new Error(errorMessage);
            }
            
            return { success: true, data };
        } catch (error) {
            console.error('API Error:', error);
            return { success: false, error: error.message };
        }
    },
    
    // GET request
    async get(endpoint) {
        return this.request(endpoint, { method: 'GET' });
    },
    
    // POST request
    async post(endpoint, body) {
        return this.request(endpoint, {
            method: 'POST',
            body: JSON.stringify(body),
        });
    },
    
    // PUT request
    async put(endpoint, body) {
        return this.request(endpoint, {
            method: 'PUT',
            body: JSON.stringify(body),
        });
    },
    
    // DELETE request
    async delete(endpoint) {
        return this.request(endpoint, { method: 'DELETE' });
    },
    
    // User API
    user: {
        async login(username, password) {
            try {
                // Get user by username
                const result = await api.get(`/users/username/${username}`);
                if (result.success && result.data) {
                    // Simple password comparison
                    if (result.data.password === password) {
                        // Store user in sessionStorage (without password)
                        sessionStorage.setItem('currentUser', JSON.stringify({
                            id: result.data.id,
                            username: result.data.username,
                            email: result.data.email,
                            fullName: result.data.fullName,
                            role: result.data.role
                        }));
                        return { success: true, data: result.data };
                    } else {
                        return { success: false, error: 'Invalid password' };
                    }
                }
                return { success: false, error: 'User not found' };
            } catch (error) {
                return { success: false, error: 'Login failed. Please try again.' };
            }
        },
        
        async getAll() {
            return api.get('/users');
        },
        
        async getById(id) {
            return api.get(`/users/${id}`);
        },
        
        async create(userData) {
            return api.post('/users', userData);
        },
        
        async update(id, userData) {
            return api.put(`/users/${id}`, userData);
        },
        
        async delete(id) {
            return api.delete(`/users/${id}`);
        }
    },
    
    // Product API
    product: {
        async getAll() {
            return api.get('/products');
        },
        
        async getById(id) {
            return api.get(`/products/${id}`);
        },
        
        async getByCode(code) {
            return api.get(`/products/search/code/${code}`);
        },
        
        async create(productData) {
            return api.post('/products', productData);
        },
        
        async update(id, productData) {
            return api.put(`/products/${id}`, productData);
        },
        
        async delete(id) {
            return api.delete(`/products/${id}`);
        },
        
        async getSorted() {
            return api.get('/products/sorted');
        },
        
        async getRecent() {
            return api.get('/products/recent');
        },
        
        async getLowStock() {
            return api.get('/products/low-stock');
        },
        
        async search(searchData) {
            return api.post('/products/search', searchData);
        }
    },
    
    // Supplier API
    supplier: {
        async getAll() {
            return api.get('/suppliers');
        },
        
        async getById(id) {
            return api.get(`/suppliers/${id}`);
        },
        
        async create(supplierData) {
            return api.post('/suppliers', supplierData);
        },
        
        async update(id, supplierData) {
            return api.put(`/suppliers/${id}`, supplierData);
        },
        
        async delete(id) {
            return api.delete(`/suppliers/${id}`);
        }
    },
    
    // Customer API
    customer: {
        async getAll() {
            return api.get('/customers');
        },
        
        async getById(id) {
            return api.get(`/customers/${id}`);
        },
        
        async create(customerData) {
            return api.post('/customers', customerData);
        },
        
        async update(id, customerData) {
            return api.put(`/customers/${id}`, customerData);
        },
        
        async delete(id) {
            return api.delete(`/customers/${id}`);
        }
    },
    
    // Purchase Order API
    purchaseOrder: {
        async getAll() {
            return api.get('/purchase-orders');
        },
        
        async getById(id) {
            return api.get(`/purchase-orders/${id}`);
        },
        
        async create(orderData) {
            return api.post('/purchase-orders', orderData);
        },
        
        async update(id, orderData) {
            return api.put(`/purchase-orders/${id}`, orderData);
        },
        
        async delete(id) {
            return api.delete(`/purchase-orders/${id}`);
        }
    },
    
    // GRN API
    grn: {
        async getAll() {
            return api.get('/grns');
        },
        
        async getById(id) {
            return api.get(`/grns/${id}`);
        },
        
        async getByPurchaseOrder(purchaseOrderId) {
            return api.get(`/grns/purchase-order/${purchaseOrderId}`);
        },
        
        async create(grnData) {
            return api.post('/grns', grnData);
        },
        
        async update(id, grnData) {
            return api.put(`/grns/${id}`, grnData);
        },
        
        async complete(id) {
            return api.post(`/grns/${id}/complete`, {});
        },
        
        async delete(id) {
            return api.delete(`/grns/${id}`);
        }
    },
    
    // Sales Order API
    salesOrder: {
        async getAll() {
            return api.get('/sales-orders');
        },
        
        async getById(id) {
            return api.get(`/sales-orders/${id}`);
        },
        
        async create(orderData) {
            return api.post('/sales-orders', orderData);
        },
        
        async update(id, orderData) {
            return api.put(`/sales-orders/${id}`, orderData);
        },
        
        async confirm(id) {
            return api.post(`/sales-orders/${id}/confirm`, {});
        },
        
        async delete(id) {
            return api.delete(`/sales-orders/${id}`);
        }
    },
    
    // Invoice API
    invoice: {
        async getAll() {
            return api.get('/invoices');
        },
        
        async getById(id) {
            return api.get(`/invoices/${id}`);
        },
        
        async create(invoiceData) {
            return api.post('/invoices', invoiceData);
        },
        
        async createFromSalesOrder(salesOrderId, invoiceData) {
            return api.post(`/invoices/from-sales-order/${salesOrderId}`, invoiceData);
        },
        
        async update(id, invoiceData) {
            return api.put(`/invoices/${id}`, invoiceData);
        },
        
        async recordPayment(id, amount) {
            return api.post(`/invoices/${id}/payment?paymentAmount=${amount}`, {});
        },
        
        async issue(id) {
            return api.post(`/invoices/${id}/issue`, {});
        },
        
        async delete(id) {
            return api.delete(`/invoices/${id}`);
        }
    }
};

// Utility Functions
const utils = {
    // Show notification/toast
    showNotification(message, type = 'info') {
        // Create a simple notification
        const notification = document.createElement('div');
        notification.className = `alert alert-${type === 'error' ? 'danger' : type} alert-dismissible fade show position-fixed`;
        notification.style.cssText = 'top: 20px; right: 20px; z-index: 9999; min-width: 300px;';
        notification.innerHTML = `
            ${message}
            <button type="button" class="btn-close" data-bs-dismiss="alert"></button>
        `;
        document.body.appendChild(notification);
        
        // Auto remove after 5 seconds
        setTimeout(() => {
            notification.remove();
        }, 5000);
    },


    
    // Get current user from session
    getCurrentUser() {
        const userStr = sessionStorage.getItem('currentUser');
        return userStr ? JSON.parse(userStr) : null;
    },
    
    // Check if user is logged in
    isLoggedIn() {
        return this.getCurrentUser() !== null;
    },
    
    // Logout
    logout() {
        sessionStorage.removeItem('currentUser');
        window.location.href = '/login';
    },
    
    // Role helpers
    userHasRole(requiredRole) {
        const u = this.getCurrentUser();
        if (!u || !u.role || !requiredRole) return false;
        return u.role.toUpperCase() === requiredRole.toUpperCase();
    },
    userHasAnyRole(requiredCsv) {
        const u = this.getCurrentUser();
        if (!u || !u.role) return false;
        const role = u.role.toUpperCase();
        return requiredCsv.split(',').map(r => r.trim().toUpperCase()).includes(role);
    },
    // Hide elements with data-role-required if current user lacks role
    applyRoleVisibility() {
        try {
            document.querySelectorAll('[data-role-required]').forEach(el => {
                const req = el.getAttribute('data-role-required');
                if (!this.userHasAnyRole(req)) {
                    el.style.display = 'none';
                    el.disabled = true;
                }
            });
        } catch (e) {
            // ignore
        }
    },
    
    // Format date
    formatDate(dateString) {
        if (!dateString) return '';
        const date = new Date(dateString);
        return date.toLocaleDateString() + ' ' + date.toLocaleTimeString();
    },
    
    // Format currency
    formatCurrency(amount) {
        return new Intl.NumberFormat('en-US', {
            style: 'currency',
            currency: 'USD'
        }).format(amount);
    }
};

// Apply role-based visibility when DOM is ready
if (typeof window !== 'undefined') {
    window.addEventListener('DOMContentLoaded', () => {
        utils.applyRoleVisibility();
    });
}

