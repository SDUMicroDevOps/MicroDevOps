from flask import Flask

app = Flask(__name__)

@app.route('/Solution/<taskid>', methods=['POST'])
def connection(taskid):
    print("Task finished: {task}".format(task=taskid))
    return "200"

if __name__ == '__main__':
   app.run()