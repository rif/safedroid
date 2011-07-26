import httplib2
import simplejson as json
import sys

USER = sys.argv[1] if len(sys.argv) == 3 else 'demows'
PASSWORD = sys.argv[2] if len(sys.argv) == 3 else 'deadbeef2'

http = httplib2.Http()
response,content = http.request("http://portal.safefleet.eu/safefleet/webservice/authenticate/?username=%(user)s&password=%(pass)s" % {'user': USER, 'pass': PASSWORD}, 'POST')

print "Athentication response:"
print response
print '-'*100

headers = {}
cookie = response['set-cookie']
headers['Cookie'] = cookie

print "headers:"
print headers
print '-'*100

response,content = http.request("http://portal.safefleet.eu/safefleet/webservice/get_vehicles/", headers=headers)

print "get_vehicles response:"
arr =  json.loads(content)
count = 0
for obj in arr:
    count += 1
    print obj, 
    r,c = http.request("http://portal.safefleet.eu/safefleet/webservice/get_vehicle_dynamic_info/?vehicle_id=" + str(obj['vehicle_id']), headers=headers)
    a =  json.loads(c)
    print "***** get_vehicle_dynamic_info:", a
print '-'*100

print "Total: %d vehicles." % count

