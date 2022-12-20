import React, {useContext} from 'react'
import AuthContext from '../context/AuthProvider'

const Header = () => {
  const { auth } = useContext(AuthContext);
  return (
  <div>
    <h1>Welcome {auth.name === '' ? 'to DevOOPS' : auth.name + '!'}</h1>
  </div>    
  )
}

export default Header