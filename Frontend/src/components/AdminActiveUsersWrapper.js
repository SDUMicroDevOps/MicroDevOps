import React from 'react'
import ActiveUsers from './ActiveUsers'

export default function AdminActiveUsersWrapper() {
  return await (
    <div className="super-button active-users-view">
        <ActiveUsers/>
    </div>
  )
}
