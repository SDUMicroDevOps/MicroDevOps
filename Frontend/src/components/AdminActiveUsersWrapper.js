import React from 'react'
import ActiveUsers from './ActiveUsers'

export default function AdminActiveUsersWrapper({users, setUsers}) {
  return (
    <div className="super-button active-users-view">
        <ActiveUsers users={users} setUsers={setUsers}/>
    </div>
  )
}
