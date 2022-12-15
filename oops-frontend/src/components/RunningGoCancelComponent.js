import React from 'react'

export default function RunningGoCancelComponent({runSolver, cancelSolver, solver}) {
  function setRunningButton(){
    solver.running ? cancelSolver(solver.id) : runSolver(solver.id);
  }

  return (
    <div onClick={setRunningButton} className={solver.running ? "super-button running-cancel-view" : "super-button running-go-view"}>
      {solver.running ? 'Cancel' : 'Go'}
    </div>
  )
}
