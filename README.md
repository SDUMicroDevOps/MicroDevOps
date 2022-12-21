The code for this project can be found here: https://github.com/SDUMicroDevOps/MicroDevOps

This guide assumes you have a created a project in the gcloud project with the name <YourProjectID> and have a billing account connected.

This part is only to be done if the project is your own, meaning you aren't using the project that was already setup by the authers of this README.
Setting up Google Cloud project:

    Download and setup gcloud (https://cloud.google.com/sdk/docs/install#deb):
        1. Add the gcloud CLI distribution URI as a package source. If your distribution supports the signed-by option, run the following command:
            'echo "deb [signed-by=/usr/share/keyrings/cloud.google.gpg] https://packages.cloud.google.com/apt cloud-sdk main" | sudo tee -a /etc/apt/sources.list.d/google-cloud-sdk.list'
        2. Import the Google Cloud public key. If your distribution's apt-key command supports the --keyring argument, run the following command:
            'curl https://packages.cloud.google.com/apt/doc/apt-key.gpg | sudo apt-key --keyring /usr/share/keyrings/cloud.google.gpg add -'
        3. Update and install the gcloud CLI:
            'sudo apt-get update && sudo apt-get install google-cloud-cli'
        4. Run the command:
            'gcloud init'
            and follow the instructions on screen.

    Setup secrets and Application Default Credentials:
    	1. Run the "ClusterSetup/Setup_gcloud.py" python script to enable all needed API's

    	2. Generate application default credentials:
    		'gcloud auth application-default login'
    	   This should generate an ADC file, and tell you the path to which it has been saved, default=/home/<username>/.config/gcloud/application_default_credentials.json

    	3. Go to the Google Cloud Console and search "Service Accounts"
    		i) 		Click the "Compute Engine default service account"
    		ii) 	Click the "Keys" tab, then create a new JSON key. Copy the contents of the key.
    		iii) 	Go to the Secrets Manager, and create a new secret with the contents of the key, and the name <secret_name>

    	4. Go to the IAM tab, and give the 'Compute Engine default service account' the additional roles:
    		i)		Secret Manager Admin
    		ii)		Storage Admin



	Under Workload Identity Federation
		Create "oopspool" and "oopsprovider"
		For pool and provider

	Create Service Accounts
		sacreator
			Cloud Composer API Service Agent
			Composer Worker
			Service Account Admin
			Service Account Key Admin
			Viewer
			Give it a principal permissions
				principal://iam.googleapis.com/projects/859134286483/locations/global/workloadIdentityPools/oopspool/subject/SDUMicroDevOps/MicroDevOps
				with the role "Workload Identity User"
				change repository and project Id as required

		spinnaker-gcs-account
			Eventarc Service Agent
			GKE Hub Service Agent
			Kubernetes Engine Cluster Admin
			Kubernetes Engine Developer
			Kubernetes Engine Viewer
			Storage Admin

	Deploy Spinnaker
		Replace every "859134286483" with your new project number
		Replace every "optical-empire-364322" with your new project id

		Once deployed, 
			execute these 6 commands in two consoles
			gcloud auth login
			gcloud container clusters get-credentials public-cluster --zone europe-north1-a --project optical-empire-364322
			kubectl -n spinnaker port-forward service/spin-deck 9000:9000

			gcloud auth login
			gcloud container clusters get-credentials public-cluster --zone europe-north1-a --project optical-empire-364322
			kubectl -n spinnaker port-forward service/spin-gate 8084:8084

			go to localhost create application oopsdeployer


    Setup the database
    	1. Go into SQL and click CREATE INSTANCE
    	2. Choose MySQL
    	3. Insert all the information for the new instance and also choose the disired configuration (Make sure to use public ip)
    	4. While waiting on it to start. Setup a new bucket and upload the oopsdb.sql file in the SQLQuery folder.
    	5. When it is done set up. Go into the database tap in the new instance.
    	6. Create a new database with disired name.
    	7. Back in the overview click import.
    	8. Select the oops.sql file in the bucket. Select the newly created Database. Click import.
    	9. Back in the project folder. Go to deployments folder and then in the green-peace.yaml change the CONNECTION_NAME with the one that is given in the overview. Change DB_USER to root unless you have created another one you want to use. Change DB_NAME to the database name. Change DB_PASS to the password. (The password will probably later be changed to work with google secrets instead)

The above setup is stuff that need to happen if the project is clean, no setup done. When using the project that this group created, simply skip the above and follow these steps:

Running the cluster:
NOTE: Even if you added to our project, you should run 'gcloud auth application-default login' once to generate ADC:

    1.  Export the following environent variables to your local terminal:
    'export PROJECT_ID=<YourProjectID>' - our project id: optical-empire-364322
    'export CREDENTIAL_SECRET_ID=<secret_name>' - our secret id: default_service_account_key

    2. Install google cloud libraries for python:
        'pip install --upgrade google-api-python-client'
        'pip install --upgrade google-cloud-container'
        'pip install google-cloud-secret-manager'

    3.	Run the ClusterSetup/Setup.py script

    The cluster should now be setting up, and once it's deployed, you should be able to go to the GKE and see the exposed services/endpoints that can be contacted. If the cluster is already running, we don't need to touch it. Any merges made into the github repo will trigger an automatic update of the deployed images.

Contacting the cluster:

1. Posting a new problem:
   curl --location --request POST 'http://<backend-service-ip>/Solve' \
    --header 'Content-Type: application/json' \
    --data-raw '{
   "problemID":"testTask",
   "dataID":"testTask",
   "solversToUse":[
   {
   "numberVCPU": 1,
   "maxMemory": 700,
   "timeout": 150,
   "solverName":"gecode"
   }
   ],
   "userID":"testUser"
   }'
2. Getting a solution to a problem:
   curl --location --request GET 'http://<backend-service-ip>/Result/testTask'
   
# Auth endpoints
## POST to /create 
Endpoint for creating a user. Needs following json body\
`{`\
`"Username":[newUsername]`\
`"Password":[newPassword]`\
`}`\
Returns 200 success. Returns 400 on fail

## Put to /login
Endpoint for getting a token to loging ind with. Needs following json body\
`{`\
`"Username":Username`\
`"Password":Password`\
`}`\
Returns 200 and `{"Token":token}` on a success. Returns 401 on a fail.


# Backend Endpoints

<aside>
📍 All calls for backend have to have this in header:
Application: Bearer <bearerToken>

</aside>

/users - GET

- returns list of all users from the database

/ProblemInstance/{ProblemID} - GET

- returns the mzn data corresponding to the provided problemID

/ProblemInstance - POST (Multipart post request)

- requeres:
    
    key/name: mznFile file: mznFile.mzn
    
    key/name: dznFile file: dznFile.mzn
    
    key/name: UserID file: “userID”
    
- adds Mzn/Dzn data to the Google Storage  with a unique problemID and corresponding UserID, returns problemID.

/DataInstance/{ProblemID} - GET

- *Returns the dzn data corresponding to ProblemID*

/Solver - GET

- returns names of all legal solvers

/Solvers/user/{userID} - GET

- returns the solverID of all running and pending solvers for UserID

/Solvers/task/{TaskID} - GET

- returns all running and pending solvers for a specific taskID

/Solve

```json
{	
	"ProblemID": "ProblemID", 
	"DataID": "DataID", 
	"SolversToUse" : [
	{
		"SolverName": "SolverName",
		"NumberVCPU": "NumberVCPU",
		"MaxMemory": "MaxMemory",
		"TimeOut": "TimeOutPeriod" 
	}
	]
	"UserID" : "UserID"
}
```

- contacts solver manager and tells it to start the solvers defined in solversToUse list. Returns the TaskID of the created task

/Result/{problemID} - GET

- returns the solution from database for the specified problemID

/Cancel/Task/{TaskID} - DELETE

```json
{
	"UserID": "userid"
}
```

- cancel the task with the specific taskID

/Cancel/Solver/{SolverName} - DELETE

```json
{
	"ProblemID": "problemID",
	"UserID": "userId"
}
```

- Cancels the sovler with the taskID

/Cancel/User - DELETE

```json
{
	"UserToCancel":"userID"
}
```

- Cancel all tasks initiated by user
