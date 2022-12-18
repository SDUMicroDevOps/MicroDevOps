import React, { useState } from 'react';
import EndUserManager from '../classes/EndUserManager';
import AdminManager from '../classes/AdminManager';

export default function SignupForm({users, setUsers}) {
  const [showSignup, setShowSignup] = useState(false);
  const [username, setUsername] = useState('');
  const [pwd, setPwd] = useState('');

  function createUser(username, role){
    const newUser = role === 'admin' ? new AdminManager(username) : new EndUserManager(username);
    if(!users.map(user => user.username).includes(username)){
      setUsers([...users, newUser]);
      console.log('user created: ' + newUser.username);
      return true;
    } else {
      setShowSignup(false);
      return false;
    }
  }

  const handleClick = () => {
    setShowSignup(true);
  };

  const handleSubmitEndUser = (e) => {
    if(createUser(username, 'endUser')){
      setUsername('');
      setPwd('');
      setShowSignup(false)
    }
  };

  const handleSubmitAdmin = (e) => {
    if(createUser(username, 'admin')){
      setUsername('');
      setPwd('');
      setShowSignup(false);
    }
  };

  return (
    <div>
      {showSignup && (
        <div className="login-popup">
          <form>
            <label>
              <input  placeholder='Username'
                      type="text"
                      name="username" 
                      autoComplete="off"
                      onChange={(e) => setUsername(e.target.value)}
                      value={username}
                      required />
            </label>
            <label>
              <input  placeholder='Password'
                      type="password" 
                      name="password" 
                      onChange={(e) => setPwd(e.target.value)}
                      value={pwd}
                      required />
            </label>
            <button type="button" onClick={handleSubmitEndUser}>Signup user</button>
            <button type="button" onClick={handleSubmitAdmin}>Signup admin</button>
          </form>
        </div>
      )}
      <button type="button" className='signup-button' onClick={handleClick}>Sign up</button>
    </div>
  );
}
