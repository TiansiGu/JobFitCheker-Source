import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import "../styles/register.css";
import API_URL from "./config.js";

function Register() {
  const [formData, setFormData] = useState({
    username: "",
    email: "",
    password: "",
    repeatPassword: "",
  });
  const [errorMessage, setErrorMessage] = useState("");
  const navigate = useNavigate();

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prev) => ({
      ...prev,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    if (formData.password !== formData.repeatPassword) {
      setErrorMessage("Passwords do not match");
      return;
    }
    setErrorMessage("");

    try {
      const response = await fetch(`${API_URL}/api/register`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify({
          username: formData.username,
          email: formData.email,
          password: formData.password,
        }),
      });

      console.log(response);
      if (response.status === 201) {
        console.log("Line 45 is reached");
        navigate("/registrationSuccessful"); // go to successful registration page
      } else if (response.status === 409) {
        setErrorMessage(
          "This email already has an account. Please login instead"
        );
      } else {
        const data = await response.json();
        setErrorMessage(data.message || "Registration failed");
      }
    } catch (error) {
      setErrorMessage("An error occurred during registration");
    }
  };

  return (
    <div id="register-container">
      <h1>Register</h1>
      <p>Please fill in this form to create an account.</p>
      <hr />
      {errorMessage && <p style={{ color: "red" }}>{errorMessage}</p>}
      <form onSubmit={handleSubmit}>
        <label htmlFor="username">
          <b>User Name</b>
        </label>
        <input
          type="text"
          id="username"
          name="username"
          placeholder="Enter your user name"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <label htmlFor="email">
          <b>Email</b>
        </label>
        <input
          type="email"
          id="email"
          name="email"
          placeholder="Enter Email"
          value={formData.email}
          onChange={handleChange}
          required
        />
        <label htmlFor="password">
          <b>Password</b>
        </label>
        <input
          type="password"
          id="password"
          name="password"
          placeholder="Enter Password"
          required
          value={formData.password}
          onChange={handleChange}
        />
        <label htmlFor="psw_repeat">
          <b>Repeat Password</b>
        </label>
        <input
          type="password"
          name="repeatPassword"
          id="psw_repeat"
          placeholder="Repeat Password"
          required
          value={formData.repeatPassword}
          onChange={handleChange}
        />
        <button type="submit" id="register">
          Register
        </button>
      </form>
      <p>
        Already have an account? <a href="/login">Log in</a>
      </p>
    </div>
  );
}

export default Register;
