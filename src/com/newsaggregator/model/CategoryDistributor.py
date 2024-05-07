import math
import string
import json 
import os
import numpy as np
import matplotlib.pyplot as plt
import pickle 
import time 

from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/TrendDetection.py', '')

class CategoryDistributor:
    def __init__(self, number_clusters = 3):
        self.data = None 
        self.kmean_model = None 
        self.vectorized_document = None
        self.number_clusters = number_clusters
        self.index_to_category = {0: 'Others', 1: 'Blockchain', 2: 'Crypto', 3: 'something', 4:'something 2', 5: 'something 3'}
        self.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), '')
    
    def load_data(self):
        '''
        Load data to self.vectorized_document
        '''
        f = open(self.file_path + 'data/newsAll.json', encoding = "utf8")
        self.data = json.load(f)
        with open(self.file_path + "data/temps.npy", "rb") as f:
            self.vectorized_document =  np.load(f)
    
    def train_kmean(self):
        '''
        Build k-mean model to cluster articles in order
        to detect trends
        '''
        self.kmean_model = KMeans(n_clusters = self.number_clusters, init = 'k-means++', n_init = 10, random_state = 42)
        self.kmean_model.fit(self.vectorized_document)
        y_kmean = self.kmean_model.predict(self.vectorized_document).tolist()
        for i in range(len(self.data)):
            self.data[i]['CATEGORY'] = self.index_to_category[y_kmean[i]]      
        
        
    def save_category(self):
        '''
        Export data to destined file under json format
        '''
        
        with open(self.file_path + 'data/newsAll.json', 'w') as f:
            json.dump(self.data, f, indent = 2) 
            print("Category saved!")
    
    def visualize(self):
        '''
        Using PCA to visualize the clustering
        '''
        pca_model = PCA(n_components=3)
        y_kmean = self.kmean_model.predict(self.vectorized_document)
        
        pca_article = pca_model.fit_transform(self.vectorized_document)
        fig = plt.figure(figsize = (8, 8))
        #ax = fig.add_subplot()
        ax = fig.add_subplot(projection = '3d')
        for i in range(self.number_clusters):
            ax.scatter(pca_article[y_kmean == i, 0], pca_article[y_kmean == i , 1], pca_article[y_kmean == i, 2], label = f'cluster {i}')

        ax.set_xlabel("Projected dimension 1")
        ax.set_ylabel("Projected dimension 2")
        ax.legend()
        plt.show()

    def run(self):
        self.load_data()
        self.train_kmean()
        self.save_category()
    
    def get_trending(self):
        '''
        Trending is the articles that are in the cluster with most points \n
        Return a json file of "trending" articles
        '''
        f = open(CURRENT_WORKING_DIRECTORY + 'data/trendings.json', encoding = "utf8")
        trending_articles = json.load(f)
        return json.dumps(trending_articles)

        

if __name__ == "__main__":
    
    categoryDistributorModel = CategoryDistributor()
    categoryDistributorModel.run()    
    categoryDistributorModel.visualize()
    '''
    Uncomment these codes if need to be retrained to get new trending from beginning
    
    TrendDetector.load_data(CURRENT_WORKING_DIRECTORY)
    TrendDetector.train_kmean()
    TrendDetector.save_model(CURRENT_WORKING_DIRECTORY)
    TrendDetector.save_trending(CURRENT_WORKING_DIRECTORY)
    '''
    
    # Load k-mean model and cluster it to find trendings articles
    #TrendDetector.load_model(CURRENT_WORKING_DIRECTORY)
    
    #print(trending_articles)
    #TrendDetector.visualize()
    
    #print(articles)




