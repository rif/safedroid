import httplib2
import simplejson as json

http = httplib2.Http()
response,content = http.request("http://portal.safefleet.eu/safefleet/webservice/authenticate/?username=demows&password=deadbeef2", 'POST')

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

print "get_companies response:"
print response
print "\n".join(content.split("{"))
print '-'*100

# urllib2 opener test
#import urllib2
#opener = urllib2.build_opener()
#opener.addheaders.append(('Cookie', cookie))
#f = opener.open("http://portal.safefleet.eu/safefleet/webservice/get_companies/")
