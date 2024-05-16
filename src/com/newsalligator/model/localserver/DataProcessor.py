import json
import os

from Utilities import *
from SearchEngine import SearchEngine


file_path = __file__.replace('\\', '/').replace('src/com/newsalligator/model/localserver/' + os.path.basename(__file__), '')

search_engine = SearchEngine.load_engine(file_path)

def process_new_data():
    from Vectorizer import Vectorizer 
    article_vectorizer = Vectorizer()

    sort_by_date()
    article_vectorizer.run()

    process_data()
    analyze_data()
    
    article_vectorizer.delete_temps()

def process_data():
    from Summarizer import Summarizer
    from CategoryDistributor import CategoryDistributor

    category_distributor_model = CategoryDistributor()
    summarizerModel = Summarizer()

    category_distributor_model.run()
    summarizerModel.run()

def analyze_data():
    from TrendDetector import TrendDetector
    trend_detector_model = TrendDetector()

    trend_detector_model.run()
    search_engine.run()
    
    #Search for something: searchEngine.search(query_string: str, num_of_relevant_results: int)
    #Get trending articles: trendDetectorModel.get_trending()

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

if __name__ == '__main__':
    process_new_data()