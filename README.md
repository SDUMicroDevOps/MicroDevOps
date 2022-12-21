This guide assumes you have a created a project in the gcloud project with the name <YourProjectID> and have a billing account connected.
This part will likely still include some debugging:

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

To avoid setting up all of the above, and avoid the debugging that will surely follow, an easier way to setup the cluster is to simply get acess to our already setup project. Since this is a Google Cloud project, all it requires is a google account, then contacting one of the members of the group, who can then add you to the project.

Running the cluster:
NOTE: Even if you added to our project, you should run 'gcloud auth application-default login' to generate ADC:

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
