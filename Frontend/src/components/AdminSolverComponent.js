import React from 'react'
import Solver from './Solver'
import AdminRemoveSolver from './AdminRemoveSolver'

export default function AdminSolverComponent({solver, removeSolver}) {
  return (
    <div className="admin-running-solvers-view">
      <Solver key={solver.id} solver={solver}/>
      <AdminRemoveSolver solver={solver} removeSolver={removeSolver}/>
    </div>
  )
}
