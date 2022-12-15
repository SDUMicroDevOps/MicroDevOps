import React, {useState} from 'react'

const AddSolverButton = ({addSolver}) => {
    const [selectedFile, setSelectedFile] = useState(null);

    function setFile(e){
      if(e.target.files[0]) {
        setSelectedFile(e.target.files[0])
        addSolver(e.target.files[0].name);
      }
    }
  return (
    <label className="super-button">
        Add solver
        <input type="file" onChange={(e) => setFile(e)} hidden></input>
    </label>   
  )
}

export default AddSolverButton