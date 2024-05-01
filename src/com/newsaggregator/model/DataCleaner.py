import json 
import string 
import re 
import os 

import nltk
from unidecode import unidecode
from nltk.corpus import stopwords
nltk.download('stopwords')


STOPWORDS = stopwords.words('english')
CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/DataProcessor.py', '')

class DataCleaner():
    def __init__(self):
        self.data = []
    
    def import_data(self, filepath: str):
        '''
        Import data into the Summarizer via json file.
        '''
        
        f = open(filepath + 'data/newsAll.json', encoding = "utf8")
        self.data = json.load(f)
        
    def clean_data(self):
        '''
        Remove punctuations, unnecessary spaces, common stopwords from the title
        and detailed content of every article in newsAll.json
        '''
        for i in range(len(self.data)):
            self.data[i]['PROCESSED_TITLE'] = preprocessing(self.data[i]['TITLE'])
            self.data[i]['DETAILED_CONTENT_PROCESSED'] = preprocessing(self.data[i]['DETAILED_CONTENT'])
    
    def export_data(self, filepath: str):
        '''
        Export data to destined file under json format
        '''
        with open(filepath + 'data/newsAllProcessed.json', 'w') as f:
            json.dump(self.data, f)

def remove_stop_words(text: str) -> str:
    '''
    Remove unnecessary stopwords
    '''
    return ' '.join(word for word in text.split() if word not in STOPWORDS)

def preprocessing(str_input) -> str:
    '''
    Convert non-ascii characters to ascii ones, remove punctuations, redundant spaces
    Return: a processed string
    '''
    str_ascii = unidecode(str_input)
    translation_table = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
    str_input_no_punct = str_ascii.translate(translation_table)
    preprocesed_str = ' '.join(str_input_no_punct.split())
    return remove_stop_words(preprocesed_str.lower())


if __name__ == "__main__":
    
    data_cleaner = DataCleaner()
    data_cleaner.import_data(CURRENT_WORKING_DIRECTORY)
    data_cleaner.clean_data()
    data_cleaner.export_data(CURRENT_WORKING_DIRECTORY)

    
        
    