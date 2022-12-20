import React, { useState } from 'react';
import axios from 'axios';
const SIGNUP_URL = 'http://' + process.env.REACT_APP_AUTH_SERVICE + ':' + process.env.REACT_APP_AUTH_PORT + '/create';

export default function SignupForm() {
  const [showSignup, setShowSignup] = useState(false);
  const [username, setUsername] = useState('');
  const [pwd, setPwd] = useState('');

  function createUser(username, type){
    console.log('SIGNUP URL: ' + SIGNUP_URL)
    //types: 1: endUser, 2: admin
    axios.post(SIGNUP_URL, {
      Username: username,
      Password: pwd,
      Type: type
    })
    .then(function (response) {
      console.log(response);
      console.log('user created: ' + username);
      setShowSignup(false);
      return true;
    })
    .catch(function (error) {
      console.log(error);
      console.log('user not created: ' + username);
      return false;
    });
  }
  const handleClick = () => {
    setShowSignup(true);
  };

  const handleSubmitEndUser = (e) => {
    if(createUser(username, 1)){
      setUsername('');
      setPwd('');
      setShowSignup(false)
    }
  };

  const handleSubmitAdmin = (e) => {
    if(createUser(username, 2)){
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
