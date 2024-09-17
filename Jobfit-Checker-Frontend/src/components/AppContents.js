import React from 'react';
import Header from './Header';
import Footer from './Footer';
import { Routes, Route } from 'react-router-dom';
import AuthContent from './AuthContent';
import '../styles/style.css';

export default class AppContent extends React.Component {
  render() {
    return (
      <div id="divMain">
        <Header />
        <div style={{ display: 'flex', flexDirection: 'row' }}>
          <article id="contents" style={{ flex: 1 }}>
            <Routes>
              {' '}
              {/* Define your routes within this Routes component */}
              <Route path="/" element={<AuthContent />} />
            </Routes>
          </article>
        </div>
        <Footer />
      </div>
    );
  }
}
