import json

tpl = '''\
<?xml version="1.0" encoding="UTF-8"?>
<kml xmlns="http://www.opengis.net/kml/2.2">
<Document>
  {0}
</Document>
</kml>
'''

pm_tpl = '''
<Placemark>
    <Point>
      <coordinates>{0},{1},0</coordinates>
    </Point>
</Placemark>
'''

mp = json.load(open('postcodes_to_latlng.json'))

r = []

for p in mp.values():
    if not p: continue
    r.append(pm_tpl.format(p[1], p[0]))

print tpl.format(''.join(r))