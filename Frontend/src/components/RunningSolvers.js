import React from 'react'
import RunningSolverComponent from './RunningSolverComponent'

const RunningSolvers = ({runSolver, cancelSolver, setAvailable, solvers}) => {
  return (
    solvers.map( solver => {
      return <RunningSolverComponent runSolver={runSolver} cancelSolver={cancelSolver} setAvailable={setAvailable} key={solver.id} solver={solver}/>
    })
  )
}

export default RunningSolvers