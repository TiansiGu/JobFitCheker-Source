import React, { useEffect, useState } from "react";
import axios from "axios";
import API_URL from "./config.js";
// for testing delete in the end
function WelcomeMessage() {
  const [message, setMessage] = useState("");

  useEffect(() => {
    axios
      .get(`${API_URL}/api/welcome`) // Make sure the URL matches your server
      .then((response) => {
        setMessage(response.data);
      })
      .catch((error) => {
        console.error(
          "There was an error fetching the welcome message:",
          error
        );
      });
  }, []);

  return (
    <div>
      <p>Here is the backend: {message}</p>
    </div>
  );
}

export default WelcomeMessage;
