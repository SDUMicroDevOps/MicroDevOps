import React from 'react'
import SolversComponent from './SolversComponent'

export default function SolversWrapper({setUnavailable, solvers}) {
  return (
    <div className="solvers-view">
        <SolversComponent setUnavailable={setUnavailable} solvers={solvers}/>
    </div>
  )
}
