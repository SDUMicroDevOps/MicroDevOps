import React from 'react'

const VcpuComponent = ({availableVcpus}) => {
  return (
    <div className="super-button vcpu-s-view">vCPUs: {availableVcpus}</div>
  )
}

export default VcpuComponent