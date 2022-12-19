import './App.css';
import { useState, useContext } from 'react';
import Header from './components/Header';
import LoginForm from './components/LoginForm'
import UserInterfaceComponent from './components/UserInterfaceComponent';
import SignupForm from './components/SignupForm';
import AdminInterfaceComponent from './components/AdminInterfaceComponent';
import AuthContext from "./context/AuthProvider";
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
      <LoginForm />
      <SignupForm />
      { auth.role === 1 ? <UserInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning}/> 
      : auth.role === 2 ? <AdminInterfaceComponent solvers={solvers} setSolvers={setSolvers} running={running} setRunning={setRunning} addSolver={addSolver} removeSolver={removeSolver}/> 
      : <h3>Please sign in to continue</h3>}
    </div>
  );
}

export default App;
