# Solution Manager

API CALLS:

- /SolutionFound - Contacts the DB service and tells it to store new solution
- /Result/{taskID} - Returns the current content of the task in the Solutions table,
  as well as whether it is optimal. Returns null if the TaskID is
  not yet in the table.

start the microservice by running ./mvnw dependency:resolve and then ./mvnw spring-boot:run
