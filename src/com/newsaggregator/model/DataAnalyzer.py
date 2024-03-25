'''
References: https://www.alexmolas.com/2024/02/05/a-search-engine-in-80-lines.html?fbclid=IwAR0AYO9zMrklZic5TXVtE-uAO1dXeEYVcbGw0AApLhDnDcaD8aDyaMxNVaI
''' 
import math
import string
import json 
import os
import pandas as pd
from unidecode import unidecode 

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/DataAnalyzer.py', '')

def preprocessing(str_input):
    str_ascii = unidecode(str_input)
    translation_table = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
    str_input_no_punct = str_ascii.translate(translation_table)
    preprocesed_str = ' '.join(str_input_no_punct.split())
    return preprocesed_str.lower()

def update_scores(current_score, previous_score):
    
    updated_score = [0] * len(current_score)
    
    for i in range(len(current_score)):
        updated_score[i] = current_score[i] + previous_score[i]
    
    return updated_score

class SearchEngine:
    def __init__(self, k1 = 1.5, b = 0.75):
        '''
        data: a list of dictionary of articles, which contains {title, url, content}
        k1: constant for BM25 ranker
        b: constant for BM25 ranker
        avg_document_length: average length of document in the data
        '''
        self.data = None
        self.k1 = k1
        self.b = b
        self.average_document_length = 0


    def fit(self, data):
        '''
        The data is fed  into the class
        '''
        for i in range(len(data)):
            data[i]['DETAILED_CONTENT'] = preprocessing(data[i]['DETAILED_CONTENT'])
            #data[i]['TITLE'] = preprocessing(data[i]['TITLE'])
        self.data = data
        self.average_document_length = sum([len(article['DETAILED_CONTENT'].split()) for article in self.data]) / len(self.data)
    
    def get_word_occurences(self, word, data_index):
        '''
        Return word occurences in a document given index
        '''
        return self.data[data_index]['DETAILED_CONTENT'].count(word)

    def get_word_occurences_global(self, word):
        '''
        Return total number of documents in data that has the word
        '''        
        return sum([word in article['DETAILED_CONTENT'] for article in self.data])

    def idf(self, word):
        '''
        return inverse document frequency value (idf) of the word
        '''
        num_documents = len(self.data) 
        num_occurences = self.get_word_occurences_global(word)
        
        return math.log((num_documents - num_occurences + 0.5) / (num_occurences + 0.5) + 1)

    def bm25_score(self, query):
        '''
        query: a word
        
        method will return a list of result
        where result[i]: score of query word with article index i
        ''' 
        average_document_length = self.average_document_length
        k1, b = self.k1, self.b  
        
        query_tokens = preprocessing(query).split()
        result = []
        word_idf = self.idf(query) 
        for i in range(len(self.data)):
            word_frequencies = self.get_word_occurences(query, i)
            numerator = word_frequencies * (k1 + 1)
            denominator = word_frequencies + k1 * (1 - b + b * len(self.data) / average_document_length)
            result.append(word_idf * numerator / denominator)
            
        return result

    def search(self, query, num_relevant_results):
        '''
        query: a string of words
        return: top 10 most relevant results, in form of dictionary
        '''
        query_tokens = preprocessing(query).split()
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
            results[i] = {"score": query_score[i][1], "title": self.data[index]['TITLE'], "content": self.data[index]['DETAILED_CONTENT']}
        return results

print(CURRENT_WORKING_DIRECTORY)

'''
Loading data into the search engine
'''
f = open(CURRENT_WORKING_DIRECTORY + 'data/newsFT.json', encoding = "utf8")
data = json.load(f)
search_engine = SearchEngine()
search_engine.fit(data)

'''
Show all relevant results
result[i] = {"score": , "title": , "detailed_content"}
'''
#print(search_engine.data[0]['DETAILED_CONTENT'])
result = search_engine.search("Facebook Libra: the", 10)
print(result[0])

