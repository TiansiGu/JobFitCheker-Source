import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/login.css";
import API_URL from "./config.js";

// let API_URL;

// const hostname = window.location.hostname;

// if (hostname.startsWith("uat.")) {
//   API_URL = "http://uat.tiansiwork.live";
// } else if (hostname.startsWith("ga.")) {
//   API_URL = "http://ga.tiansiwork.live";
// } else {
//   API_URL = "http://uat.tiansiwork.live"; // fallback
// }

console.log("Resolved API_URL:", API_URL);

function Login({ setUser }) {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleSubmit = async (event) => {
    event.preventDefault();
    // Reset error message
    setErrorMessage("");

    try {
      const response = await fetch(`${API_URL}/api/login`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        credentials: "include", // need this for session management to remain logged in

        body: JSON.stringify({ email, password }),
      });

      if (!response.ok) {
        throw new Error("Login failed: " + (await response.text()));
      }

      const data = await response.json();

      // Update user state
      setUser(data);
      console.log("Login Successful");
      navigate("/home");
    } catch (error) {
      setErrorMessage(error.message);
    }
  };

  return (
    <div id="login-container">
      <h1>Login</h1>
      {errorMessage && <p id="error-message">{errorMessage}</p>}
      <form onSubmit={handleSubmit}>
        <label htmlFor="email">
          <b>Email</b>
        </label>
        <br />
        <input
          type="email"
          id="email"
          name="email"
          placeholder="Enter Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
          required
        />
        <br />
        <br />
        <label htmlFor="password">
          <b>Password</b>
        </label>
        <br />
        <input
          type="password"
          id="password"
          name="password"
          placeholder="Enter Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
          required
        />
        <br />
        <br />
        <button type="submit" id="login">
          Login
        </button>
      </form>
      <p>
        Don't have an account? <a href="/register">Create a new account</a>
      </p>
    </div>
  );
}

export default Login;
