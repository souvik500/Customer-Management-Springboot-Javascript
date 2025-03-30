document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    const auth = localStorage.getItem('auth');
    if (!auth) {
        window.location.href = 'login.html';
    }
    
    // Elements
    const customerTableBody = document.getElementById('customerTableBody');
    const searchInput = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    const addCustomerBtn = document.getElementById('addCustomerBtn');
    const syncBtn = document.getElementById('syncBtn');
    const prevPageBtn = document.getElementById('prevPage');
    const nextPageBtn = document.getElementById('nextPage');
    const pageInfo = document.getElementById('pageInfo');
    const logoutBtn = document.getElementById('logoutBtn');
    const message = document.getElementById('message');
    
    // Pagination state
    let currentPage = 0;
    let totalPages = 0;
    let pageSize = 10;
    let currentSearch = '';
    let currentSort = 'id';
    let currentSortDir = 'asc';
    
    // Load customers
    function loadCustomers() {
        const url = `http://localhost:2000/api/customers?page=${currentPage}&size=${pageSize}&search=${currentSearch}&sortBy=${currentSort}&sortDir=${currentSortDir}`;
        
        fetch(url, {
            method: 'GET',
            headers: {
                'Authorization': auth
            }
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else if (response.status === 401) {
                // Unauthorized, redirect to login
                localStorage.removeItem('auth');
                window.location.href = 'login.html';
            } else {
                throw new Error('Failed to load customers');
            }
        })
        .then(data => {
            // Update pagination info
            currentPage = data.currentPage;
            totalPages = data.totalPages;
            pageInfo.textContent = `Page ${currentPage + 1} of ${totalPages}`;
            
            // Enable/disable pagination buttons
            prevPageBtn.disabled = currentPage === 0;
            nextPageBtn.disabled = currentPage >= totalPages - 1;
            
            // Clear table
            customerTableBody.innerHTML = '';
            
            // Add customers to table
            data.customers.forEach(customer => {
                const row = document.createElement('tr');
                
                row.innerHTML = `
                    <td>${customer.id}</td>
                    <td>${customer.firstName}</td>
                    <td>${customer.lastName}</td>
                    <td>${customer.email}</td>
                    <td>${customer.phone}</td>
                    <td>${customer.city}</td>
                    <td>${customer.state}</td>
                    <td>
                        <button class="btn btn-primary btn-sm edit-btn" data-id="${customer.id}">Edit</button>
                        <button class="btn btn-danger btn-sm delete-btn" data-id="${customer.id}">Delete</button>
                    </td>
                `;
                
                customerTableBody.appendChild(row);
            });
            
            // Add event listeners to edit and delete buttons
            document.querySelectorAll('.edit-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const id = this.getAttribute('data-id');
                    window.location.href = `customer-form.html?id=${id}`;
                });
            });
            
            document.querySelectorAll('.delete-btn').forEach(btn => {
                btn.addEventListener('click', function() {
                    const id = this.getAttribute('data-id');
                    if (confirm('Are you sure you want to delete this customer?')) {
                        deleteCustomer(id);
                    }
                });
            });
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    }
    
    // Delete customer
    function deleteCustomer(id) {
        fetch(`http://localhost:2000/api/customers/${id}`, {
            method: 'DELETE',
            headers: {
                'Authorization': auth
            }
        })
        .then(response => {
            if (response.ok) {
                showMessage('Customer deleted successfully', 'success');
                loadCustomers();
            } else {
                throw new Error('Failed to delete customer');
            }
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    }
    
    // Sync customers
    function syncCustomers() {
        fetch('http://localhost:2000/api/customers/sync', {
            method: 'POST',
            headers: {
                'Authorization': auth
            }
        })
        .then(response => {
            if (response.ok) {
                return response.text();
            } else {
                throw new Error('Failed to sync customers');
            }
        })
        .then(data => {
            showMessage(data, 'success');
            loadCustomers();
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    }
    
    // Show message
    function showMessage(text, type) {
        message.textContent = text;
        message.className = `message ${type}`;
        
        // Clear message after 3 seconds
        setTimeout(() => {
            message.textContent = '';
            message.className = 'message';
        }, 3000);
    }
    
    // Event listeners
    searchBtn.addEventListener('click', function() {
        currentSearch = searchInput.value;
        currentPage = 0;
        loadCustomers();
    });
    
    searchInput.addEventListener('keypress', function(e) {
        if (e.key === 'Enter') {
            currentSearch = searchInput.value;
            currentPage = 0;
            loadCustomers();
        }
    });
    
    addCustomerBtn.addEventListener('click', function() {
        window.location.href = 'customer-form.html';
    });
    
    syncBtn.addEventListener('click', function() {
        syncCustomers();
    });
    
    prevPageBtn.addEventListener('click', function() {
        if (currentPage > 0) {
            currentPage--;
            loadCustomers();
        }
    });
    
    nextPageBtn.addEventListener('click', function() {
        if (currentPage < totalPages - 1) {
            currentPage++;
            loadCustomers();
        }
    });
    
    logoutBtn.addEventListener('click', function() {
        localStorage.removeItem('auth');
        window.location.href = 'login.html';
    });
    
    // Sort table columns
    document.querySelectorAll('th[data-sort]').forEach(th => {
        th.addEventListener('click', function() {
            const sortBy = this.getAttribute('data-sort');
            
            // Toggle sort direction if clicking on the same column
            if (sortBy === currentSort) {
                currentSortDir = currentSortDir === 'asc' ? 'desc' : 'asc';
            } else {
                currentSort = sortBy;
                currentSortDir = 'asc';
            }
            
            // Update sort icons
            document.querySelectorAll('.sort-icon').forEach(icon => {
                icon.textContent = '↕';
            });
            
            const sortIcon = this.querySelector('.sort-icon');
            sortIcon.textContent = currentSortDir === 'asc' ? '↑' : '↓';
            
            loadCustomers();
        });
    });
    
    // Initial load
    loadCustomers();
});