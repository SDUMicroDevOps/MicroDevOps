This class is a microservice which communicates with the google bucket storage, allowing for gettings signed URLS to download and upload problems automatically using them.

We need to integration test it with the Solver to make sure it works, but manual tests seem to confirm functionality, as all files in the bucket 'microservices22tasks_bucket' have been put there and downloaded automatically.

An example of the use of this microservice can be seen in the service "Solver.py", which contains the following code:

<!-- resp = requests.get(self.bucket_handler_url + "/TaskBucket/{taskID}".format(taskID = self.taskID))     #Sends the initial request to get the signed URLS
        urls = resp.json()                                                                                  #BucketHandler responds with a json object
        problem_file_response = requests.get(urls["ProblemFileUrl"])                                        #Send a get request to the url returned for the problemFile
        with open("mzn.mzn", "wb") as f:
            f.write(problem_file_response.content)                                                          #Write the data returned by the url call above to a file'
-->
