import React, {useContext} from 'react'
import AuthContext from '../context/AuthProvider';

export default function Solver({setUnavailable, solver}) {
  const { auth } = useContext(AuthContext);
  function handleOnClick(){
    if(auth === 'endUser'){ setUnavailable(solver.id) };
  }
  return (
    <div onClick={handleOnClick} className="solver-view">
        {solver.name}
    </div>
  )
}

