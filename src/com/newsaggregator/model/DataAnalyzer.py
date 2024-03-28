import json
from SearchEngine import SearchEngine
from flask import Flask, request

engine = Flask(__name__)

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/')\
                                    .replace('src/com/newsaggregator/model/DataAnalyzer.py', '')

# Get the data
data_file = open(CURRENT_WORKING_DIRECTORY + 'data/newsAll.json', encoding = "utf8")
data = json.load(data_file)

# Loading data into the engine
search_engine = SearchEngine()
search_engine.fit(data)

# API methods
@engine.route('/search/', methods = ['POST'])
def search():
    if request.method == 'POST':
        decoded_request = request.data.decode('utf-8')
        print(decoded_request)
        params = json.loads(decoded_request)
        return search_engine.search(params['content'], 10)

if __name__ == '__main__':
    engine.run(debug=True)
