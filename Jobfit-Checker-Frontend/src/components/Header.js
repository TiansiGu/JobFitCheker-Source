import * as React from 'react';
import Nav from './Nav';

export default function Header({ user, setUser }) {
  return (
    <header>
      <h1>Welcome to Job Fit Checker</h1>
      <Nav user={user} setUser={setUser} /> {/* Pass user state to Nav */}
    </header>
  );
}
