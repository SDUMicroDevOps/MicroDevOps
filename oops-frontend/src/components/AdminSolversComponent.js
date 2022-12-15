import React from 'react'
import Solver from './Solver'
import AdminSolverComponent from './AdminSolverComponent'

export default function AdminSolversComponent( { solvers, removeSolver } ) {
  return (
    solvers.map( solver => {
        return <AdminSolverComponent key={solver.id} solver={solver} removeSolver={removeSolver}/>
    })
  )
}
