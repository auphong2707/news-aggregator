import json 
import os


from TrendDetection import TrendDetection
from SearchEngine import SearchEngine
from flask import Flask, request

server = Flask(__name__)

search_engine = SearchEngine()
trend_detector_model = TrendDetection()

def split_date(article):
    '''
    Key function for sorting the articles by date descending
    '''
    splitted_date = article['CREATION_DATE'].split('-')
    if len(splitted_date) == 1:
        return 0, 0, 0
    return -int(splitted_date[0]), -int(splitted_date[1]), -int(splitted_date[2])

def sort_by_date():
    '''
    Sort data by dates in descending order, save it in newsAll.json
    '''
    file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
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

@server.route('/getlatest', methods=['GET'])
def get_latest():
    '''
    Get the latest 100 articles from the json files
    '''
    file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    
    f.close()
    
    return json.dump(data[:100])

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