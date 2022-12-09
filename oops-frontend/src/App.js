import './App.css';
import { useState } from 'react';
import Header from './components/Header';
import LoginForm from './components/LoginForm'
import UserInterfaceComponent from './components/UserInterfaceComponent';
import SignupForm from './components/SignupForm';

function App() {
  const [userType, setUserType] = useState(null); 
  
  function setType(type){
    setUserType(type);
  }

  return (
    <div class="App">
      <Header class="App-header"/>
      <LoginForm setType={setType}/>
      <SignupForm/>
      { userType === 'user' ? <UserInterfaceComponent/> 
      : userType === 'admin' ? <h3>Admin interface not implemented</h3>
      : <h3>Please sign in to continue</h3>}
    </div>
  );
}

export default App;
