import React from 'react'

export default function UserComponent({user}) {
  return (
    <div className='solver-view'>
        {user.username}
    </div>
  )
}
