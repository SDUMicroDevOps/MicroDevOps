import React from 'react'
import AdminSolversComponent from './AdminSolversComponent'

export default function AdminSolversWrapper({solvers, removeSolver}) {
  return (
    <div className="solvers-view">
        <AdminSolversComponent solvers={solvers} removeSolver={removeSolver}/>
    </div>
  )
}
