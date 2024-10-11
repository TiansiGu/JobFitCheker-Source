// AppContent.js
import React from 'react';
import Header from './Header';
import Footer from './Footer';
import Profile from './Profile';
import LoginPrompt from './LoginPrompt';
import { Routes, Route, Link } from 'react-router-dom';
import Home from './Home'; // Import the Home component
import '../styles/style.css';

export default class AppContent extends React.Component {
  render() {
    const { user, setUser } = this.props;

    return (
      <div id="divMain">
        <Header user={user} setUser={setUser} />

        {/* Navigation links */}
        <div
          id="nav"
          style={{ display: 'flex', flexDirection: 'column', gap: '10px' }}
        >
           <Link to="/home">Home</Link>
          {/*<Link to="/home">Home</Link>*/}
          <Link to="/profile">My Profile</Link>
          <Link to="/track-application">Track Your Application</Link>
        </div>

        {/* Main content area for routes */}
        <article id="contents" style={{ flex: 1 }}>
          <Routes>
            {/* Home Route */}
            <Route path="/home" element={<Home />} />
            <Route path="/" element={<Home />} />

            {/* Profile Route - Conditional rendering based on user login */}
            <Route
              path="/profile"
              element={user ? <Profile user={user} /> : <LoginPrompt />}
            />

            {/* Other routes can be added here */}
          </Routes>
        </article>

        <Footer />
      </div>
    );
  }
}
