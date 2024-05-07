import math
import string
import json 
import os
import numpy as np


from Summarizer import Summarizer
from Vectorizer import Vectorizer 
from TrendDetection import TrendDetection
from CategoryDistributor import CategoryDistributor
from SearchEngine import SearchEngine

categoryDistributorModel = CategoryDistributor()
trendDetectorModel = TrendDetection()
articleVectorizer = Vectorizer()
summarizerModel = Summarizer()
searchEngine = SearchEngine()


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

def get_latest():
    '''
    Get the latest 100 articles from the json files
    '''
    file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    
    f.close()
    
    return json.dump(data[:100])
    

def process_data():
    categoryDistributorModel.run()
    #summarizerModel.run()

def process_new_data():
    sort_by_date()
    articleVectorizer.run()
    process_data()
    analyze_data()
    articleVectorizer.delete_temps()

def analyze_data():
    trendDetectorModel.run()
    searchEngine.run()
    
    #Search for something: searchEngine.search(query_string: str, num_of_relevant_results: int)
    #Get trending articles: trendDetectorModel.get_trending()

if __name__ == "__main__":
    process_new_data()