import React, { useState } from 'react'

const MZN = () => {
  const [selectedFile, setSelectedFile] = useState(null);

  function setFile(e){
    if(e.target.files[0]) {
      setSelectedFile(e.target.files[0])
    }
  }
  
  return (
    <label class="super-button mzn-button">
      {selectedFile === null ? '.MZN' : selectedFile.name}
      <input type="file" onChange={(e) => setFile(e)} hidden></input>
    </label>   
  )
}

export default MZN