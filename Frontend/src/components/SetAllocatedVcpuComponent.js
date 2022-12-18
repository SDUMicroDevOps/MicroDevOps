import React from 'react'

const SetAllocatedVcpuComponent = ({setAllocatedVcpus}) => {
    function handleChange(e){
        if(e.key === 'Enter'){
            console.log('enter pressed');
            setAllocatedVcpus(e.target.value);
        } 
    }
  return (
    <div className="vcpu-s-view">
        <form>
            <label>
                <input  type="number"
                        placeholder='crash app'
                        onKeyDown={(e) => handleChange(e)} />
            </label>
        </form>
    </div>
  )
}
export default SetAllocatedVcpuComponent;