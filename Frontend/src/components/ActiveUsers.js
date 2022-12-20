import React from 'react'
import axios from 'axios'
import ActiveUserComponent from './ActiveUserComponent'
const ALL_USERS_URL = 'http://' + process.env.DATABASE_SERVICE + ':' + process.env.DATBASE_PORT + '/users';

export default function ActiveUsers() {
    const users = axios.get(ALL_USERS_URL)
        .then(response => {
            console.log(response);
            return response.data;
        })
        .catch(error => {
            console.log(error);
            return [];
        });
    return (
        users.map( user => { return <ActiveUserComponent key={user.username} user={user}/>}
        )
    )
}
