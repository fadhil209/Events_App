import requests
from bs4 import BeautifulSoup

USER_AGENT = {'User-Agent':'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36'}

def fetch_results(search_term, number_results, language_code):
    assert isinstance(search_term, str), 'Search term must be a string'
    assert isinstance(number_results, int), 'Number of results must be an integer'
    escaped_search_term = search_term.replace(' ', '+')

    google_url = 'https://www.google.com/search?q={}&num={}&hl={}'.format(escaped_search_term, number_results, language_code)
    response = requests.get(google_url, headers=USER_AGENT)
    response.raise_for_status()

    return search_term, response.text


def parse_results(html, keyword):
    soup = BeautifulSoup(html, 'html.parser')

    found_results = []
    result_block = soup.find_all('div', attrs={'class': 'h998We mlo-c'})
    for result in result_block:
        link = result.find('a', href=True) #filter the top list of the google search only
        title = result.find('div', attrs={'class': 'title'})
        date = result.find('div', attrs={'class':'aXUuyd'})
        location = result.find('span')
        time = result.find('div', attrs={'class': 'HoEOQb'})
        if link and title:
            link = link['href']
            title = title.get_text()
            date = date.get_text()
            if location:
                location = location.get_text()
            if time:
                time = time.get_text()
            #if link != '#':
            found_results.append({'keyword': keyword, 'title': title, 'location': location, 'link': link, 'date':date, 'time':time})

    return found_results


def scrape_google(search_term, number_results, language_code):
    try:
        keyword, html = fetch_results(search_term, number_results, language_code)
        results = parse_results(html, keyword)
        return results
    except AssertionError:
        raise Exception("Incorrect arguments parsed to function")
    except requests.HTTPError:
        raise Exception("You appear to have been blocked by Google")
    except requests.RequestException:
        raise Exception("Appears to be an issue with your connection")


if __name__ == '__main__':
    ''' keywords = ['events in antalya']
    data = []
    for keyword in keywords:
        try:
            results = scrape_google(keyword, 100, "en")
            for result in results:
                data.append(result)
        except Exception as e:
            print(e)
        finally:
            time.sleep(10)
    print(data)''' #if keyword file is maintained
    print(scrape_google('events in antalya', 100, 'en'))
