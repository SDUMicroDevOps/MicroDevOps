import React, { useState, useRef } from 'react';

export default function SignupForm() {
  const [showSignup, setShowSignup] = useState(false);

  const handleClick = () => {
    setShowSignup(true);
  };

  const handleSubmitUser = () => {
    setShowSignup(false);
  };

  const handleSubmitAdmin = () => {
    setShowSignup(false);
  };

  return (
    <div>
      {showSignup && (
        <div className="login-popup">
          <form>
            <label>
              <input placeholder='Username' type="text" name="username" />
            </label>
            <label>
              <input placeholder='Password' type="password" name="password" />
            </label>
            <button type="submit" onClick={handleSubmitUser}>Signup as user</button>
            <button type="submit" onClick={handleSubmitAdmin}>Signup as admin</button>
          </form>
        </div>
      )}
      <button type="button" className='signup-button' onClick={handleClick}>Sign up</button>
    </div>
  );
}
