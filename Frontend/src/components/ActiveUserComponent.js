import React from 'react'
import UserComponent from './UserComponent'
import SetAllocatedVcpuComponent from './SetAllocatedVcpuComponent'
import RunningVcpuComponent from './RunningVcpuComponent';
import RemoveUser from './RemoveUser';

export default function ActiveUserComponent({user}) {
  function setAllocatedVcpus(newNum){
    user.vCPULimit = newNum; 
  }
  return (
    <div className="running-solvers-view">
        <UserComponent user={user}/>
        <RunningVcpuComponent availableVcpus={user.vCPULimit}/>
        <SetAllocatedVcpuComponent setAllocatedVcpus={setAllocatedVcpus}/>
        <RemoveUser username={user.username}/>
    </div>
  )
}
