import React from 'react'
import SolversWrapper from './SolversWrapper'
import AddSolverButton from './AddSolverButton';
import AdminSolversWrapper from './AdminSolversWrapper';

export default function AdminInterfaceComponent({solvers, setSolvers, running, setRunning, addSolver, removeSolver}) {
    function setUnavailable(id){
        const newSolvers = [...solvers];
        const solver = newSolvers.find(solver => solver.id === id);
        solver.running = false;
        solver.available = false;
        if(!isRunningAnything()){setRunning(false);}
        setSolvers(newSolvers);
    }
    function isRunningAnything(){
        var running = false;
        const values = [...solvers];
        values.forEach(function(solver){
          if(solver.running){
            running = true;
          }
        })
        return running;
    }
  return (
    <div className='admin-box'>
        <AddSolverButton addSolver={addSolver}/>
        <AdminSolversWrapper solvers={solvers} removeSolver={removeSolver}/>
    </div>
  )
}
