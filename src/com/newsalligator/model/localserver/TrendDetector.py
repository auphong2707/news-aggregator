import json 
import os
import numpy as np
import matplotlib.pyplot as plt

from sklearn.cluster import KMeans
from sklearn.decomposition import PCA

CURRENT_WORKING_DIRECTORY = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/localserver/TrendDetection.py', '')

class TrendDetector:
    def __init__(self):
        self.data = None 
        self.kmean_model = None 
        self.vectorized_document = None
        self.number_clusters = 0
        self.trending_articles = None
        self.number_of_latest_articles = 100
        self.file_path = __file__.replace('\\', '/').replace('src/com/newsaggregator/model/localserver/' + os.path.basename(__file__), '')
    
    def load_data(self):
        '''
        Load data to self.vectorized_document
        '''
        f = open(self.file_path + 'data/newsAll.json', encoding = "utf8")
        self.data = json.load(f)[:self.number_of_latest_articles]
        f.close()
        with open(self.file_path + "data/temps.npy", "rb") as f:
            a = np.load(f)
            self.vectorized_document = a[:self.number_of_latest_articles, :]
    
    def select_number_clusters(self):
        
        loss_pairs = []
        loss_val = []
        
        for k in range(1, 21):
            kmean_model = KMeans(n_clusters = k, init = 'k-means++', n_init = 10, random_state = 42)
            kmean_model.fit(self.vectorized_document)
            loss_pairs.append((k, kmean_model.inertia_))
            loss_val.append(kmean_model.inertia_)
        
        cur_angle = -float('inf')
        
        for _ in range(1, len(loss_pairs) - 1):
            
            cur_k, cur_loss = loss_pairs[_]
            prev_k, prev_loss = loss_pairs[_ - 1]
            post_k, post_loss = loss_pairs[_ + 1]
            
            d_loss_post = cur_loss - post_loss
            d_loss_prev = prev_loss - cur_loss
            sub_loss = d_loss_prev - d_loss_post  
            
            if sub_loss > cur_angle:
                self.number_clusters = cur_k
                cur_angle = sub_loss
                
        #plt.plot(range(1, 21), loss_val)
        #plt.show()
    
    def train_kmean(self):
        '''
        Build k-mean model to cluster articles in order
        to detect trends
        '''
        self.kmean_model = KMeans(n_clusters = self.number_clusters, init = 'k-means++', n_init = 10, random_state = 42)
        self.kmean_model.fit(self.vectorized_document)
        y_kmean = self.kmean_model.predict(self.vectorized_document).tolist()
        trending_articles = []
        most_frequent_cluster = max(set(y_kmean), key = y_kmean.count)       
        self.trending_articles = [self.data[i] for i in range(len(self.data)) if y_kmean[i] == most_frequent_cluster]
        
    def save_trending(self):
        '''
        Export data to destined file under json format
        '''
        with open(self.file_path + 'data/trendings.json', 'w') as f:
            json.dump(self.trending_articles, f) 
            print("Trending articles saved!")
    
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

    def run(self):
        self.load_data()
        self.select_number_clusters()
        self.train_kmean()
        self.save_trending()

         

if __name__ == "__main__":
    #print(__file__.replace('\\', '/').replace('src/com/newsaggregator/model/' + os.path.basename(__file__), ''))
    trend_detector= TrendDetector()
    trend_detector.run()
    print(trend_detector.number_clusters)
    '''
    Uncomment these codes if need to be retrained to get new trending from beginning
    
    TrendDetector.load_data(CURRENT_WORKING_DIRECTORY)
    TrendDetector.train_kmean()
    TrendDetector.save_model(CURRENT_WORKING_DIRECTORY)
    TrendDetector.save_trending(CURRENT_WORKING_DIRECTORY)
    '''
    
    # Load k-mean model and cluster it to find trendings articles
    #TrendDetector.load_model(CURRENT_WORKING_DIRECTORY)
    #trending_articles = TrendDetector.get_trending() #this is output
    #print(trending_articles)
    #TrendDetector.visualize()
    
    #print(articles)
