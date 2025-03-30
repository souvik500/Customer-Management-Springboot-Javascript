document.addEventListener('DOMContentLoaded', function() {
    // Check if user is logged in
    const auth = localStorage.getItem('auth');
    if (!auth) {
        window.location.href = 'login.html';
    }
    
    // Elements
    const customerForm = document.getElementById('customerForm');
    const formTitle = document.getElementById('formTitle');
    const customerId = document.getElementById('customerId');
    const backBtn = document.getElementById('backBtn');
    const formMessage = document.getElementById('formMessage');
    
    // Check if editing existing customer
    const urlParams = new URLSearchParams(window.location.search);
    const id = urlParams.get('id');
    
    if (id) {
        formTitle.textContent = 'Edit Customer';
        loadCustomer(id);
    } else {
        formTitle.textContent = 'Add Customer';
    }
    
    // Load customer data for editing
    function loadCustomer(id) {
        fetch(`http://localhost:2000/api/customers/${id}`, {
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
                throw new Error('Failed to load customer');
            }
        })
        .then(customer => {
            customerId.value = customer.id;
            document.getElementById('firstName').value = customer.firstName;
            document.getElementById('lastName').value = customer.lastName;
            document.getElementById('street').value = customer.street;
            document.getElementById('address').value = customer.address;
            document.getElementById('city').value = customer.city;
            document.getElementById('state').value = customer.state;
            document.getElementById('email').value = customer.email;
            document.getElementById('phone').value = customer.phone;
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    }
    
    // Save customer
    customerForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const customer = {
            firstName: document.getElementById('firstName').value,
            lastName: document.getElementById('lastName').value,
            street: document.getElementById('street').value,
            address: document.getElementById('address').value,
            city: document.getElementById('city').value,
            state: document.getElementById('state').value,
            email: document.getElementById('email').value,
            phone: document.getElementById('phone').value
        };
        
        let url = 'http://localhost:2000/api/customers';
        let method = 'POST';
        
        if (customerId.value) {
            url += `/${customerId.value}`;
            method = 'PUT';
        }
        
        fetch(url, {
            method: method,
            headers: {
                'Authorization': auth,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(customer)
        })
        .then(response => {
            if (response.ok) {
                return response.json();
            } else {
                throw new Error('Failed to save customer');
            }
        })
        .then(data => {
            showMessage('Customer saved successfully', 'success');
            
            // Redirect to list after 1 second
            setTimeout(() => {
                window.location.href = 'customer-list.html';
            }, 1000);
        })
        .catch(error => {
            showMessage(error.message, 'error');
        });
    });
    
    // Back button
    backBtn.addEventListener('click', function() {
        window.location.href = 'customer-list.html';
    });
    
    // Show message
    function showMessage(text, type) {
        formMessage.textContent = text;
        formMessage.className = `message ${type}`;
    }
});