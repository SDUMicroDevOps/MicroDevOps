import React from 'react'
import AdminSolver from './AdminSolver'
import AdminRemoveSolver from './AdminRemoveSolver'

export default function RunningSolverComponent({solver, removeSolver}) {
  return (
    <div className="running-solvers-view">
      <AdminSolver key={solver.id} solver={solver}/>
      <AdminRemoveSolver solver={solver} removeSolver={removeSolver}/>
    </div>
  )
}
