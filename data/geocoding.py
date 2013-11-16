import json, time
import urllib, urllib2

ps = '''\
E10 7NL
E14 8AH
E15 3EP
E15 3LE
E15 3LF
E16 4LB
E2 8QU
E3 3GH
E5 9QB
E6 3SE
E7 9QT
E8 3BY
E9 5DX
N1 8RL
N11 3BU
N15 4UH
N16 7TL
N17 6HZ
N17 9TR
N18 1PU
N4 1BX
N4 2BD
N4 2BD
N4 2EH
N5 1PL
N5 2JB
N7 0NL
N8 7AP
NW1 0PB
NW5 3EH
NW6 2AB
NW6 5YP
SE1 3BZ
SE10 8DF
SE14 6DN
SE15 3EJ
SE15 3LF
SE15 4LB
SE15 6BE
SE17 1QS
SE18 7SX
SE23 3LZ
SE25 6QY
SE26 5TH
SE26 6DP
SE28 8BB
SE28 8NT
SE4 2HU
SW10 0LA
SW10 9PB
SW17 9JJ
SW18 1NF
SW1P 1NL
SW3 3QG
SW4 7DH
SW4 7NY
SW4 OAQ
SW5 0NF
SW6 5QA
W12 0QU
W12 8HP
W12 9DN
W6 0PW
W8 6NX
W9 2PR
WC1X 0LR
WC2H 0BJ\
'''.split('\n')


def geocode(postcode):
    url_tpl = "http://maps.googleapis.com/maps/api/geocode/json?components=country:UK|postal_code:{0}&sensor=false"
    url = url_tpl.format(urllib.quote_plus(postcode))
    req = urllib2.urlopen(url)
    result = json.loads(req.read())
    if result['status'] == 'ZERO_RESULTS':
        return None
    if result['status'] != 'OK':
        raise Exception('API call returned %s' % result['status'])
    res = result['results'][0]['geometry']['location']
    return res['lat'], res['lng']

if __name__ == '__main__':
    psmap = {}
    for p in ps:
        if p not in psmap:
            psmap[p] = geocode(p)
            time.sleep(0.5)
    print json.dumps(psmap, indent=4)
