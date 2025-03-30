document.addEventListener('DOMContentLoaded', function() {
    // Check if user is already logged in
    if (sessionStorage.getItem('auth')) {
        window.location.href = 'login.html';
    }
    
    const loginForm = document.getElementById('loginForm');
    const loginMessage = document.getElementById('loginMessage');
    
    loginForm.addEventListener('submit', function(e) {
        e.preventDefault();
        
        const username = document.getElementById('username').value;
        const password = document.getElementById('password').value;
        
        // Create Basic Auth header
        const authHeader = 'Basic ' + btoa(username + ':' + password);
        
        // Test the credentials by making a request to the API
        fetch('http://localhost:2000/api/customers', {
            method: 'GET',
            headers: {
                'Authorization': authHeader
            }
        })
        .then(response => {
            if (response.ok) {
                // Store auth in localStorage
                localStorage.setItem('auth', authHeader);
                
                // Redirect to customer list page
                window.location.href = 'customer-list.html';
            } else {
                loginMessage.textContent = 'Invalid username or password';
                loginMessage.className = 'message error';
            }
        })
        .catch(error => {
            loginMessage.textContent = 'An error occurred: ' + error.message;
            loginMessage.className = 'message error';
        });
    });
});