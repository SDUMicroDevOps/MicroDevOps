import './App.css';
import { useState, useContext } from 'react';
import { v4 as uuidv4 } from 'uuid';
import Header from './components/Header';
import LoginForm from './components/LoginForm'
import UserInterfaceComponent from './components/UserInterfaceComponent';
import SignupForm from './components/SignupForm';
import AdminInterfaceComponent from './components/AdminInterfaceComponent';
import AuthContext from "./context/AuthProvider";
import EndUserManager from './classes/EndUserManager';
import AdminManager from './classes/AdminManager';

function App() {
  const testList = [
    {id: uuidv4(), name: 'solver 1', available: true, selected: false, running: false},
    {id: uuidv4(), name: 'solver 2', available: true, selected: false, running: false},
    {id: uuidv4(), name: 'solver 3', available: true, selected: false, running: false},
    {id: uuidv4(), name: 'solver 4', available: true, selected: false, running: false},
    {id: uuidv4(), name: 'solver 5', available: true, selected: false, running: false}
  ];
  const { auth } = useContext(AuthContext);
  const [users, setUsers] = useState([]);
  const [solvers, setSolvers] = useState(testList);
  const [running, setRunning] = useState(false);

  function addSolver(name){
    const newElement = {
      id: uuidv4(), 
      name: name, 
      available: true, 
      selected: false, 
      running: false}
    setSolvers([...solvers, newElement]);
  }

  function removeSolver(id){
    setSolvers(solvers.filter(solver => solver.id !== id));
  }

  function userExists(username){
    return users.map(user => user.username).includes(username);
  }

  return (
    <div className="App">
      <Header className="App-header"/>
      <LoginForm userExists={userExists} users={users}/>
      <SignupForm userExists={userExists} users={users} setUsers={setUsers} />
      { auth instanceof EndUserManager ? <UserInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning}/> 
      : auth instanceof AdminManager ? <AdminInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning} addSolver={addSolver} removeSolver={removeSolver}/> 
      : <h3>Please sign in to continue</h3>}
    </div>
  );
}

export default App;
