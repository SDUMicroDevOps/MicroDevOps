import React, { Component } from 'react';
import UserComponent from './UserComponent';
import SetAllocatedVcpuComponent from './SetAllocatedVcpuComponent';
import RunningVcpuComponent from './RunningVcpuComponent';
import RemoveUser from './RemoveUser';

export default class ActiveUserComponent extends Component {
  constructor(props) {
    super(props);
    this.state = {
      user: props.user
    };
  }

  setAllocatedVcpus = newNum => {
    this.setState({
      user: {
        ...this.state.user,
        vCPULimit: newNum
      }
    });
  }

  render() {
    const { user } = this.state;
    return (
      <div className="running-solvers-view">
        <UserComponent user={user} />
        <RunningVcpuComponent availableVcpus={user.vCPULimit} />
        <SetAllocatedVcpuComponent setAllocatedVcpus={this.setAllocatedVcpus} />
        <RemoveUser username={user.username} />
      </div>
    );
  }
}
