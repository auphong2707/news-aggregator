import math
import string
import json 
import os
import nltk
import numpy as np
import matplotlib.pyplot as plt
import pickle 

from nltk.corpus import stopwords
nltk.download('stopwords')

from unidecode import unidecode
from gensim.models import Word2Vec
from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

STOPWORDS = stopwords.words('english')
CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/TrendDetection.py', '')


def remove_stop_words(text: str) -> str:
    '''
    Remove unnecessary stopwords
    '''
    return ' '.join(word for word in text.split() if word not in STOPWORDS)

def preprocessing(str_input) -> str:
    str_ascii = unidecode(str_input)
    translation_table = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
    str_input_no_punct = str_ascii.translate(translation_table)
    preprocesed_str = ' '.join(str_input_no_punct.split())
    return remove_stop_words(preprocesed_str.lower())

def preprocess_corpus(data):
    '''
    Preprocess the detailed content of the articles
    And split them into specific tokens
    '''
    corpus = []
    for content in data:
        detailed_content = content['DETAILED_CONTENT']
        detailed_content = preprocessing(detailed_content)

        corpus.append(detailed_content)
    
    documents = [text.split() for text in corpus]
    return documents

def vectorize(document, model):
    '''
    Vectorize the tokens into densed vectors using word2vec
    '''
    vectorized_article = []
    for content in document:
        len = 0
        article_content_vec = np.zeros(200, )
        
        for word in content:
            if word in model.wv.index_to_key:
                article_content_vec += model.wv[word]
                len += 1
        if len != 0:
            article_content_vec = article_content_vec / len
        
        article_content_vec = article_content_vec.reshape(-1, 1).T
        vectorized_article.append(article_content_vec)
    
    vectorized_article = np.array(vectorized_article).squeeze()
    return vectorized_article
    
class TrendDetectionModel:
    def __init__(self, word2vec_model, number_clusters = 8):
        self.data = None 
        self.document = None
        self.kmean_model = None 
        self.word2vec_model = word2vec_model
        self.vectorized_document = None
        self.number_clusters = number_clusters
    
    def fit_data(self, data):
        '''
        Fit all article data into the class, perform necessary transformation
        '''
        self.data = data 
        self.document = preprocess_corpus(data)
        self.vectorized_document = vectorize(self.document, self.word2vec_model)
    
    def load_model(self, filepath):
        with open(filepath + "data/model/" + "kmean_model.pkl", "rb") as f:
            self.kmean_model = pickle.load(f)
    
    def save_model(self, filepath):
        '''
        Save model if needed
        '''
        with open(filepath + "data/model/" + "kmean_model.pkl", "wb") as f:
            pickle.dump(self.kmean_model, f)
    
    def train_kmean(self):
        '''
        Build k-mean model to cluster articles in order
        to detect trends
        '''
        self.kmean_model = KMeans(n_clusters = self.number_clusters, init = 'k-means++', n_init = 10, random_state = 42)
        self.kmean_model.fit(self.vectorized_document)
    
    def visualize(self):
        '''
        Using PCA to visualize the clustering
        '''
        pca_model = PCA(n_components=2)
        y_kmean = self.kmean_model.predict(self.vectorized_document)
        
        pca_article = pca_model.fit_transform(self.vectorized_document)
        fig = plt.figure(figsize = (8, 8))
        ax = fig.add_subplot()
        # ax = fig.add_subplot(projection = '3d')
        for i in range(self.number_clusters):
            ax.scatter(pca_article[y_kmean == i, 0], pca_article[y_kmean == i , 1], label = f'cluster {i}')

        ax.set_xlabel("Projected dimension 1")
        ax.set_ylabel("Projected dimension 2")
        ax.legend()
        plt.show()

    def get_trending(self):
        '''
        Trending is the articles that are in the cluster with most points
        Return a json file of "trending" articles
        '''
        y_kmean = self.kmean_model.predict(self.vectorized_document).tolist()
        trending_articles = []
        most_frequent_cluster = max(set(y_kmean), key = y_kmean.count)
        
        trending_articles = [self.data[i] for i in range(len(self.data)) if y_kmean[i] == most_frequent_cluster]
        return_json_trending_articles = json.dumps(trending_articles)
        return return_json_trending_articles
        
    

def load_model():
    '''
    Load saved word2vec model from news-aggregator/data/model/
    '''
    model = Word2Vec.load(CURRENT_WORKING_DIRECTORY + "data/model/" + "word2vec.model")
    #sims = model.wv.most_similar('bitcoin', topn = 10)
    #print(model.wv['bitcoin'])
    #print(sims)
    print("Model loaded!")
    return model

def train(data):
    '''
    Train word2vec models and save it into news-aggregator/data/model/
    '''
    w2v = Word2Vec(vector_size = 200, window = 3, min_count = 1)
    w2v.build_vocab(data)

    words = w2v.wv.index_to_key
    vocab_size = len(words)
    
    w2v.train(data, total_examples = len(data), epochs = 32)
    w2v.save(CURRENT_WORKING_DIRECTORY + "data/model/" + "word2vec.model")
    print("Model trained and saved!")

if __name__ == "__main__":
    CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/TrendDetection.py', '')
    f = open(CURRENT_WORKING_DIRECTORY + 'data/newsFT.json', encoding = "utf8")
    data = json.load(f)
    model = load_model()
    TrendDetector = TrendDetectionModel(model, 11)
    TrendDetector.fit_data(data)
    #TrendDetector.train_kmean()
    #TrendDetector.save_model(CURRENT_WORKING_DIRECTORY)
    TrendDetector.load_model(CURRENT_WORKING_DIRECTORY)
    trending_articles = TrendDetector.get_trending() #this is output
    #TrendDetector.visualize()
    #print(articles)




