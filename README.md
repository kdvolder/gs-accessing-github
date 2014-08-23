Using Spring Social to Access Github Data
=========================================

This contains a copy of the spring.io guide about [accessing twitter data](http://spring.io/guides/gs/accessing-twitter/).
It has been modified to do a similar thing using github rather than twitter.

Notes:
=====

When you are running this on localhost you need to make sure the app you register with github
points to the correct localhost url as its 'callback URL'. For example:

	http://localhost:8080/connect/github
	
This is because unlike Twitter, github will reject oauth requests if the callback url
specified in the request doesn't match what you specified registering your app.