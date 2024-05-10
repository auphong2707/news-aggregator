import json 
import os
import random
import psutil
import subprocess

from SearchEngine import SearchEngine
from flask import Flask, request
from Utilities import *

file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')

server = Flask(__name__)
process = None

search_engine = SearchEngine.load_engine(file_path)

@server.route('/update', methods=['GET'])
def process_new_data():
    global process
    if process is None:
        process = subprocess.Popen(['python', file_path + 'src/com/newsaggregator/model/DataProcessor.py'])
        return "Start processing data"
    elif process.poll() is not None:
        return "Data is already updated"
    else:
        return "Data is in the process"

@server.route('/random', methods=['GET'])
def get_random() -> str:
    '''
    Get a random number of articles from the json files
    '''
    number_of_articles = request.args.get('number', type=int)
    
    f = open(file_path + 'data/newsAll.json', encoding = "utf8")
    data = json.load(f)
    f.close()
    
    selected_articles = []
    randomIndex = random.sample(range(len(data)), number_of_articles)
    for idx in randomIndex:
        selected_articles.append(data[idx])
    
    return json.dumps(selected_articles)
    
@server.route('/latest', methods=['GET'])
def get_latest() -> str:
    '''
    Get the latest number of articles from the json files
    '''
    number_of_articles = request.args.get('number', type=int)
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
    f = open(file_path + 'data/trendings.json', encoding = "utf8")
    data = json.load(f)
    f.close()

    number_of_articles = request.args.get('number', type=int)

    selected_articles = []
    randomIndex = random.sample(range(len(data)), number_of_articles)
    for idx in randomIndex:
        selected_articles.append(data[idx])

    return json.dumps(selected_articles)

if __name__ == "__main__":
    server.run()