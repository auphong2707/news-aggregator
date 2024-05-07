#pip install -U sentence-transformers

from summarizer.sbert import SBertSummarizer
from os.path import expanduser

import os
import json 
import warnings 
import threading
import concurrent.futures

warnings.filterwarnings('ignore')

home_path = expanduser("~").replace('\\', '/') + "/.cache/huggingface/hub"

class Summarizer():
    def __init__(self):
        self.data = []
        self.model = SBertSummarizer('all-mpnet-base-v2')
        self.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
        self.lock = threading.Lock()
        
       
    def summarize_article(self, articles, start_idx):
        '''
        Using pretrained Sentence-Bert model to summarize detailed content
        Of the article in 30% length of the originals.
        '''
        for i, article in enumerate(articles):
            body = article['DETAILED_CONTENT']
            summary = self.model(body, ratio=0.3)
            
            with self.lock:
                article['SUMMARY'] = summary
        
            print(f"Article {start_idx + i} summarized")
        #print("article summarized")
    def summarize_data(self):
        '''
        Using threading module to summarize articles concurrently with 6 threads.
        '''
        num_threads = 6
        chunk_size = len(self.data) // num_threads
        threads = []
        for i in range(num_threads):
            start_index = i * chunk_size
            end_index = start_index + chunk_size if i < num_threads - 1 else len(self.data)
            thread_data = self.data[start_index:end_index]
            thread = threading.Thread(target=self.summarize_article, args=(thread_data, start_index))
            threads.append(thread)
            thread.start()

        for thread in threads:
            thread.join()
           
    def import_data(self):
        '''
        Import data into the Summarizer via json file.
        '''
        f = open(self.file_path + 'data/newsAll.json', encoding = "utf8")
        self.data = json.load(f)       

    def export_data(self):
        '''
        Export data to destined file under json format
        '''
        with open(self.file_path + 'data/newsAllBackup.json', 'w') as f:
            json.dump(self.data, f)
    def run(self):
        self.import_data()
        self.summarize_data()
        self.export_data()


if __name__ == "__main__":
    newsSummarizer = Summarizer()
    #newsSummarizer.run()
    