import React from 'react';
import { Link, useNavigate } from 'react-router-dom'; // Import useNavigate for redirection
import '../styles/style.css';

const Nav = ({ user, setUser }) => {
  const navigate = useNavigate(); // Hook for navigation

  const handleLogout = () => {
    // Perform logout operations, e.g., clearing local storage, invalidating session, etc.
    localStorage.removeItem('userToken'); // Assuming you store the token or user data in local storage
    setUser(null); // Update state to reflect logged-out status, assuming 'setUser' is passed as a prop

    // Redirect user to the login page
    navigate('/login');
  };

  return (
    <nav id="topNavigation">
      {user ? (
        <>
          <span>Welcome, {user.username}</span>
          <button onClick={handleLogout} className="nav-item">
            Logout
          </button>
        </>
      ) : (
        <>
          <Link to="/login" className="nav-item">
            Login
          </Link>
          <Link to="/register" className="nav-item">
            New User
          </Link>
        </>
      )}
    </nav>
  );
};

export default Nav;
