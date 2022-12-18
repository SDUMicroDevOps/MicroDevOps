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
import SolverManager from './classes/SolverManager';

function App() {
  const testList = [
    new SolverManager('solver 1'),
    new SolverManager('solver 2'),
    new SolverManager('solver 3'),
    new SolverManager('solver 4'),
    new SolverManager('solver 5')
  ];
  const { auth } = useContext(AuthContext);
  const [users, setUsers] = useState([new EndUserManager("user"), new AdminManager("a")]);
  const [solvers, setSolvers] = useState(testList);
  const [running, setRunning] = useState(false);

  function addSolver(name){
    setSolvers([...solvers, new SolverManager(name)]);
  }

  function removeSolver(id){
    setSolvers(solvers.filter(solver => solver.id !== id));
  }

  return (
    <div className="App">
      <Header className="App-header"/>
      <LoginForm users={users}/>
      <SignupForm users={users} setUsers={setUsers} />
      { auth instanceof EndUserManager ? <UserInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning}/> 
      : auth instanceof AdminManager ? <AdminInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning} addSolver={addSolver} removeSolver={removeSolver} users={users} setUsers={setUsers}/> 
      : <h3>Please sign in to continue</h3>}
    </div>
  );
}

export default App;
