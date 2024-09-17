import * as React from 'react';
import Header from './Header';
import AuthContent from './AuthContent';

export default class Appcontent extends React.Component {
  render() {
    return (
      <div>
        <Header />
        <AuthContent />
      </div>
    );
  }
}
