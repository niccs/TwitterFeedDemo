# TwitterFeedDemo
Fetch the twitter feeds of a user using Application Only Authentication(OAuth2)  against twitter  REST API without using 3rd Party libs
Using the 
https://api.twitter.com/1.1/statuses/user_timeline.json?screen_name=<SCREEN_NAME> endpoint to get a list of tweets for a user.

## Screen Flow:-

<p align="center">
  <img src="https://cloud.githubusercontent.com/assets/5301598/14309021/1cf86714-fc1d-11e5-9087-d6f246beff74.png" width="300"/>
  
  
  <img src="https://cloud.githubusercontent.com/assets/5301598/14309029/2bb02314-fc1d-11e5-91d1-1b536e8cf739.png" width="300"/>
</p>


## Process Flow:--


It goes through the following steps:

•	 Register an application in Twitter to get a consumer key and consumer secret.

•	 Combine the key and secret together and encode it with a base64 encoding.

•	 Use that new encoded key to ask Twitter for a bearer token.

•	 Get the token back from Twitter, and then supply it in the headers of additional requests while fetching the feed.


## APIs used

•	Target SDK is 23 and build tools versions is “23.0.2”

•	Supports Android 4.3 upwards

•	Google gson lib (2.6.2) is used to convert the JSON tweet responses to listview.

•	URLConnection is used for GET and POST requests via REST API

•	And connecting and fetching the results from the network is handled  in async task


## Known Issues and work to be done:

•	REST API error handling to be done via JSON error response

•	Unit Test cases to be done

•	Resources to be modified for modulatization


