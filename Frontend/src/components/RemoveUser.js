import React from 'react'
import axios from 'axios'
const DELETE_USER_URL = 'http://' + process.env.REACT_APP_DATABASE_SERVICE + ':' + process.env.REACT_APP_DATBASE_PORT + '/users';

const RemoveUser = ({username}) => {
    function handleOnClick(){
        axios.delete(DELETE_USER_URL+username)
        .then(response => {
            console.log(response);
        })
        .catch(error => {
            console.log(error);
        });
    }
  return (
    <button onClick={handleOnClick} className='super-button remove-running-solver-view'>
        Remove
    </button>
  )
}

export default RemoveUser