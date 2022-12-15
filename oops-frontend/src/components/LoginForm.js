import React, { useState, useRef } from 'react';

export default function LoginForm({setType}) {
  const [showLogin, setShowLogin] = useState(false);

  const handleClick = () => {
    setShowLogin(true);
  };

  const handleSubmitUser = () => {
    setShowLogin(false);
    setType('user');
  };

  const handleSubmitAdmin = () => {
    setShowLogin(false);
    setType('admin');
  };

  return (
    <div>
      {showLogin && (
        <div className="login-popup">
          <form>
            <label>
              <input placeholder='Username' type="text" name="username" />
            </label>
            <label>
              <input placeholder='Password' type="password" name="password" />
            </label>
            <button type="submit" onClick={handleSubmitUser}>Login as user</button>
            <button type="submit" onClick={handleSubmitAdmin}>Login as admin</button>
          </form>
        </div>
      )}
      <button type="button" className='login-button' onClick={handleClick}>Sign in</button>
    </div>
  );
}
