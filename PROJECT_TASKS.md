# Task Status for project

## USER

| Name      | status | Description |
| ----------- | ----------- | ----------- |
| Create username and password      | Completed       |  It is possible to create a new user with a username and a password.
| Create, read, update and delete a .mzn instance   | partially complete        | An user can create, read and delete an instance, however they cannot update them
| Create, read, update and delete a .dzn instance   | partially complete        | An user can create, read and delete an instance, however they cannot update them
| trigger the execution of one or more solvers   | complete        | It is possible for an user to spin up solvers on the kubernetes cluster.
| monitor the termination state of the solver execution | partially completed | It is possible for an user to query for the solution, getting partailly completed solutions, if a solver has posted one |
|given a computaton request, retrieve its result if terminated| partially complete | Solvers write completed solutions to the database, and it is possible for users to retrieve these results, we however forgot to add which solver actually solved the problem |
|cancel the execution of a computation request| Completed| It is possible to cancel requests on a specific request|
|stop a solver for a specific request| partially completed | The endpoint for doing this is available in the backend, and it is unit tested to work locally on minikube, however it has not been tested on GKE|
|minimal GUI support| partially completed| Some functionality is present on the front end, such as creating a user, however there is some parts of this that does not integrate well with the backend|

## ADMIN

| Name      | status | Description |
| ----------- | ----------- | ----------- |
| Monitor and log the platform using a dashboard | Completed | A dashboard with relevant metrics has been setup on Google Dashboards |
| Kill all solver executions started by a user| Partially Complete | The functionallity is in place, and unit tests are succeding, but it has not been tested in production |
| Set resources quota to users | Completed | This can be done directly by interacting with the database |
| Delete a user and all his/her data | Partially Complete | An admin with access to the firebase database can delete users by directly interacting with it.|
| Deploy the system and add new computing nodes in an easy way| Complete | The system can be deployed by running a script, with parameters defined in a sepererate config file. New nodes can be added manually in the Google Cloud Console|
|  Add or remove a solver.  |  Partially Completed  | It is possible to add the solver directly to a Google Bucket, which will cause the solver to be available for use. Support for adding a solver programatically is in place, but not tested. |
| Paragraph   | Text        |

## DEVELOPER

| Name      | status | Description |
| ----------- | ----------- | ----------- |
|   fairness    |   Partially Complete      | Tasks are queued by on default kubernetes scheduler, and all jobs are assigned with a priority of 0.
|Minimal documentation | Partially Complete | Providing Minimal Documentation for deployment to GKE is tough. Documentation for starting the cluster is clear, as it is just running a script. Documentation for setting up a project is not as clear. |
|Provide user stories to explain how the system is intended to be use | Complete | User stories provided in USER_STORIES.md |
| Security | No | Support for Authentication has been implemented in the Backend, and Authentication service, but it is not yet properly implemented. We also have a password in the environemnt file... |
|Have tests to test the system| Partially | The Solver and SolverManager have a few unit tests, but no integration tests have been set up. All in all, a lot of tests are lacking.|
| Scalable | Completed | Running the entire thing on Google Cloud allows us to use their infrastructure to deploy auto scalars. Adding more vCPUs to a request is supported at all levels|
|IaC w. DevOps pipeline| Paritally Completed | Our pipeline runs in Github Actions, and automatically builds and deploys to the cluster on changes merged into the branch. We still lack a automatic deployment to a staging environemtn for running tests |
|Use CI/CD| Complete| Same reason as above|
