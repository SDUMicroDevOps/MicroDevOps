
import subprocess

apis_to_enable = [
    "artifactregistry.googleapis.com",
    "autoscaling.googleapis.com",
    "cloudbuild.googleapis.com",
    "clouddeploy.googleapis.com",
    "cloudresourcemanager.googleapis.com",
    "compute.googleapis.com",
    "container.googleapis.com",
    "containerfilesystem.googleapis.com",
    "containerregistry.googleapis.com",
    "dns.googleapis.com",
    "domains.googleapis.com",
    "firebaserules.googleapis.com",
    "firestore.googleapis.com",
    "iam.googleapis.com",
    "iamcredentials.googleapis.com",
    "oslogin.googleapis.com",
    "pubsub.googleapis.com",
    "secretmanager.googleapis.com",
    "sqladmin.googleapis.com",
    "sts.googleapis.com"
    ]
for api in apis_to_enable:
    subprocess.Popen(f"gcloud services enable {api}", shell=True, stdout=subprocess.PIPE)
