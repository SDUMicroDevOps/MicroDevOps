import React, {useState, useEffect} from 'react'
import axios from 'axios'
import ActiveUserComponent from './ActiveUserComponent'
const ALL_USERS_URL = process.env.REACT_APP_BACKEND_SERVICE + ':' + process.env.REACT_APP_BACKEND_PORT + '/Users'

export default function ActiveUsers() {
    const [users, setUsers] = useState([]);
    
    function fillUsers(responseData){
        setUsers(responseData);
    }

    useEffect(() => {
        const getUsers = async () => {
            try {
                const response = await axios.get(ALL_USERS_URL);
                console.log(response.body.data);
                fillUsers(response.body.data);
            } catch (error) {
                console.log(error);
            }
        }
        getUsers();
    }, [])  

    return (
        users?.map( user => { return <ActiveUserComponent key={user.username} username={user}/>}) 
    )
}