import os

#Variables needed for retrieving the secret credentials
project_id = os.getenv("PROJECT_ID")
secret_id = os.getenv("CREDENTIAL_SECRET_ID")

#Configurations for the cluster
cluster_name = "public-cluster"
initial_node_count = 2
cluster_zone = "europe-north1-a"        #europe-north1-a || europe-north1-b || europe-north1-c

#Configurations for node pool:
machine_type = "e2-medium"      #e2-micro || e2-medium
disk_size_gb = 50               #Disk space available for each node. Default = 100 gb
scopes = [
            "https://www.googleapis.com/auth/devstorage.read_only", 
            "https://www.googleapis.com/auth/service.management.readonly",
            "https://www.googleapis.com/auth/servicecontrol",
            "https://www.googleapis.com/auth/trace.append",
            "https://www.googleapis.com/auth/logging.write",
            "https://www.googleapis.com/auth/monitoring",
            "https://www.googleapis.com/auth/cloud-platform"
        ]
