#Not working? Try these
#pip install --upgrade google-api-python-client
#pip install --upgrade google-cloud-container
#pip install google-cloud-secret-manager
import json
from time import sleep
from google.cloud import container_v1
from google.cloud import secretmanager
from config import *

def get_client(logger):
    #Secrets manager client to get the secret credentials file:
    logger("Creating secrets manager")
    manager = secretmanager.SecretManagerServiceClient()
    name = f"projects/{project_id}/secrets/{secret_id}/versions/1"

    logger("Retrieving secret")
    response = manager.access_secret_version(request={"name": name})

    credentials = json.loads(response.payload.data)

    #Client is required to connect to the cloud. Each API has a seperate client.
    #The environemnt variable GOOGLE_APPLICATION_CREDENTIALS needs to point to an application_default_credentials file.
    logger("Creating cluster manager")
    return container_v1.ClusterManagerClient.from_service_account_info(credentials)


def create_cluster(client : container_v1.ClusterManagerClient, logger):
    # Build the response. parameters are project and zone
    logger("Building locations and defining cluster")
    cluster_location = client.common_location_path(project_id, cluster_zone)

    cluster_def = {
        "name": cluster_name,
        "initial_node_count": initial_node_count,
        "node_config": 
        {
            "machine_type": machine_type,
            "disk_size_gb": disk_size_gb,
            "oauth_scopes": scopes
        }                     
    }

    request = {"parent": cluster_location, "cluster": cluster_def}

    logger("Sending creation request...")
    response = client.create_cluster(request) #sends the message.

    logger("Got response:")
    logger(response)

    response = client.get_operation(project_id=project_id, zone=cluster_zone, operation_id=str(response.name))

    while (response.status in (1,2)):       #Status = 1 means PENDING, 2 means RUNNING, status 3 means DONE
        logger(f"Waiting for the cluster to start! Progess: {str(response.progress)}")
        sleep(5)
        response = client.get_operation(project_id=project_id, zone=cluster_zone, operation_id=str(response.name))

    os.system(f"gcloud container clusters get-credentials {cluster_name} --zone {cluster_zone} --project {project_id}")


def apply_deployments(logger):
    for filename in os.listdir("Deployments"):
        if (filename.endswith(".yaml")):
            logger(f"Deploying {filename}")
            os.system(f"kubectl apply -f Deployments/{filename}")

def setup_solver_manager(logger):
    logger("Setting up the solvermanager")
    os.system("kubectl create serviceaccount solvermanager")
    os.system("kubectl create clusterrolebinding solvermanager-role --clusterrole=edit --serviceaccount=default:solvermanager --namespace=default")

debug = True
if(debug):
    logger = print
else:
    log = open("log.txt", "a")
    logger = log.write

client = get_client(logger)

create_cluster(client, logger)

setup_solver_manager(logger)

apply_deployments(logger)