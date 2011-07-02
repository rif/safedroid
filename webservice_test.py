import urllib
import httplib2

http = httplib2.Http()
response,content = http.request("http://portal.safefleet.eu/safefleet/webservice/authenticate/?username=demows&password=deadbeef2", 'POST')

print "Athentication response:"
print response
print '-'*100

headers = {}
headers['Cookie'] = response['set-cookie']

print "headers:"
print headers
print '-'*100

response,content = http.request("http://portal.safefleet.eu/safefleet/webservice/get_companies", headers=headers)

print "get_companies response:"
print response
print '-'*100
