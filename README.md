# Project 2 - Google Image Search

Google Image Search is a simple way to search for pictures and share them with your friends.  You can apply some basic filters and see a nice view.

Time spent: ~12 hours spent in total.

## User Stories

The following **required** functionality is completed:

* [x] User can enter a search query that will display a grid of image results from the Google Image API.
* [x] User can click on "settings" which allows selection of advanced search options to filter results
* [x] User can configure advanced search filters such as:
* [x] Size (small, medium, large, extra-large)
* [x] Color filter (black, blue, brown, gray, green, etc...)
* [x] Type (faces, photo, clip art, line art)
* [x] Site (espn.com)
* [x] Subsequent searches will have any filters applied to the search results
* [x] User can tap on any image in results to see the image full-screen
* [x] User can scroll down “infinitely” to continue loading more image results (up to 8 pages)

The following **optional** features are implemented:

* [x] Robust error handling, check if internet is available, handle error cases, network failures
* [x] Use the ActionBar SearchView or custom layout as the query box instead of an EditText
* [x] User can share an image to their friends or email it to themselves
* [x] Replace Filter Settings Activity with a lightweight modal overlay
* [x] Improve the user interface and experiment with image assets and/or styling and coloring

The following **bonus** features are implemented:

* [x] Use the StaggeredGridView to display improve the grid of image results
* [x] User can zoom or pan images displayed in full-screen detail view

The following **extra** features are also implemented:
* [x] Automatic alerting to network failure in activity.
* [x] Persistence of settings to sqllite.

## Video Walkthrough

[Imgur](http://i.imgur.com/kdGv0qu.gifv)

## Open-source libraries used

- [Android Async HTTP](https://github.com/loopj/android-async-http) - Simple asynchronous HTTP requests with JSON parsing
- [Picasso](http://square.github.io/picasso/) - Image loading and caching library for Android
- [Material Dialogs] (https://github.com/afollestad/material-dialogs) - clean modal design
- [Etsy Staggered Grid View] (https://github.com/etsy/AndroidStaggeredGrid) - clean staggered grid view
- [TouchImageView] (https://github.com/MikeOrtiz/TouchImageView) - clean implementation of touch view for images
