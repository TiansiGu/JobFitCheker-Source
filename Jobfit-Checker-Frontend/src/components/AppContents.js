import React from 'react';
import Header from './Header';
import Footer from './Footer';
import Profile from './Profile';
import LoginPrompt from './LoginPrompt';
import { Routes, Route, Link } from 'react-router-dom';
import Home from './Home'; // Import the Home component
import ApplicationHistory from './ApplicationHistory';
import CheckQualification from './CheckQualification';
import '../styles/style.css';

export default class AppContent extends React.Component {
  render() {
    const { user, setUser } = this.props;

    return (
      <div id="divMain">
        <Header user={user} setUser={setUser} />

        {/*links */}
          <nav id="sideNavigation">
                  <Link to="/home">Home</Link>
                  <Link to="/profile">My Profile</Link>
                  <Link to="/checkQualification">Check your qualification</Link>
                  <Link to="/applicationHistory">Track Your Application</Link>
          </nav>

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
            <Route
              path="/applicationHistory"
              element={user ? <ApplicationHistory user={user} /> : <LoginPrompt />}
            />
            <Route
              path="/checkQualification"
              element = {user ? <CheckQualification user={{user}}/> : <LoginPrompt />}
            />

          </Routes>
        </article>

        <Footer />
      </div>
    );
  }
}
