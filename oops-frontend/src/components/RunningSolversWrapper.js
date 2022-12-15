import React from 'react'
import RunningSolvers from './RunningSolvers'

export default function SolversWrapper({ runSolver, cancelSolver, setAvailable, solvers }) {
  return (
    <div className="super-button cli-view">
        <RunningSolvers runSolver={runSolver} cancelSolver={cancelSolver} setAvailable={setAvailable} solvers={solvers}/>
    </div>
  )
}
