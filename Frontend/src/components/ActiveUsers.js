import React from 'react'
import axios from 'axios'
import ActiveUserComponent from './ActiveUserComponent'
const ALL_USERS_URL = process.env.REACT_APP_BACKEND_SERVICE + ':' + process.env.REACT_APP_BACKEND_PORT + '/Users'

export default async function ActiveUsers() {
    async function getUsers(){
        try {
            const response = await axios.get(ALL_USERS_URL);
            console.log(response.body.data);
            return response.body.data.map(user => {
                return {
                    username: user.username,
                    vCPULimit: user.vCPULimit
                };
            });
        } catch (error) {
            console.log(error);
            return [];
        }
    }
    const users = await getUsers();
    return (
        users.map( user => { return <ActiveUserComponent key={user.username} username={user}/>}) 
    )
}
