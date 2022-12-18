import React from 'react'
import EndUserManager from '../classes/EndUserManager'
import ActiveUserComponent from './ActiveUserComponent'

export default function ActiveUsers({users, setUsers}) {
    return (
        users.map( user => {
            if(user instanceof EndUserManager){
                return <ActiveUserComponent key={user.username} user={user} users={users} setUsers={setUsers}/>
            }
        })
    )
}
