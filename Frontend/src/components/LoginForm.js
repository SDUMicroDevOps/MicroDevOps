import React, { useState, useContext, useEffect } from 'react';
import axios from 'axios';
import AuthContext from "../context/AuthProvider";

const LOGIN_URL = 'http://' + process.env.REACT_APP_AUTH_SERVICE + ':' + process.env.REACT_APP_AUTH_PORT + '/login';

export default function LoginForm() {
  const [showLogin, setShowLogin] = useState(false);
  const { auth, setAuth } = useContext(AuthContext);
  const [username, setUsername] = useState('');
  const [pwd, setPwd] = useState('');

  useEffect(() => {
    console.log('auth changed: ' + auth.name + ', ' + auth.type + ', ' + auth.authToken);
  }, [auth]);

  const handleClick = () => {
    setShowLogin(true);
  };

  const resetForm = () => {
    setUsername('');
    setPwd('');
  }

  const handleSubmit = (e) => {
    e.preventDefault();
    const requestBody = {Username: username, Password: pwd}
    axios.put(LOGIN_URL, requestBody)
    .then(function (response) {
      console.log(response);
      const userType = response.data.Type;
      const authToken = response.data.Token;
      setAuth({name: username, type: userType, authToken: authToken});
      console.log('logged in successfully.');
      resetForm();
      setShowLogin(false);
      })
    .catch(function (error) {
      console.log(error);
      console.log('login failed');
    }); 
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
