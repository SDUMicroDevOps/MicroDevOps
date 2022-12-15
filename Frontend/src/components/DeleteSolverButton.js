import React, {useState} from 'react'

export default function DeleteSolverButton() {
    const [selectedFile, setSelectedFile] = useState(null);

    function setFile(e){
      if(e.target.files[0]) {
        setSelectedFile(e.target.files[0])
      }
    }
  return (
    <label className="super-button dzn-button">
        {selectedFile === null ? 'Add Solver' : selectedFile.name}
        <input type="file" onChange={(e) => setFile(e)} hidden></input>
    </label>   
  )
}
