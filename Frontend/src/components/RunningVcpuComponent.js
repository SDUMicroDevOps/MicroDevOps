import React from 'react'

const RunningVcpuComponent = ({availableVcpus}) => {
  return (
    <div className="super-button vcpu-s-view">vCPUs: {availableVcpus}</div>
  )
}

export default RunningVcpuComponent