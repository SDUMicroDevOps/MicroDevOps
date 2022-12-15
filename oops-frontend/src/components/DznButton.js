import React, { useState } from 'react'

const DZN = () => {
  const [selectedFile, setSelectedFile] = useState(null);

  function setFile(e){
    if(e.target.files[0]) {
      setSelectedFile(e.target.files[0])
    }
  }
  
  return (
    <label class="super-button dzn-button">
      {selectedFile === null ? '.DZN' : selectedFile.name}
      <input type="file" onChange={(e) => setFile(e)} hidden></input>
    </label>   
  )
}

export default DZN