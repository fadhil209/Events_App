#!/usr/bin/env python
# -*- coding: utf-8 -*-

from bs4 import BeautifulSoup
from selenium import webdriver
import pyrebase

def parse_results(html):
    soup = BeautifulSoup(html, 'html.parser')
    found_results = []
    result_block = soup.find_all('div', attrs={'class': 'notliveevent'})
    for result in result_block:
        link = result.find('a', href=True)
        title = result.find('div', attrs={'class': 'searchResultEventNameMobile hiddenOnDesktop'})
        startdate = result.find('span', attrs={'class':'ln1'})
        image = result.find('img', src=True)
        location = result.find('a', attrs={'class': 'ln1 searchResultPlace'})
        if link and title:
            link = "http://www.biletix.com" + link['href']
            image = "http://www.biletix.com" + image['src']
            title = title.get_text()
            startdate = startdate.get_text()
            location =location.get_text()
            found_results.append({'title': title, 'link': link, 'startdate': startdate,
                                  'image': image, 'location': location, 'category': ''})
    return found_results

def fitImage (category, location):
    category = category.lower()
    config = {
        "apiKey": "AIzaSyCUZgaYTHb-gIKpPZ8zuFGzcYyvEInicxo",
        "authDomain": "eventsapp-3ac5f.firebaseapp.com",
        "databaseURL": "https://eventsapp-3ac5f.firebaseio.com",
        "storageBucket": "eventsapp-3ac5f.appspot.com"
    }
    firebase = pyrebase.initialize_app(config)
    storage = firebase.storage()  #storage of firebase

    if (location == "Jolly Joker Antalya"):
        img_uri = storage.child("events/" + "jollyjokerantalya.png").get_url(None)
    elif (location == "Holly Stone Performance Hall"):
        img_uri = storage.child("events/" + "hollystone.jpg").get_url(None)
    elif (location == "The Land of Legends"):
        img_uri = storage.child("events/" + "landoflegends.jpg").get_url(None)
    elif (location == "Konyaaltı Belediyesi Nazım Hikmet" and category == "art"):
        img_uri = storage.child("events/" + "art2.png").get_url(None)
    elif (location == "Çeşitli Mekanlar" and category == "art"):
        img_uri = storage.child("events/" + "art.png").get_url(None)
    elif (location == "Çeşitli Mekanlar" and category == "family"):
        img_uri = storage.child("events/" + "family.png").get_url(None)
    elif (location == "Çeşitli Mekanlar" and category == "music"):
        img_uri = storage.child("events/" + "music.png").get_url(None)
    elif (location == "Çeşitli Mekanlar" and category == "other"):
        img_uri = storage.child("events/" + "default.jpeg").get_url(None)
    elif (category == "art"):
        img_uri = storage.child("events/" + "art_default.jpg").get_url(None)
    elif (category == "music"):
        img_uri = storage.child("events/" + "music-default.jpg").get_url(None)
    elif(category == "sports"):
        img_uri = storage.child("events/" + "sports-default.png").get_url(None)
    elif(category == "family"):
        img_uri = storage.child("events/" + "family-default.jpg").get_url(None)
    elif(category == "other"):
        img_uri = storage.child("events/" + "others-default.jpg").get_url(None)
    else:
        img_uri = storage.child("events/" + "default.jpeg").get_url(None)
    return img_uri


def uploadData(datalist):
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
    user = auth.sign_in_with_email_and_password(email, password)
    db = firebase.database()
    counter = 0
    for item in datalist:

        data = {"username": "Heba Mohtaram",
                "category": item["category"],
                "date": item["startdate"],
                "deals": "",
                "description": item["link"],
                "eventId": user['idToken'],
                "eventName": item["title"],
                "location": item["location"],
                "time": "",
                "uri": fitImage(item["category"], item["location"])
                }
        db.child("events").push(data, user['idToken']) #pushing data into the app
        counter += 1
    print(counter, "unique events uploaded!")

USER_AGENT = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36'}

browser = webdriver.Firefox()
url = ""
page_result = []
page1_result = []
page2_result = []
final = []
category = ["SPORT", "OTHER" , "FAMILY" , "ART" ,"MUSIC"]
for i in category:
    url = "http://www.biletix.com/search/TURKIYE/tr?category_sb="+i+"&date_sb=-1&city_sb=Antalya#!category_sb:"+i+",city_sb:Antalya"
    browser.get(url)
    page1 = browser.page_source #html of the site
    page1_result = parse_results(page1) #parses
    xpath = "/html/body/div[8]/div[5]/div/div[2]/div[3]/div[5]/div[2]/div[4]/ul/li[3]/a"
    try:
        nextpage = browser.find_element_by_xpath(xpath)
        nextpage.click()
        browser.get(url)
        page2 = browser.page_source  # html of the site
        page2_result = parse_results(page2)  # parses
    except Exception as e:
        pass

    page_result = page1_result + page2_result
    for j in page_result:
        j.update({'category': i}) # add category

    final += page_result


if __name__ == '__main__':
    print(final)
    uploadData(final)
