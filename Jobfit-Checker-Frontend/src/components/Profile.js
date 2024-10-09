import React, { useState, useEffect } from "react"; // Make sure to import useState and useEffect

export default function Profile() {
  // Create local state to handle form inputs and profile data
  const [user, setUser] = useState({
    firstName: null,
    lastName: null,
    email: null,
    phoneNumber: null,
    degree: null,
    needSponsor: null,
  });
  const [resume, setResume] = useState(null); // State to hold the file

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch profile data when the component mounts
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetch("http://localhost:8080/profile", {
          credentials: "include", // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error("Failed to fetch profile data");
        }
        const data = await response.json();
        setUser({
          // userName: data.userName,
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          degree: data.degree,
          needSponsor: data.needSponsor
        });
        setLoading(false);
      } catch (err) {
        setError(err.message);
        setLoading(false);
      }
    };

    fetchProfile();
  }, []); // Empty dependency array ensures this runs once on mount

  // Handle form submission to update profile information
  const handleSubmit = async (e) => {
    e.preventDefault();

    const action = e.nativeEvent.submitter.value;

    if (action === "Update Profile") {
      try {
        const response = await fetch("http://localhost:8080/update-profile", {
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
      } catch (err) {
        alert("Error updating profile: " + err.message);
      }
    } else if (action === "Upload Resume") {
      try {
        const formData = new FormData();
        formData.append("resume", resume);
        const response = await fetch("http://localhost:8080/upload-resume", {
          method: "POST",
          body: formData,
          credentials: "include", // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error("Failed to upload resume");
        }
        alert("Resume uploaded successfully");
      } catch (err) {
        alert("Error uploading resume: " + err.message);
      }
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

  // Render loading or error state
  if (loading) return <p>Loading...</p>;
  if (error) return <p>Error: {error}</p>;

  return (
    <div className="profile-container">
      <h1>My Profile</h1>

      {/* Update User Information Form */}
      <form onSubmit={handleSubmit}>
        <label htmlFor="firstName">First Name:</label>
        <input
          type="text"
          id="firstName"
          name="firstName"
          value={user.firstName}
          onChange={handleChange}
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
        />
        <br />
        <br />
        <div style={{ display: "flex", alignItems: "center", gap: "20px" }}>
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
        </div>
        <br />
        <br />
        <input type="submit" value="Update Profile" />
        <br />
        <file></file>
        <br />
        <br />
        <label htmlFor="resume">Update Resume:</label>
        <br />
        <input
          type="file"
          id="resume"
          name="resume"
          onChange={handleFileChange}
        />
        <br />
        <br />
        <input type="submit" value="Upload Resume" />
      </form>
    </div>
  );
}
