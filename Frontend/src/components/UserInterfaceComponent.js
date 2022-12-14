import React, { useState} from 'react'
import MZN from './MznButton'
import DZN from './DznButton'
import SolversWrapper from './SolversWrapper'
import GoCancelComponent from './GoCancelComponent'
import GetResultComponent from './GetResultComponent'
import VcpuComponent from './VcpuComponent'
import RunningSolversWrapper from './RunningSolversWrapper'

export default function UserInterfaceComponent({solvers, setSolvers, running, setRunning}) {

  function setAvailable(id){
    const newSolvers = [...solvers];
    const solver = newSolvers.find(solver => solver.id === id);
    solver.available = true;
    setSolvers(newSolvers);
  }

  function setUnavailable(id){
    const newSolvers = [...solvers];
    const solver = newSolvers.find(solver => solver.id === id);
    solver.running = false;
    solver.available = false;
    if(!isRunningAnything()){setRunning(false);}
    setSolvers(newSolvers);
  }

  function runAll(){
    setRunning(true);
    setSolvers([
      ...solvers.filter(solver => solver.available === true),
      ...solvers.filter(solver => solver.available === false)
        .map(solver => ({
          ...solver,
          running:true
        }))
    ])
  }

  function cancelAll(){
    setRunning(false);
    setSolvers([
      ...solvers.filter(solver => solver.available === true),
      ...solvers.filter(solver => solver.available === false)
        .map(solver => ({
          ...solver,
          running:false
        }))
    ])
  }

  function runSolver(id){
    setRunning(true);
    const newSolvers = [...solvers];
    const solver = newSolvers.find(solver => solver.id === id);
    solver.running = true;
    setSolvers(newSolvers);
  }

  function cancelSolver(id){
    const newSolvers = [...solvers];
    const solver = newSolvers.find(solver => solver.id === id);
    solver.running = false;
    setSolvers(newSolvers);
    if(!isRunningAnything()){setRunning(false);}
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
    <div className='main-box'>
        <MZN/>
        <DZN/>
        <SolversWrapper setUnavailable={setUnavailable} solvers={solvers.filter(solver => solver.available)}/>
        <RunningSolversWrapper runSolver={runSolver} cancelSolver={cancelSolver} setAvailable={setAvailable} solvers={solvers.filter(solver => !solver.available)}/>
        <GoCancelComponent runAll={runAll} cancelAll={cancelAll} running={running}/>
        <GetResultComponent/>
        <VcpuComponent availableVcpus={5}/>
    </div>
  )
}

