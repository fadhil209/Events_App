#!/usr/bin/env python
# -*- coding: utf-8 -*-

import pyrebase;
from bs4 import BeautifulSoup
from selenium import webdriver
import urllib.request

USER_AGENT = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36'}

browser = webdriver.Firefox()
url = "http://www.mallofantalya.com.tr/TR/etkinlikler"
browser.get(url)
page1 = browser.page_source

url2 = "http://www.mallofantalya.com.tr/TR/etkinlikler?page=2"
browser.get(url2)
page2 = browser.page_source

page = page1+page2

def parse_results(html):
    soup = BeautifulSoup(html, 'html.parser')
    found_results = []
    result_block = soup.find_all('a', attrs={'class': 'comcard'})
    for result in result_block:
        title = result.find('span', attrs={'class': 'strname'})
        description = result.find('span', attrs={'class': "strdesc"})
        startdate = result.find('span', attrs={'class': "strstartdate"})
        enddate = result.find('span', attrs={'class': "strenddate"})
        image = result.find('img', src=True)
        if title:
            image = "http://www.mallofantalya.com.tr/" + image['src']
            title = title.get_text()
            startdate = startdate.get_text()
            enddate = enddate.get_text()
            description = description.get_text()
            found_results.append({'title': title, 'link': 'http://www.mallofantalya.com.tr/TR/etkinlikler',
                                  'startdate': startdate, 'enddate' : enddate,'image': image,
                                  'location': "Mall of Antalya", 'description' : description})
    return found_results

def downloadImages(datalist):
    counter1 = 0
    counter2= 0
    counter3 = 0
    config = {
        "apiKey": "AIzaSyCUZgaYTHb-gIKpPZ8zuFGzcYyvEInicxo",
        "authDomain": "eventsapp-3ac5f.firebaseapp.com",
        "databaseURL": "https://eventsapp-3ac5f.firebaseio.com",
        "storageBucket": "eventsapp-3ac5f.appspot.com"
    }
    firebase = pyrebase.initialize_app(config)
    email = "hebamohtaram@gmail.com"
    password = "123456"
    auth = firebase.auth()
    user = auth.sign_in_with_email_and_password(email, password)  # using firebase as a user
    storage = firebase.storage() #storing pics
    db = firebase.database() #real time data

    for item in datalist:
        img_url = item["image"]
        splitted = item["image"].split('/')
        filename = splitted[-1]
        urllib.request.urlretrieve(img_url , 'C:/Users/Heba/PycharmProjects/SP-Scrapper/DealsImages/'+filename)
        counter1 +=1
        storage.child("deals/"+filename).put('C:/Users/Heba/PycharmProjects/SP-Scrapper/DealsImages/'+ filename, user['idToken'])
        counter2 += 1
        #store the pictures in the storage of db
        xyz = storage.child("deals/"+filename).get_url(None)
        item.update({"img_uri": xyz})

        data = {"username": "Heba Mohtaram",
                "dealName": item["title"],
                "startdate": item["startdate"],
                "enddate" : item["enddate"],
                "description": item["description"] + item["link"],
                "location": item["location"],
                "uri": item["img_uri"]
                }
        db.child("deals").push(data, user['idToken'])
        counter3 += 1
    print (counter1 ,"many pictures were downloaded")
    print(counter2, "many pictures were uploaded")
    print(counter3, "unique deals were added to app")

downloadImages(parse_results(page))
print("testline")
