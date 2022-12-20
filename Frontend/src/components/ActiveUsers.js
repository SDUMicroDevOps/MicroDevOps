import React from 'react'
import axios from 'axios'
import ActiveUserComponent from './ActiveUserComponent'
const ALL_USERS_URL = '34.88.214.181:5600/Users' 

export default function ActiveUsers() {
    function getUsers(){
        axios.get(ALL_USERS_URL)
        .then(response => {
            console.log(response);
            return response.data;
        })
        .catch(error => {
            console.log(error);
            return [];
        });
    }
    const users = getUsers();
    return (
        users.map( user => { return <ActiveUserComponent key={user.username} user={user}/>}) 
    )
}
