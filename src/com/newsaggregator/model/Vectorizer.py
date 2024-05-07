import math
import string
import json 
import os
import numpy as np
import pickle 
import time 
import nltk

from unidecode import unidecode
from gensim.models import Word2Vec
from nltk.corpus import stopwords
from Utilities import *

nltk.download('stopwords')
STOPWORDS = stopwords.words('english')
CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/LocalServer.py', '')

class Vectorizer:
    def __init__(self, hidden_size = 200):
        self.data: list = []
        self.hidden_size = hidden_size
        self.model = Word2Vec(vector_size = hidden_size, window = 3, min_count = 3)
        self.vectorized_data = 0
        self.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
    
    def fit(self):
        f = open(self.file_path + 'data/newsAll.json', encoding = "utf8")
        data = json.load(f)
        with open(self.file_path + 'data/newsAll.json', 'w') as f:
            json.dump(data, f, indent = 2)
        self.data = DocumentProcessor.process(data, 'DETAILED_CONTENT')

    def train_model(self):
        '''
        Train word2vec models and save it into news-aggregator/data/model/word2vec.model
        '''
        w2v = Word2Vec(vector_size = self.hidden_size, window = 3, min_count = 3)
        w2v.build_vocab(self.data)

        words = w2v.wv.index_to_key
        vocab_size = len(words)
        
        w2v.train(self.data, total_examples = len(self.data), epochs = 50)
        w2v.save(self.file_path + "data/model/" + "word2vec.model")
        self.model = w2v
        print("Model trained and saved!")
    
    def load_model(self):
        '''
        Load saved word2vec model from news-aggregator/data/model/
        '''
        self.model = Word2Vec.load(self.file_path + "data/model/" + "word2vec.model")
        #sims = model.wv.most_similar('bitcoin', topn = 10)
        #print(model.wv['bitcoin'])
        #print(sims)
        print("Model loaded!")
    
    def vectorize_data(self):
        '''
        Vectorize the tokens into densed vectors using word2vec
        '''
        vectorized_article = []
        for content in self.data:
            len = 0
            article_content_vec = np.zeros(self.hidden_size, )
            
            for word in content:
                if word in self.model.wv.index_to_key:
                    article_content_vec += self.model.wv[word]
                    len += 1
            if len != 0:
                article_content_vec = article_content_vec / len
            article_content_vec = article_content_vec.reshape(-1, 1).T
            vectorized_article.append(article_content_vec)
        
        vectorized_article = np.array(vectorized_article).squeeze()
        self.vectorized_data = vectorized_article
    
    def export_vector(self):
        np.save(self.file_path + "data/temps.npy", self.vectorized_data)
        print(f"Vectorized text saved at {self.file_path}")
    
    def run(self, training = False):
        self.fit()
        if training:
            self.train_model()
        else:
            self.load_model()
        self.vectorize_data()    
        self.export_vector()
    
    def delete_temps(self):
        if os.path.exists(self.file_path + "data/temps.npy"):
            os.remove(self.file_path + "data/temps.npy")
            print("File deleted sucessfully!")
        else:
            print("File does not exist!")
        
if __name__ == "__main__":
    VectorizerModel = Vectorizer()
    VectorizerModel.run()
    print(len(VectorizerModel.data))
    #VectorizerModel.vectorize()
    #VectorizerModel.delete_temps()