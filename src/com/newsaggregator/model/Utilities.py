import math
import string
import json 
import os
import numpy as np
import pickle 
import time 
import nltk

from unidecode import unidecode
from abc import ABC, abstractmethod

class StringProcessor():
    @staticmethod
    def process(str_input: str) -> str:
        '''
        Convert non-ascii characters to ascii ones, remove punctuations, redundant spaces
        Return: a processed string
        '''
        str_ascii = unidecode(str_input)
        translation_table = str.maketrans(string.punctuation, ' ' * len(string.punctuation))
        str_input_no_punct = str_ascii.translate(translation_table)
        preprocesed_str = ' '.join(str_input_no_punct.split())
        return preprocesed_str.lower()

class DocumentProcessor():
    @staticmethod
    def process(data: list, key: str) -> list:
        '''
        Process the string content at [key] of a list of dictionaries
        The process involve preprocessing each document using StringProcessor() 
        Then tokenize them into tokens
        
        Return: a list of lists, where inner list contain the tokens of processed article content
        '''
        corpus = []
        for content in data:
            detailed_content = StringProcessor.process(content[key])
            corpus.append(detailed_content)
        
        documents = [text.split() for text in corpus]
        return documents
        