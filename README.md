# 🛒 Grocery List Web App

A full-stack web application that allows users to register, log in, and manage grocery lists. Built with a focus on CRUD functionality, session management, and client-side security practices.

## 🚀 Features

### 🔐 Authentication
- User registration with username and password
- Secure login with session handling via backend
- Logout functionality with session cleanup and frontend localStorage clearing

### 🧑 User Dashboard
- Personalized greeting using user's first name
- View all grocery lists associated with the logged-in user
- Create new grocery lists

### 📋 Grocery List Management
- View individual grocery lists
- Add new grocery items (name + quantity)
- Delete grocery items from the list
- Real-time updates after each CRUD operation
- Inline form validation for empty or invalid input

### ⚙️ Security Enhancements
- Session-based access control to prevent unauthorized access to lists
- Restricted routes (e.g., dashboard and list pages require an active session)
- Browser back button behavior controlled to prevent viewing cached data post-logout
- Login form auto-clears on page load and after logout to prevent credential autofill on back navigation
- Inputs and forms have `autocomplete="off"` and caching is disabled via meta headers

### 🌐 Technologies Used
- **Frontend**: HTML, CSS, Bootstrap, JavaScript
- **Backend**: Node.js with Express (API assumed to be running separately)
- **Session Handling**: Cookie-based session control via backend `/session` endpoints
- **Storage**: localStorage used for temporary client-side user metadata

## 📂 File Structure (Frontend)
