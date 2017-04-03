# Project 4 - TweeterPhile II

TweeterPhile is an android app that allows a user to view his Twitter timeline and post a new tweet.
He may switch tabs to view his mentions, a list of all tweets that mention his screen name.
He may also view his own or anyone's profile with background image and the history of all tweets by him.
The app utilizes [Twitter REST APIs] (https://dev.twitter.com/rest/public).

Time spent: 40 hours spent during week 2

## User Stories

The following **required** functionality is completed:

* [Y] The app includes **all required user stories** from Week 3 Twitter Client
* [Y] User can **switch between Timeline and Mention views using tabs**
  * [Y] User can view their home timeline tweets.
  * [Y] User can view the recent mentions of their username.
* [Y] User can navigate to **view their own profile**
  * [Y] User can see picture, tagline, # of followers, # of following, and tweets on their profile.
* [Y] User can **click on the profile image** in any tweet to see **another user's** profile.
 * [Y] User can see picture, tagline, # of followers, # of following, and tweets of clicked user.
 * [Y] Profile view includes that user's timeline
* [Y] User can [infinitely paginate](http://guides.codepath.com/android/Endless-Scrolling-with-AdapterViews-and-RecyclerView) any of these timelines (home, mentions, user) by scrolling to the bottom

The following **optional** features are implemented:

* [-] User can view following / followers list through the profile
* [Y] Implements robust error handling, [check if internet is available](http://guides.codepath.com/android/Sending-and-Managing-Network-Requests#checking-for-network-connectivity), handle error cases, network failures
* [-] When a network request is sent, user sees an [indeterminate progress indicator](http://guides.codepath.com/android/Handling-ProgressBars#progress-within-actionbar)
* [-] User can **"reply" to any tweet on their home timeline**
  * [-] The user that wrote the original tweet is automatically "@" replied in compose
* [-] User can click on a tweet to be **taken to a "detail view"** of that tweet
 * [-] User can take favorite (and unfavorite) or retweet actions on a tweet
* [-] User can **search for tweets matching a particular query** and see results
* [-] Usernames and hashtags are styled and clickable within tweets [using clickable spans](http://guides.codepath.com/android/Working-with-the-TextView#creating-clickable-styled-spans)

The following **bonus** features are implemented:

* [Y] Use Parcelable instead of Serializable using the popular [Parceler library](http://guides.codepath.com/android/Using-Parceler).
* [-] Leverages the [data binding support module](http://guides.codepath.com/android/Applying-Data-Binding-for-Views) to bind data into layout templates.
* [-] On the profile screen, leverage the [CoordinatorLayout](http://guides.codepath.com/android/Handling-Scrolls-with-CoordinatorLayout#responding-to-scroll-events) to [apply scrolling behavior](https://hackmd.io/s/SJyDOCgU) as the user scrolls through the profile timeline.
* [-] User can view their direct messages (or send new ones)

The following **additional** features are implemented:

* [Y] After inspecting the profile background image pixels, the view's text color is set
either black or white to stand out over the background image.
* [Y] Profile background images are cropped to fit the view, respecting the image aspect ratios.
If no background image is defined, as is the case of @POTUS, the user's background color is used on
 the header.
* [Y] Lean Activities with all view and data fetching logic in the Fragments.
* [Y] Tabbed Navigation with ViewPager


## Video Walkthrough

Here's a walkthrough of implemented user stories:  ()

<img src='http://i.imgur.com/link/to/your/gif/file.gif' title='Video Walkthrough' width='' alt='Video Walkthrough' />

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Notes

Describe any challenges encountered while building the app.
Periodically, the Twitter timeline API seems to return cached lists of tweets after a certain 
amount of history is gotten.

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Picasso transformations](http://www.wasabeef.jp/) - Image transformation library for Picasso
- [Parceler](http://www.parceler.org/) - Data serialization for Android


## License

    Copyright 2017 by Dave Friedman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
