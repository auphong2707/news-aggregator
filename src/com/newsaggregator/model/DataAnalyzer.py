import json
import concurrent

from SearchEngine import SearchEngine
from TrendDetection import TrendDetectionModel, load_model
from flask import Flask, request

engine = Flask(__name__)

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/')\
                                    .replace('src/com/newsaggregator/model/DataAnalyzer.py', '')

# Get the data
data_file = open(CURRENT_WORKING_DIRECTORY + 'data/newsAll.json', encoding = "utf8")
data = json.load(data_file)

def init_search():
    # Loading data into the search engine
    search_engine = SearchEngine()
    search_engine.fit(data)
    
    return search_engine

def init_trend():
    # Loading data into trend detection model
    trend_detector = TrendDetectionModel(load_model(), 11)
    trend_detector.load_data(data, CURRENT_WORKING_DIRECTORY)
    trend_detector.load_model(CURRENT_WORKING_DIRECTORY)

    return trend_detector

# API methods
@engine.route('/search/', methods = ['POST'])
def search():
    if request.method == 'POST':
        decoded_request = request.data.decode('utf-8')
        print(decoded_request)
        params = json.loads(decoded_request)
        return search_engine.search(params['content'], 50)

@engine.route('/trending', methods=['GET'])
def get_trending():
    return trend_detector.get_trending()

if __name__ == '__main__':
    with concurrent.futures.ThreadPoolExecutor(max_workers=3) as executor:
        task_1 = executor.submit(init_search)
        task_2 = executor.submit(init_trend)

        search_engine = task_1.result()
        trend_detector = task_2.result()
    engine.run()
