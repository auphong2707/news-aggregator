import json
from SearchEngine import SearchEngine
from TrendDetection import TrendDetectionModel, load_model
from flask import Flask, request

engine = Flask(__name__)

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/')\
                                    .replace('src/com/newsaggregator/model/DataAnalyzer.py', '')

# Get the data
data_file = open(CURRENT_WORKING_DIRECTORY + 'data/newsAll.json', encoding = "utf8")
data = json.load(data_file)

# Loading data into the search engine
search_engine = SearchEngine()
search_engine.fit(data)

# Loading data into trend detection model
trend_detector = TrendDetectionModel(load_model(), 11)
trend_detector.fit_data(data)
trend_detector.load_model(CURRENT_WORKING_DIRECTORY)

# API methods
@engine.route('/search/', methods = ['POST'])
def search():
    if request.method == 'POST':
        decoded_request = request.data.decode('utf-8')
        print(decoded_request)
        params = json.loads(decoded_request)
        return search_engine.search(params['content'], 10)

@engine.route('/trending', methods=['GET'])
def get_trending():
    return trend_detector.get_trending()

if __name__ == '__main__':
    engine.run(debug=True)
