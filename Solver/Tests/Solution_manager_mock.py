from flask import Flask
from flask import request

app = Flask(__name__)

@app.route('/SolutionFound', methods=['POST'])
def connection():
   print("data: " + str(request.get_data()))
   return "200"

if __name__ == '__main__':
   app.run()