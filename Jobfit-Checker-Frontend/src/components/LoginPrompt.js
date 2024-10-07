import React from 'react';
import { Link } from 'react-router-dom';

export default function LoginPrompt() {
  return (
    <div>
      <h2>You need to log in first</h2>
      <p>
        Please <Link to="/login">click here to login</Link>.
      </p>
    </div>
  );
}
