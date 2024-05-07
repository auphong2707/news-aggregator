import math
import string
import json 
import os
import numpy as np


from Summarizer import Summarizer
from Vectorizer import Vectorizer 
from TrendDetection import TrendDetection

#VectorizerModel = Vectorizer()
#TrendDetector = TrendDetection()
#VectorizerModel.vectorize()
#VectorizerModel.delete_temps()
TrendDetector = TrendDetection()
TrendDetector.run()
