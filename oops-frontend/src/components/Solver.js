import React from 'react'

export default function Solver({setUnavailable, solver}) {
  function handleOnClick(){
    setUnavailable(solver.id);
  }
  return (
    <div onClick={handleOnClick} class="solver-view">
      {solver.name}
  </div>
  )
}

