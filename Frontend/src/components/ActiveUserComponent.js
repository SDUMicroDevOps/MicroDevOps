import React, {useState} from 'react'
import UserComponent from './UserComponent'
import SetAllocatedVcpuComponent from './SetAllocatedVcpuComponent'
import RunningVcpuComponent from './RunningVcpuComponent';

export default function ActiveUserComponent({user, users, setUsers}) {
  //const [active, setActive] = useState(false);
  function setAllocatedVcpus(newNum){
    user.maxvcpus = newNum; 
    //setActive(false);
  }
  return (
    <div className="running-solvers-view">
        <UserComponent user={user}/>
        <RunningVcpuComponent availableVcpus={user.maxvcpus}/>
        <SetAllocatedVcpuComponent setAllocatedVcpus={setAllocatedVcpus}/>
    </div>
  )
}
