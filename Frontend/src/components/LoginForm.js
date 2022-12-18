import React, { useState, useRef, useEffect, useContext } from 'react';
//import axios from '../api/axios';
import AuthContext from "../context/AuthProvider";
//const LOGIN_URL = '/auth';


export default function LoginForm({ users}) {
  const [showLogin, setShowLogin] = useState(false);
  const { auth, setAuth } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [pwd, setPwd] = useState('');

  const handleClick = () => {
    setShowLogin(true);
  };

  function userExists(username){
    return users.map(user => user.username).includes(username);
  }

  const validLogin = () => {
    return(username !== '' && pwd !== '' && userExists(username));
  }

  const handleSubmit = (e) => {
      e.preventDefault();
      if(validLogin()){
        setAuth(users.find(user => user.username === username));
        setUsername('');
        setPwd('');
        setShowLogin(false);
        console.log(auth);
      }
  }

  return (
    <div>
      {showLogin && (
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
            <button type="submit" onClick={handleSubmit}>Login</button>
          </form>
        </div>
      )}
      <button type="button" className='login-button' onClick={handleClick}>Sign in</button>
    </div>
  );
}
