import React from 'react'

export default function RemoveSolver({setAvailable, solver}) {
  function handleOnClick(){
    setAvailable(solver.id);
  }

  return (
    <button onClick={handleOnClick} className='super-button remove-running-solver-view'>
        Remove
    </button>
  )
}
