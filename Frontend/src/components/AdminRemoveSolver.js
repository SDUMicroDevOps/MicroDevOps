import React from 'react'

export default function AdminRemoveSolver({solver, removeSolver}) {
  function handleOnClick(){
    removeSolver(solver.id);
  }

  return (
    <button onClick={handleOnClick} className='super-button remove-running-solver-view'>
        Remove
    </button>
  )
}
