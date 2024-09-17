import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import '../styles/login.css'; // Make sure the path matches where your CSS file is located

function Login() {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [errorMessage, setErrorMessage] = useState('');
  const navigate = useNavigate();

  const handleSubmit = (event) => {
    event.preventDefault();
    // Here you would typically handle your login logic, API call etc.
    if (email === 'test@example.com' && password === 'password') {
      navigate('/dashboard'); // Redirect to dashboard or some other route on success
    } else {
      setErrorMessage('Invalid email or password');
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
