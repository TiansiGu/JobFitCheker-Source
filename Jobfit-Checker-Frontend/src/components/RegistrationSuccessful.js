import React, { useEffect } from "react";
import { useNavigate } from "react-router-dom";

function RegistrationSuccessful() {
  const navigate = useNavigate();

  useEffect(() => {
    setTimeout(() => {
      navigate("/");
    }, 5000);
  }, [navigate]);

  return (
    <div className="container">
      <h1>Registration Successful</h1>
      <p>You will be redirected to the home page shortly.</p>
    </div>
  );
}

export default RegistrationSuccessful;
