import React, { useState, useEffect } from 'react'; // Make sure to import useState and useEffect

export default function Profile() {
  // Create local state to handle form inputs and profile data
  const [user, setUser] = useState({
    userName: '',
    firstName: '',
    lastName: '',
    email: '',
    phoneNumber: '',
    degree: '',
  });
  const [resume, setResume] = useState(null); // State to hold the file

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);

  // Fetch profile data when the component mounts
  useEffect(() => {
    const fetchProfile = async () => {
      try {
        const response = await fetch('http://localhost:8080/profile', {
          credentials: 'include', // Ensures cookies and sessions are included
        });
        if (!response.ok) {
          throw new Error('Failed to fetch profile data');
        }
        const data = await response.json();
        setUser({
          userName: data.userName,
          firstName: data.firstName,
          lastName: data.lastName,
          email: data.email,
          phoneNumber: data.phoneNumber,
          degree: data.degree,
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
    try {
      const response = await fetch('http://localhost:8080/profile', {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json',
        },
        body: JSON.stringify(user),
      });
      if (!response.ok) {
        throw new Error('Failed to update profile');
      }
      alert('Profile updated successfully');
    } catch (err) {
      alert('Error updating profile: ' + err.message);
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
      <h1>Profile Page</h1>
      <div className="profile-details">
        <p>
          <strong>User Name:</strong> {user.userName || 'Guest'}
        </p>
        <p>
          <strong>Name:</strong> {user.firstName || 'Guest'}{' '}
          {user.lastName || ''}
        </p>
        <p>
          <strong>Email:</strong> {user.email || 'Not provided'}
        </p>
        <p>
          <strong>Phone:</strong> {user.phoneNumber || 'Not provided'}
        </p>
        <p>
          <strong>Degree:</strong> {user.degree || 'Not provided'}
        </p>
      </div>

      {/* Update User Information Form */}
      <form onSubmit={handleSubmit}>
        <label htmlFor="firstName">First Name:</label>
        <br />
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
        <br />
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
        <br />
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
        <br />
        <input
          type="tel"
          id="phoneNumber"
          name="phoneNumber"
          value={user.phoneNumber}
          onChange={handleChange}
        />
        <br />
        <br />
        <label htmlFor="degree">Degree Type:</label>
        <br />
        <input
          type="text"
          id="degree"
          name="degree"
          value={user.degree}
          onChange={handleChange}
        />
        <br />
        <br />
        <label htmlFor="resume">Update Resume:</label>
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
        <input type="submit" value="Update Profile" />
      </form>
    </div>
  );
}
