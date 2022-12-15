import React from 'react'
import Solver from './Solver'
import RunningVcpuComponent from './RunningVcpuComponent'
import RunningGoCancelComponent from './RunningGoCancelComponent'
import RemoveSolver from './RemoveSolver'

export default function RunningSolverComponent({runSolver, cancelSolver, setAvailable, solver}) {
  return (
    <div className="running-solvers-view">
      <Solver key={solver.id} solver={solver}/>
      <RunningVcpuComponent availableVcpus={solver.id}/>
      <RunningGoCancelComponent runSolver={runSolver} cancelSolver={cancelSolver} solver={solver}/>
      <RemoveSolver setAvailable={setAvailable} solver={solver}/>
    </div>
  )
}
