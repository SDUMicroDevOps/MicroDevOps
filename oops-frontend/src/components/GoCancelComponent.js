import React from 'react'

export default function GoCancelComponent({runAll, cancelAll, running}) {
  function setRunningButton(){
    running ? cancelAll() : runAll();
    console.log("running: " + running);
  }

  return (
    <div onClick={setRunningButton} class={running ? "super-button cancel-view" : "super-button go-view"}>
      {running ? 'Cancel' : 'Go'}
    </div>
  )
}
