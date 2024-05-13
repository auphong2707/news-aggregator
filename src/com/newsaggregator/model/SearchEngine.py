'''
References: https://www.alexmolas.com/2024/02/05/a-search-engine-in-80-lines.html?fbclid=IwAR0AYO9zMrklZic5TXVtE-uAO1dXeEYVcbGw0AApLhDnDcaD8aDyaMxNVaI
''' 
import math
import json 
import os
import pickle

from unidecode import unidecode
from Utilities import *

def update_scores(current_score: list, previous_score: list) -> list:
    
    updated_score = [0] * len(current_score)
    
    for i in range(len(current_score)):
        updated_score[i] = current_score[i] + previous_score[i]
    
    return updated_score


class SearchEngine:
    def __init__(self, k1: int = 1.5, b: int = 0.75):
        '''
        data: a list of dictionary of articles, which contains {title, url, content}
        k1: constant for BM25 ranker
        b: constant for BM25 ranker
        avg_document_length: average length of document in the data
        '''
        self.data = None
        self.k1 = k1
        self.b = b
        
        self.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
        self.data_path = self.file_path + 'data/searchEngineData.txt'
        self.vocab = []
        
    def save_engine(self):
        self.data = None
        with open(self.file_path + 'data/model/search_engine.pkl', 'wb') as f:
            pickle.dump(self, f)
    
    @classmethod
    def load_engine(cls, file_path):
        f = open(file_path + 'data/newsAll.json', encoding = "utf8")
        data = json.load(f)
        f.close()

        with open(file_path + 'data/model/search_engine.pkl', 'rb') as f:
            loaded_search_engine = pickle.load(f)
        loaded_search_engine.data = data
        loaded_search_engine.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
        return loaded_search_engine
        
    def run(self):
        '''
        The data is fed into the class attributes self.data \\
        The processed_article_contents list will contain the processed article content string of every content in the list \\ 
        '''
        f = open(self.file_path + 'data/newsAll.json', encoding = "utf8")
        data = json.load(f)
        f.close()
        
        self.vocab = [dict() for i in range(len(data))]
        processed_article_content_new = []
        for i in range(len(data)):
            processed_article_content_new.append(StringProcessor.process(data[i]['TITLE'] + ' ') * 10 + ' ' + StringProcessor.process(data[i]['DETAILED_CONTENT'])) 
        
        for i, content in enumerate(processed_article_content_new):
            words = content.split()
            for word in words:
                self.vocab[i][word] = self.vocab[i].get(word, 0) + 1

        self.average_document_length = sum([len(processed_article_content_new[i].split()) for i in range(len(data))]) / len(data)
        self.save_engine()
    
    def get_word_occurences(self, word, data_index):
        '''
        Return word occurences in a document given index
        '''
        return self.vocab[data_index].get(word, 0)

    def get_word_occurences_global(self, word):
        '''
        Return total number of documents in data that has the word
        '''        
        return sum([(self.vocab[i].get(word, 0) != 0) for i in range(len(self.data))])

    def idf(self, word: str) -> float:
        '''
        return inverse document frequency value (idf) of the word
        '''
        num_documents = len(self.data) 
        num_occurences = self.get_word_occurences_global(word)
        
        return math.log((num_documents - num_occurences + 0.5) / (num_occurences + 0.5) + 1)

    def bm25_score(self, query: str) -> list:
        '''
        query: a word
        
        method will return a list of result
        where result[i]: score of query word with article index i
        ''' 
        average_document_length = self.average_document_length
        k1, b = self.k1, self.b  
        
        query_tokens = StringProcessor.process(query).split()
        result = []
        word_idf = self.idf(query) 
        for i in range(len(self.data)):
            word_frequencies = self.get_word_occurences(query, i)
            numerator = word_frequencies * (k1 + 1)
            denominator = word_frequencies + k1 * (1 - b + b * len(self.data) / average_document_length)
            result.append(word_idf * numerator / denominator)
            
        return result

    def search(self, query: str, num_relevant_results: int) -> str:
        '''
        query: a string of words
        return: top 10 most relevant results, in form of dictionary
        '''
        query_tokens = StringProcessor.process(query).split()
        query_score = [0] * len(self.data) 
        results = [0] * num_relevant_results
        
        for i, word in enumerate(query_tokens):
            word_bm25 = self.bm25_score(word)
            query_score = update_scores(query_score, word_bm25)

        for i in range(len(self.data)):
            query_score[i] = (i, query_score[i])

        query_score = sorted(query_score, key = lambda x: -x[1])

        for i in range(num_relevant_results):
            index = query_score[i][0]
            results[i] = self.data[index]
        
        return_json_string = json.dumps(results)
        
        return return_json_string

if __name__ == "__main__":
    '''
    Loading data into the search engine
    '''
    
    '''py
    search_engine = SearchEngine()
    search_engine.run()
    second_search_engine = SearchEngine.load_engine(__file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), ''))
    query = input()
    result = second_search_engine.search(query, 50)
    print(result[:2000])
    '''
    '''
    Show all relevant articles given the query string
    return variable is a json string
    '''
    