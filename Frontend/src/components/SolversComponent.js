import React from 'react'
import Solver from './Solver'

export default function SolversComponent( {setUnavailable, solvers } ) {
  return (
    solvers.map( solver => {
        return <Solver setUnavailable={setUnavailable} key={solver.id} solver={solver}/>
    })
  )
}
