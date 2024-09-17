import React, { useState } from 'react';
import '../styles/register.css';

function Register() {
  const [formData, setFormData] = useState({
    username: '',
    email: '',
    password: '',
    repeatPassword: '',
  });
  const [errorMessage, setErrorMessage] = useState('');

  const handleChange = (e) => {
    const { name, value } = e.target;
    setFormData((prevState) => ({
      ...prevState,
      [name]: value,
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    // Example validation
    if (formData.password !== formData.repeatPassword) {
      setErrorMessage('Passwords do not match');
      return;
    }
    setErrorMessage('');
    // POST request to server would go here
  };

  return (
    <div id="register-container">
      <h1>Register</h1>
      <p>Please fill in this form to create an account.</p>
      <hr />
      {errorMessage && <p style={{ color: 'red' }}>{errorMessage}</p>}
      <form onSubmit={handleSubmit}>
        <label htmlFor="username">
          <b>Name</b>
        </label>
        <br />
        <input
          type="text"
          id="username"
          name="username"
          placeholder="Enter your name"
          value={formData.username}
          onChange={handleChange}
          required
        />
        <br />
        <br />
        <label htmlFor="email">
          <b>Email</b>
        </label>
        <br />
        <input
          type="email"
          id="email"
          name="email"
          placeholder="Enter Email"
          value={formData.email}
          onChange={handleChange}
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
          required
          value={formData.password}
          onChange={handleChange}
        />
        <br />
        <br />
        <label htmlFor="psw_repeat">
          <b>Repeat Password</b>
        </label>
        <br />
        <input
          type="password"
          name="repeatPassword"
          id="psw_repeat"
          placeholder="Repeat Password"
          required
          value={formData.repeatPassword}
          onChange={handleChange}
        />
        <br />
        <br />
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
