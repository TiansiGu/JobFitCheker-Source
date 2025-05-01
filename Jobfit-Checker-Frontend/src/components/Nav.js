import React, { useEffect } from "react";
import { Link, useNavigate } from "react-router-dom"; // Import useNavigate for redirection
import axios from "axios";
import "../styles/style.css";
import API_URL from "./config.js";

const Nav = ({ user, setUser }) => {
  const navigate = useNavigate(); // Hook for navigation

  useEffect(() => {
    axios
      .get(`${API_URL}/api/currentUser`, {
        withCredentials: true,
      })
      .then((response) => {
        setUser({ username: response.data });
        console.log("User object:", user); // for checking purpose
        console.log("All user properties:", JSON.stringify(user, null, 2)); // for checking purpose. below parts too
        console.log("respoonse: " + response.data);
        console.log("user: " + user.username);
        console.log("userEmail: " + user.email);
      })
      .catch((error) => {
        console.error("Error fetching user info", error);
        setUser(null);
      });
  }, [setUser]);

  // Track changes to 'user' and log the updated value
  useEffect(() => {
    if (user) {
      console.log("Updated user:", user);
    }
  }, [user]);

  const handleLogout = () => {
    setUser(null); // Update state to reflect logged-out status

    // Redirect user to the login page
    navigate("/login");
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
