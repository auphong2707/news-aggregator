'''
References: https://www.alexmolas.com/2024/02/05/a-search-engine-in-80-lines.html?fbclid=IwAR0AYO9zMrklZic5TXVtE-uAO1dXeEYVcbGw0AApLhDnDcaD8aDyaMxNVaI
''' 
import math
import string

def preprocessing(str_input):
    translation_table = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
    str_input_no_punct = str_input.translate(translation_table)
    preprocesed_str = ' '.join(str_input_no_punct.split())
    return preprocesed_str

def update_scores(current_score, previous_score):
    
    updated_score = [] * len(current_score)
    
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
            data[i]['content'] = preprocessing(data[i]['content'])
        self.data = data
        self.average_document_length = sum([len(article['content'].split()) for article in self.data]) / len(self.data)
    
    def get_word_occurences(self, word, data_index):
        '''
        Return word occurences in a document given index
        '''
        return self.data[data_index]['content'].count(word)

    def get_word_occurences_global(self, word):
        '''
        Return total number of documents in data that has the word
        '''        
        return sum([word in article['content'] for article in self.data])

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
        where result[i]: score of query with article index i
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

    def search(self, query):
        '''
        query: a string of words
        
        '''
        query_tokens = preprocessing(query).split()
        query_score = [0] * len(self.data) 
        
        for word in query_tokens:
            word_bm25 = self.bm25_score(word)
            query_score = update_scores(query_score, word_bm25)
        
        return query_score
            

