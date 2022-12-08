import React from 'react'
import SolversComponent from './SolversComponent'

export default function SolversWrapper({setUnavailable, solvers }) {
  return (
    <div class="solvers-view">
        <SolversComponent setUnavailable={setUnavailable} solvers={solvers}/>
    </div>
  )
}
