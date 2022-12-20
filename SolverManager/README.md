# Solver Manager for the OOPS solution
The solver manager for the oops solution is responsible for creating kubernetes jobs, for solving linear programming problems. Given a problem iD , and an id for the solver to use, it will generate jobs using the **Solver** docker image.

## Configuring the Sovler Manager
The solver manager provides an kubernetes deployment script and an kubernetes service script, that can be used to create an functioning instance. For it to work correct a service account for the solver manager needs to be configured on the cluster. this can be done using the following commands:
1. **kubectl create serviceaccount solvermanager**
2. **kubectl create clusterrolebinding solvermanager-role --clusterrole=edit --serviceaccount=default:solvermanager --namespace=default**
