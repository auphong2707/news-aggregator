#pip install -U sentence-transformers

from summarizer.sbert import SBertSummarizer

import json 
import warnings 
warnings.filterwarnings('ignore')

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/Summarizer.py', '')


class Summarizer():
    def __init__(self):
        self.data = []
        self.model = SBertSummarizer('all-mpnet-base-v2')
    
    
    def summarize_data(self):
        for i in range(len(self.data)):
            body = self.data[i]['DETAILED_CONTENT']
            self.data[i]['SUMMARY'] = self.model(body, ratio = 0.3)
            
            if i % 20 == 0:
                print(f"Article {i} summarized")
    def import_data(self, filepath):
        f = open(filepath + 'data/newsAllProcessed.json', encoding = "utf8")
        self.data = json.load(f)       

    def export_data(self, filepath):
        with open(filepath + 'data/newsAllProcessed.json', 'w') as f:
            json.dump(self.data, f)

if __name__ == "__main__":
    newsSummarizer = Summarizer()
    newsSummarizer.import_data(CURRENT_WORKING_DIRECTORY)
    newsSummarizer.summarize_data()
    newsSummarizer.export_data(CURRENT_WORKING_DIRECTORY)