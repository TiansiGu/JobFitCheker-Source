import React, { useState, useEffect } from "react";
import "../styles/style.css";
import API_URL from "./config.js";

export default function Profile() {
  // Create states to handle inputs and data
  const [user, setUser] = useState({
    firstName: null,
    lastName: null,
    email: null,
    phoneNumber: null,
    degree: null,
    needSponsor: null,
    resumeName: null,
  });
  const [originalUserInfo, setOriginalUserInfo] = useState(null); // State to hold the original user data
  const [resume, setResume] = useState(null); // State to hold the file
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch profile data when the component mounts
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetch(`${API_URL}/api/profile`, {
          credentials: "include", // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error("Failed to fetch profile data");
        }
        const data = await response.json();
        console.log("resume key: " + JSON.stringify(data));
        setUser({
          // userName: data.userName,
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          degree: data.degree,
          needSponsor: data.needSponsor,
          resumeName: data.resumeKey ? data.resumeKey.split("@")[1] : null, // extract the resume name
        });
        // remember the original info
        setOriginalUserInfo({
          email: data.email, //Store the original user data for comparison later
        });
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };
    fetchProfile();
  }, []);

  const checkEmail = async (email) => {
    try {
      const response = await fetch(
        `${API_URL}/api/check-email?email=${encodeURIComponent(email)}`,
        {
          method: "GET",
          headers: {
            "Content-Type": "application/json",
          },
        }
      );

      if (!response.ok) {
        throw new Error("Failed to check email");
      }
      let emailExists = await response.json();
      console.log(emailExists);
      return emailExists; // Return true or false based on the server response
    } catch (e) {
      alert("Error checking email: " + e.message);
    }
  };

  // Handle form submission to update profile information
  const handleSubmit = async (e) => {
    e.preventDefault();

    const action = e.nativeEvent.submitter.value;

    if (action === "Update Profile") {
      // check if the email is already used or not
      if (user.email !== originalUserInfo.email) {
        let isEmailUsed = await checkEmail(user.email);
        if (isEmailUsed) {
          alert("This Email is already used, please use a different Email");
          return;
        }
      }
      try {
        const response = await fetch(`${API_URL}/api/update-profile`, {
          method: "PUT",
          headers: {
            "Content-Type": "application/json",
          },
          body: JSON.stringify(user),
          credentials: "include", // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error("Failed to update profile");
        }
        if (response.ok)
          console.log("set user fields are" + JSON.stringify(user));
        alert("Profile updated successfully");
        //update the original email
        setOriginalUserInfo({ email: user.email });
      } catch (err) {
        alert("Error updating profile: " + err.message);
      }
    } else if (action === "Upload Resume") {
      try {
        const formData = new FormData();
        formData.append("resume", resume);
        const response = await fetch(`${API_URL}/api/upload-resume`, {
          method: "POST",
          body: formData,
          credentials: "include", // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error("Failed to upload resume");
        }
        // Update the resumeName after successful upload
        setUser((prevState) => ({
          ...prevState,
          resumeName: resume.name, // Use the file name from the uploaded resume
        }));
        alert("Resume uploaded successfully");
      } catch (err) {
        alert("Error uploading resume: " + err.message);
      }
    }
  };

  // Handle resume deletion
  const handleDeleteResume = async () => {
    try {
      const response = await fetch(`${API_URL}/api/delete-resume`, {
        method: "DELETE",
        credentials: "include", // Ensures cookies and sessions are included
      });

      if (!response.ok) {
        throw new Error("Failed to delete the resume");
      }
      alert("Deleted resume successfully!");
      setUser((prevState) => ({
        ...prevState,
        resumeName: "", // Clear resume from state after deletion
      }));
    } catch (err) {
      alert("Error deleting resume: " + err.message);
    }
  };

  // Handle input changes
  const handleChange = (e) => {
    const { name, value } = e.target;
    setUser((prevState) => ({ ...prevState, [name]: value }));
  };

  const handleFileChange = (e) => {
    setResume(e.target.files[0]); // Save the selected file
  };

  return (
    <div className="profile-container">
      <h1>My Profile</h1>

      {/* Update User Information Form */}
      <form onSubmit={handleSubmit}>
        <fieldset>
          <legend>
            <h3>Profile Information</h3>
          </legend>

          <label htmlFor="firstName">First Name:</label>
          <input
            type="text"
            id="firstName"
            name="firstName"
            value={user.firstName}
            onChange={handleChange}
            placeholder="Enter your first name"
          />
          <br />
          <br />

          <label htmlFor="lastName">Last Name:</label>
          <input
            type="text"
            id="lastName"
            name="lastName"
            value={user.lastName}
            onChange={handleChange}
            placeholder="Enter your last name"
          />
          <br />
          <br />

          <label htmlFor="email">Email:</label>
          <input
            type="email"
            id="email"
            name="email"
            value={user.email}
            onChange={handleChange}
            placeholder="Enter your email"
          />
          <br />
          <br />

          <label htmlFor="phoneNumber">Phone Number:</label>
          <input
            type="tel"
            id="phoneNumber"
            name="phoneNumber"
            value={user.phoneNumber}
            onChange={handleChange}
            placeholder="Enter your phone number"
          />
          <br />
          <br />

          <label htmlFor="degree">Degree:</label>
          <input
            type="text"
            id="degree"
            name="degree"
            value={user.degree}
            onChange={handleChange}
            placeholder="Enter your degree"
          />
          <br />
          <br />

          <label htmlFor="needSponsor">Need Sponsorship or not: </label>
          <select
            name="needSponsor"
            value={user.needSponsor}
            onChange={handleChange}
          >
            <option value="">Select</option>
            <option value="Yes">Yes</option>
            <option value="No">No</option>
          </select>
        </fieldset>

        <br />
        <input type="submit" value="Update Profile" />

        <br />
        <br />

        {/* Resume Upload Section */}
        <fieldset>
          <legend>
            <h3>Upload Resume</h3>
          </legend>
          {user.resumeName ? (
            <div>
              <p
                style={{
                  fontSize: "0.8em",
                  color: "grey",
                  margin: 0,
                  padding: 0,
                  textAlign: "left",
                }}
              >
                Current Resume: {user.resumeName}
              </p>
              <button type="button" onClick={handleDeleteResume}>
                Delete Resume
              </button>
            </div>
          ) : (
            <p
              style={{
                fontSize: "0.8em",
                color: "grey",
                margin: 0,
                padding: 0,
                textAlign: "left",
              }}
            >
              No resume
            </p>
          )}
          <label htmlFor="resume"></label>
          <input
            type="file"
            id="resume"
            name="resume"
            onChange={handleFileChange}
          />
        </fieldset>

        <br />
        <input type="submit" value="Upload Resume" />
      </form>
    </div>
  );
}
