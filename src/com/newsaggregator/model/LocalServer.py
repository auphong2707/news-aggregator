import json 
import os
import random

from TrendDetection import TrendDetection
from SearchEngine import SearchEngine
from flask import Flask, request
from Utilities import *

file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')

server = Flask(__name__)

search_engine = SearchEngine()
trend_detector_model = TrendDetection()


def sort_by_date():
    '''
    Sort data by dates in descending order, save it in newsAll.json
    '''
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    f.close()
    
    data.sort(key = split_date)
    with open(file_path + 'data/newsAll.json', 'w') as f:
        json.dump(data, f, indent = 2)

def process_data():
    from Summarizer import Summarizer
    from CategoryDistributor import CategoryDistributor

    category_distributor_model = CategoryDistributor()
    summarizerModel = Summarizer()

    category_distributor_model.run()
    #summarizerModel.run()

def analyze_data():
    trend_detector_model.run()
    search_engine.run()
    
    trend_detector_model.load_engine(file_path)
    
    #Search for something: searchEngine.search(query_string: str, num_of_relevant_results: int)
    #Get trending articles: trendDetectorModel.get_trending()

def process_new_data():
    from Vectorizer import Vectorizer 
    article_vectorizer = Vectorizer()

    sort_by_date()
    article_vectorizer.run()

    process_data()
    analyze_data()
    
    article_vectorizer.delete_temps()

@server.route('/getrandom', methods=['GET'])
def get_random(number_of_articles: int) -> str:
    '''
    Get a random number of articles from the json files
    '''
    
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    f.close()
    
    selected_articles = []
    randomIndex = random.sample(range(len(data)), number_of_articles)
    for idx in randomIndex:
        selected_articles.append(data[idx])
    
    return json.dumps(selected_articles)
    
@server.route('/getlatest', methods=['GET'])
def get_latest(number_of_articles: int) -> str:
    '''
    Get the latest number of articles from the json files
    '''
    file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    f.close()
    
    return json.dumps(data[:number_of_articles])

@server.route('/search/', methods = ['POST'])
def search():
    if request.method == 'POST':
        decoded_request = request.data.decode('utf-8')
        print(decoded_request)
        params = json.loads(decoded_request)
        return search_engine.search(params['content'], 50)
    
@server.route('/trending', methods=['GET'])
def get_trending():
    return trend_detector_model.get_trending()

if __name__ == "__main__":
    server.run()